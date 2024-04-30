package afamijas.service;

import afamijas.dao.NotificationsRepository;
import afamijas.dao.UsersRepository;
import afamijas.model.Notification;
import afamijas.model.User;
import afamijas.queuemail.model.dto.SendingResultDTO;
import afamijas.queuemail.services.QueuemailHardyService;
import afamijas.utils.SendMail;
import afamijas.utils.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;

@Service
public class NotificationsServiceImpl implements NotificationsService
{
    @Value("${debug.queries}")
    private Boolean debug_queries;


    @Value("${queuemail.use}")
    private String use_queuemail;


    final MongoTemplate mongoTemplate;
    final NotificationsRepository notificationsRepository;

    final UsersRepository usersRepository;

    final ErrorsService errorsService;

    final SendMail sendMail;

    final Template template;
    final QueuemailHardyService queuemailHardyService;


    @Autowired
    public NotificationsServiceImpl(MongoTemplate mongoTemplate, NotificationsRepository notificationsRepository, UsersRepository usersRepository, ErrorsService errorsService, SendMail sendMail, Template template, QueuemailHardyService queuemailHardyService)
    {
        this.mongoTemplate = mongoTemplate;
        this.notificationsRepository = notificationsRepository;
        this.usersRepository = usersRepository;
        this.errorsService = errorsService;
        this.sendMail = sendMail;
        this.template = template;
        this.queuemailHardyService = queuemailHardyService;
    }

    @Override
    public List<Notification> findMyNotifications(User user)
    {
        //Criteria criteria = new Criteria().orOperator(Criteria.where("iduser").is(user.get_id()), Criteria.where("role").is(user.getRole())); //NO LO HACEMOS ASÍ PORQUE LAS DE ROLE SE EXPANDEN A TODOS LOS USERS
        Criteria criteria = new Criteria().where("iduser").is(user.get_id());

        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "created"));

        if(debug_queries) System.out.println("findMyNotifications: " + query.getQueryObject().toJson());
        return this. mongoTemplate.find(query, Notification.class);
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void create(String iduser, List<String> roles, String title, String type, String message, String url)
    {
        try
        {
            if(iduser!=null)
            {
                User user = this.usersRepository.findOne(iduser);
                if(user==null || !user.getStatus().equals("A")) return;
                this.notificationsRepository.save(new Notification(iduser, null, title, type, message, url));
                if(type.equals("EMAIL") && user.getEmail_notifications()!=null && user.getEmail_notifications()==true)
                    this.sendNotificationByEmail(user, title, message);
            }
            else if(roles != null)
            {
                List<User> users = this.usersRepository.findUsersByRoleAndStatus(roles, "A");
                for(User user: users)
                {
                    this.notificationsRepository.save(new Notification(user.get_id(), null, title, type, message, url));
                    if(type.equals("EMAIL") && user.getEmail_notifications()!=null && user.getEmail_notifications()==true)
                        this.sendNotificationByEmail(user, title, message);
                }
            }
            else  //todo el mundo
            {
                List<User> userstonotify = this.usersRepository.findUsersByStatus("A");
                for(User user : userstonotify)
                {
                    this.notificationsRepository.save(new Notification(user.get_id(), null, title, type, message, url));
                    if(type.equals("EMAIL") && user.getEmail_notifications()!=null && user.getEmail_notifications()==true)
                        this.sendNotificationByEmail(user, title, message);
                }
            }
        }
        catch (Exception e) { e.printStackTrace();}
    }




    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void deleteAll(String iduser)
    {
        if(iduser==null) return;

        List<Notification> notificationList = this.notificationsRepository.findByUser(iduser);
        if(notificationList!=null)
            for(Notification notification : notificationList)
                this.notificationsRepository.delete(notification);
    }



    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void delete(String idnotification)
    {
        this.notificationsRepository.deleteById(idnotification);
    }






    private void sendNotificationByEmail(User user, String title, String message)
    {
        try
        {
            HashMap<String, String> values = new HashMap<String, String>();
            values.put("message", message);
            String body = template.parse("mail_notification.html", values);
            String subject = user.getUsername() + ": " + title;

            if(use_queuemail.equals("true"))
            {
                //ENVIAMOS POR QUEUEMAIL CON SERVICIO REBUSTO QUE IMPLEMENTA COLA LOCAL DE EMAILS FALLIDOS
                SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, "info@afamijas.org", "AFA Mijas",  user.getEmail(), user.getEmail(), null, null, "info@afamijas.org",
                        subject, "UTF-8", body, "UTF-8", "text/html",
                        null, null, null,
                        true, false,
                        true, null);

                if(sendingResultDTO==null || (sendingResultDTO.getLocalqueued()!=null && sendingResultDTO.getLocalqueued()==false) )
                {
                    this.errorsService.sendWarning("Email local-queuing failed", sendingResultDTO==null?"NULL":sendingResultDTO.getError());
                    this.sendMail.send("info@afamijas.org", user.getEmail(), "AFA Mijas", user.getEmail(), null, null, "info@afamijas.org", subject, "UTF-8", body, "UTF-8", null, "html");
                }
            }
            else
            {
                this.sendMail.send("info@afamijas.org", user.getEmail(), "AFA Mijas", user.getEmail(), null, null, "info@afamijas.org", subject, "UTF-8", body, "UTF-8", null, "html");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
