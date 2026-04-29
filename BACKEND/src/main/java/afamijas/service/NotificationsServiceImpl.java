package afamijas.service;

import afamijas.dao.CalendarEventsRepository;
import afamijas.dao.NotificationsRepository;
import afamijas.dao.UsersRepository;
import afamijas.model.CalendarEvent;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    final CalendarEventsRepository calendarEventsRepository;


    @Autowired
    public NotificationsServiceImpl(MongoTemplate mongoTemplate, NotificationsRepository notificationsRepository, UsersRepository usersRepository, ErrorsService errorsService, SendMail sendMail, Template template, QueuemailHardyService queuemailHardyService, CalendarEventsRepository calendarEventsRepository)
    {
        this.mongoTemplate = mongoTemplate;
        this.notificationsRepository = notificationsRepository;
        this.usersRepository = usersRepository;
        this.errorsService = errorsService;
        this.sendMail = sendMail;
        this.template = template;
        this.queuemailHardyService = queuemailHardyService;
        this.calendarEventsRepository = calendarEventsRepository;
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
    @Transactional
    public void create(String iduser, List<String> roles, String title, String type, String message, String url)
    {
        try
        {
            if(iduser!=null)
            {
                User user = this.usersRepository.findOne(iduser);
                if(user==null || !user.getStatus().equals("A")) return;
                this.notificationsRepository.save(new Notification(iduser, null, title, type, message, url));
                if(type.equals("EMAIL") && Boolean.TRUE.equals(user.getEmail_notifications()))
                    this.sendNotificationByEmail(user, title, message);
            }
            else if(roles != null)
            {
                List<User> users = this.usersRepository.findUsersByRoleAndStatus(roles, "A");
                for(User user: users)
                {
                    this.notificationsRepository.save(new Notification(user.get_id(), null, title, type, message, url));
                    if(type.equals("EMAIL") && Boolean.TRUE.equals(user.getEmail_notifications()))
                        this.sendNotificationByEmail(user, title, message);
                }
            }
            else  //todo el mundo
            {
                List<User> userstonotify = this.usersRepository.findUsersByStatus("A");
                for(User user : userstonotify)
                {
                    this.notificationsRepository.save(new Notification(user.get_id(), null, title, type, message, url));
                    if(type.equals("EMAIL") && Boolean.TRUE.equals(user.getEmail_notifications()))
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
            values.put("title", title);
            values.put("message", message);

            String subject = "Nueva notificación: " + title;
            String body = template.parse("mail_notification.html", values);

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


    @Scheduled(fixedDelay = 60000) // cada 1 minuto
    @Transactional
    public void processAlerts() {

        LocalDateTime now = LocalDateTime.now().plusMinutes(1); //para adelantarlas un minuto

        List<CalendarEvent> emailEvents = calendarEventsRepository.findPendingAlerts(now);

        for (CalendarEvent event : emailEvents) {

            try {

                // EMAIL
                if (!Boolean.TRUE.equals(event.getEmailsent())) {
                    sendAlertByEmal(event);
                    event.setEmailsent(true);
                }

                // NOTIFICATION
                if (!Boolean.TRUE.equals(event.getNotificationsent())) {
                    if (event.getIdworker() != null) {
                        this.create(
                                event.getIdworker(),
                                null,
                                event.getTitle(),
                                "NORMAL",
                                event.getDescription(),
                                event.getUrl()
                        );
                    }
                    event.setNotificationsent(true);
                }

                calendarEventsRepository.save(event);

            } catch (Exception e) {
                System.err.println("Error event " + event.get_id() + ": " + e.getMessage());
            }
        }


    }


    private void sendAlertByEmal(CalendarEvent calendarEvent)
    {
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            if(calendarEvent.getIdworker()==null) return;
            User user = this.usersRepository.findOne(calendarEvent.getIdworker());
            if(user==null) return;

            String subject = "Recordatorio: " + calendarEvent.getTitle();

            String message = "Tienes un evento programado:<br/>"
                    + "<b>" + calendarEvent.getTitle() + "</b><br/>"
                    + "Desde: " + calendarEvent.getStart().format(formatter) + "<br/><br/>";
            if(calendarEvent.getEnd()!=null) message +=  "Hasta: " + calendarEvent.getStart().format(formatter) + "<br/><br/>";
            message += (calendarEvent.getDescription() != null ? calendarEvent.getDescription() : "");

            HashMap<String, String> values = new HashMap<String, String>();
            values.put("title", subject);
            values.put("message", message);
            String body = template.parse("mail_notification.html", values);

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
