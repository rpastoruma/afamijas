package afamijas.queuemail.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import afamijas.queuemail.repositories.QueueEmailAttachmentsRepository;
import afamijas.queuemail.repositories.QueueEmailsRepository;
import afamijas.queuemail.model.QueueEmail;
import afamijas.queuemail.model.QueueEmailAttachment;
import afamijas.queuemail.model.dto.LoginResponseDTO;
import afamijas.queuemail.model.dto.SendingResultDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



@Service
@Component
public class QueuemailHardyService
{
    private static final String API_MAIN_URL = "https://api.queuemail.dev/api";

    @Value("${queuemail.username}")
    private String username;

    @Value("${queuemail.password}")
    private String password;

    @Value("${queuemail.idapp}")
    private String idapp;

    final QueueEmailsRepository queueEmailsRepository;
    final QueueEmailAttachmentsRepository queueEmailAttachmentsRepository;

    public QueuemailHardyService(QueueEmailsRepository queueEmailsRepository, QueueEmailAttachmentsRepository queueEmailAttachmentsRepository)
    {
        this.queueEmailsRepository = queueEmailsRepository;
        this.queueEmailAttachmentsRepository = queueEmailAttachmentsRepository;
    }


    public LoginResponseDTO login() throws Exception
    {
        try
        {
            HttpResponse response = Request.Post(API_MAIN_URL + "/auth/login").bodyForm(Form.form()
                    .add("username", username)
                    .add("password", password)
                    .build()).execute().returnResponse();

            if (response.getStatusLine().getStatusCode() == 200)
            {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                LoginResponseDTO res =  mapper.readValue(response.getEntity().getContent(), LoginResponseDTO.class);
                return res;
            }
            else if (response.getStatusLine().getStatusCode() == 403)
            {
                throw new Exception("USER_PASSWORD_NOT_VALID");
            }
            else
            {
                throw new Exception("ERROR_LOGIN");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("GENERAL_ERROR");
        }
    }

    public SendingResultDTO sendEmail(String token,
                                      String from, String fromname, String to, String toname,
                                      String ccs, String bccs, String replyto,
                                      String subject, String subjectencoding, String body, String bodyencoding,
                                      String mimetype,
                                      List<File> attachments,
                                      List<String> urlattachments,
                                      String attachmenttype,
                                      Boolean includetracking,
                                      Boolean includeunregisterlink,
                                      Boolean localqueue,
                                      Boolean removeAttachmentsAfterSent)
    {
        try
        {
            final SendingResultDTO[] res = new SendingResultDTO[1];

            if(token==null) token = this.login().getToken();
            if(token==null)
            {
                return new SendingResultDTO("AUTH_ERROR",
                        this.queueEmail(idapp,
                                from, fromname, to, toname,
                                ccs, bccs, replyto,
                                subject, subjectencoding, body, bodyencoding,
                                mimetype,
                                attachments,
                                urlattachments,
                                attachmenttype,
                                includetracking,
                                includeunregisterlink,
                                localqueue,
                                removeAttachmentsAfterSent));
            }

            HttpPost httppost = new HttpPost(API_MAIN_URL + "/private/emails/send");
            httppost.addHeader("Authorization", "Bearer " + token);

            MultipartEntityBuilder mpeb = MultipartEntityBuilder.create();

            if(attachments!=null && attachments.size()>0)
                for(File mf : attachments)
                {
                    mpeb.addPart("attachments", new FileBody(mf));
                }

            mpeb.addPart("idapp", new StringBody(idapp, ContentType.TEXT_PLAIN));
            if(from!=null) mpeb.addPart("from", new StringBody(from, ContentType.TEXT_PLAIN));
            if(fromname!=null) mpeb.addPart("fromname", new StringBody(fromname, ContentType.TEXT_PLAIN));
            if(to!=null) mpeb.addPart("tos", new StringBody(to, ContentType.TEXT_PLAIN));
            if(toname!=null) mpeb.addPart("tonames", new StringBody(toname, ContentType.TEXT_PLAIN));
            if(ccs!=null) mpeb.addPart("ccs", new StringBody(ccs, ContentType.TEXT_PLAIN));
            if(bccs!=null) mpeb.addPart("bccs", new StringBody(bccs, ContentType.TEXT_PLAIN));
            if(replyto!=null) mpeb.addPart("replyto", new StringBody(replyto, ContentType.TEXT_PLAIN));
            if(subject!=null) mpeb.addPart("subject", new StringBody(subject, ContentType.TEXT_PLAIN));
            if(subjectencoding!=null) mpeb.addPart("subjectencoding", new StringBody(subjectencoding, ContentType.TEXT_PLAIN));
            if(body!=null) mpeb.addPart("body", new StringBody(body, ContentType.TEXT_PLAIN));
            if(bodyencoding!=null) mpeb.addPart("bodyencoding", new StringBody(bodyencoding, ContentType.TEXT_PLAIN));
            if(mimetype!=null) mpeb.addPart("mimetype", new StringBody(mimetype, ContentType.TEXT_PLAIN));
            if(attachmenttype!=null) mpeb.addPart("attachmenttype", new StringBody(attachmenttype, ContentType.TEXT_PLAIN));
            if(urlattachments!=null) mpeb.addPart("urlattachments", new StringBody(String.join(",", urlattachments), ContentType.TEXT_PLAIN));
            if(includetracking!=null) mpeb.addPart("includetracking", new StringBody(""+includetracking, ContentType.TEXT_PLAIN));
            if(includeunregisterlink!=null) mpeb.addPart("includeunregisterlink", new StringBody(""+includeunregisterlink, ContentType.TEXT_PLAIN));

            HttpEntity reqEntity = mpeb.build();
            httppost.setEntity(reqEntity);

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());

            try(CloseableHttpClient client = HttpClientBuilder.create().build())
            {
                client.execute(httppost, response -> {
                    if (response.getStatusLine().getStatusCode() == 200)
                    {
                        if(removeAttachmentsAfterSent!=null && removeAttachmentsAfterSent==true)
                            for(File file : attachments) try { file.delete(); } catch (Exception e) { e.printStackTrace();}

                        res[0] = mapper.readValue(response.getEntity().getContent(), SendingResultDTO.class);
                    }
                    else if (response.getStatusLine().getStatusCode() == 401)
                    {
                        res[0] = new SendingResultDTO("Not authorized", false);
                    }
                    else if (response.getStatusLine().getStatusCode() == 403)
                    {
                        res[0] = new SendingResultDTO("Credentials not valid", false);
                    }
                    else if (response.getStatusLine().getStatusCode() == 406)
                    {
                        String responseString = "";
                        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String readLine;
                        while (((readLine = br.readLine()) != null)) responseString += readLine;
                        br.close();
                        res[0] = new SendingResultDTO(responseString,
                        this.queueEmail(idapp,
                                from, fromname, to, toname,
                                ccs, bccs, replyto,
                                subject, subjectencoding, body, bodyencoding,
                                mimetype,
                                attachments,
                                urlattachments,
                                attachmenttype,
                                includetracking,
                                includeunregisterlink,
                                localqueue==null?false:localqueue,
                                removeAttachmentsAfterSent==null?false:removeAttachmentsAfterSent));
                    }
                    else
                    {
                        res[0] = new SendingResultDTO("ERROR_CAN_RETRY_SENDING_EMAIL",
                        this.queueEmail(idapp,
                                from, fromname, to, toname,
                                ccs, bccs, replyto,
                                subject, subjectencoding, body, bodyencoding,
                                mimetype,
                                attachments,
                                urlattachments,
                                attachmenttype,
                                includetracking,
                                includeunregisterlink,
                                localqueue==null?false:localqueue,
                                removeAttachmentsAfterSent==null?false:removeAttachmentsAfterSent));
                    }

                    return res[0];
                });


            } catch (Exception e) { throw e; }

            return res[0];
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new SendingResultDTO("GENERAL_ERROR",
                    this.queueEmail(idapp,
                            from, fromname, to, toname,
                            ccs, bccs, replyto,
                            subject, subjectencoding, body, bodyencoding,
                            mimetype,
                            attachments,
                            urlattachments,
                            attachmenttype,
                            includetracking,
                            includeunregisterlink,
                            localqueue==null?false:localqueue,
                            removeAttachmentsAfterSent==null?false:removeAttachmentsAfterSent));
        }
    }


    private boolean queueEmail(String idapp,
                            String from, String fromname, String to, String toname,
                            String ccs, String bccs, String replyto,
                            String subject, String subjectencoding, String body, String bodyencoding,
                            String mimetype,
                            List<File> attachments,
                            List<String> urlattachments,
                            String attachmenttype,
                            Boolean includetracking,
                            Boolean includeunregisterlink,
                            Boolean localqueue,
                            Boolean removeAttachmentsAfterSent)
    {
        try
        {
            if(!localqueue) return  false;

            QueueEmail queueEmail = new QueueEmail();
            queueEmail.setIdapp(idapp);
            queueEmail.setFrom(from);
            queueEmail.setFromname(fromname);
            queueEmail.setTo(to);
            queueEmail.setToname(toname);
            queueEmail.setCcs(ccs);
            queueEmail.setBccs(bccs);
            queueEmail.setReplyto(replyto);
            queueEmail.setSubject(subject);
            queueEmail.setSubjectencoding(subjectencoding);
            queueEmail.setBody(body);
            queueEmail.setBodyencoding(bodyencoding);
            queueEmail.setMimetype(mimetype);
            queueEmail.setUrlattachments(urlattachments);
            queueEmail.setAttachmentstype(attachmenttype);
            queueEmail.setIncludetracking(includetracking);
            queueEmail.setIncludeUnregisterLink(includeunregisterlink);
            queueEmail = this.queueEmailsRepository.save(queueEmail);

            if (attachments != null && attachments.size() > 0)
                for (File file : attachments) {
                    QueueEmailAttachment queueEmailAttachment = new QueueEmailAttachment();
                    queueEmailAttachment.setIdemail(queueEmail.get_id());
                    queueEmailAttachment.setFilepath(file.getAbsolutePath());
                    queueEmailAttachment.setRemoveAfterSent(removeAttachmentsAfterSent);
                    this.queueEmailAttachmentsRepository.save(queueEmailAttachment);
                }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Scheduled(fixedRate = 1000*60*5) // 5 minutes
    public void processQueuedEmails()
    {
        try
        {
            List<QueueEmail> pendingemails = this.queueEmailsRepository.findAll(); //TODO: improve sorting in order to better selection based on created or numretries
            if(pendingemails==null || pendingemails.size()==0) return;

            System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - Procesing queued email.");

            String token = this.login().getToken();
            if(token==null) throw new Exception("NO VALID TOKEN");

            for(QueueEmail queueEmail : pendingemails)
            {
                try
                {
                    List<File> attachments = null;
                    List<QueueEmailAttachment> qattachments = this.queueEmailAttachmentsRepository.findByEmail(queueEmail.get_id());
                    if(qattachments!=null && qattachments.size()>0)
                    {
                        attachments = new ArrayList<>(qattachments.size());
                        for(QueueEmailAttachment queueEmailAttachment : qattachments)
                            attachments.add(new File(queueEmailAttachment.getFilepath()));
                    }

                    SendingResultDTO sendingResultDTO = this.sendEmail(token,
                            queueEmail.getFrom(), queueEmail.getFromname(),  queueEmail.getTo(), queueEmail.getToname(),
                            queueEmail.getCcs(), queueEmail.getBccs(), queueEmail.getReplyto(),
                            queueEmail.getSubject(), queueEmail.getSubjectencoding(), queueEmail.getBody(), queueEmail.getBodyencoding(),
                            queueEmail.getMimetype(),
                            attachments,
                            queueEmail.getUrlattachments(),
                            queueEmail.getAttachmentstype(),
                            queueEmail.getIncludetracking(),
                            queueEmail.getIncludeUnregisterLink(),
                            false, //at queue process do not queue up again!
                            null);


                    // SI EL ERROR NO ES REINTENTABLE PONEMOS ERROR A NULL PARA QUE ENTRE POR EL ELSE DE ABAJO Y LO BORRE SIN ENVIAR PUESTO QUE NUNCA SE VA A ENVIAR Y LOGUEAMOS
                    if(sendingResultDTO!=null && sendingResultDTO.getError()!=null && !sendingResultDTO.getError().equals("ERROR_CAN_RETRY_SENDING_EMAIL"))
                    {
                        sendingResultDTO.setError(null);
                        sendingResultDTO.setStatus("E");
                    }


                    if(sendingResultDTO==null || sendingResultDTO.getStatus()==null || sendingResultDTO.getError()!=null)
                    {
                        queueEmail.setNumretries(queueEmail.getNumretries()+1);
                        queueEmail.setModified(LocalDateTime.now());
                        this.queueEmailsRepository.save(queueEmail);
                    }
                    else
                    {
                        this.queueEmailsRepository.delete(queueEmail);
                        try
                        {
                            if(qattachments!=null && qattachments.size()>0)
                            {
                                for(QueueEmailAttachment queueEmailAttachment : qattachments)
                                   if(queueEmailAttachment.getRemoveAfterSent())
                                       new File(queueEmailAttachment.getFilepath()).delete();
                            }
                        }
                        catch (Exception e) { e.printStackTrace(); }
                        this.queueEmailAttachmentsRepository.deleteByEmail(queueEmail.get_id());
                    }
                }
                catch (Exception e)
                {
                    try { queueEmail.setNumretries(queueEmail.getNumretries()+1); queueEmail.setModified(LocalDateTime.now()); this.queueEmailsRepository.save(queueEmail); } catch (Exception ee) {}
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }






}
