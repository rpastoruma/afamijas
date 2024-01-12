package afamijas.utils;

import afamijas.queuemail.model.QueueEmail;
import afamijas.queuemail.model.QueueEmailAttachment;
import afamijas.queuemail.model.dto.SendingResultDTO;
import afamijas.queuemail.repositories.QueueEmailAttachmentsRepository;
import afamijas.queuemail.repositories.QueueEmailsRepository;
import afamijas.queuemail.services.QueuemailHardyService;
import afamijas.service.ConfigurationService;
import afamijas.service.LogsService;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Configuration
public class SendMail
{
    private ArrayList<SMTPServer> smtps = null;
    private Session session = null;
    private Transport t = null;

    @Autowired
    private Environment env;

    @Value("${spring.profiles.active}")
    private String active_profile;

    @Value("${test.emailto}")
    private String test_emailto;

    final ConfigurationService configurationService;
    final LogsService logsService;
    final QueueEmailsRepository queueEmailsRepository;
    final QueueEmailAttachmentsRepository queueEmailAttachmentsRepository;
    final QueuemailHardyService queuemailHardyService;

    @Value("${queuemail.use}")
    private String queuemail_use;

    final static int NUM_MAX_LOCAL_RETRIES = 3;

    @Autowired
    public SendMail(ConfigurationService configurationService, LogsService logsService, QueueEmailsRepository queueEmailsRepository, QueueEmailAttachmentsRepository queueEmailAttachmentsRepository, QueuemailHardyService queuemailHardyService)
    {
        this.configurationService = configurationService;
        this.logsService = logsService;
        this.queueEmailsRepository = queueEmailsRepository;
        this.queueEmailAttachmentsRepository = queueEmailAttachmentsRepository;
        this.queuemailHardyService = queuemailHardyService;
    }


    public void send(String from, String to, String subject, String body, String bodyformat) throws Exception
    {
        send(from, to, from, to, null, null, null, subject, "UTF-8", body, "UTF-8", null, bodyformat);
    }




    public void send(String from, String to, String fromname, String toname,
                     String cc, String bcc, String replyto,
                     String subject, String subjectencoding, /* UTF-8, ISO-8859-1, ... */
                     String body, String bodyencoding, /* UTF-8, ISO-8859-1, ... */
                     ArrayList<Attachment> attachments,
                     String bodyformat  /* html o plain */)  throws Exception
    {
        try
        {
            if(queuemail_use.equals("true"))
            {
                List<File> attachmentArrayList = new ArrayList<>();
                if(attachments!=null && attachments.size()>0)
                {
                    for(Attachment at : attachments)
                        attachmentArrayList.add(new File(at.getWholePath()));
                }

                SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, from, fromname,  to, toname, cc, bcc, replyto,
                        subject, subjectencoding, body, bodyencoding, "text/" + bodyformat,
                        attachmentArrayList, null, "FILE",
                        false, false,
                        true, true);

                if(sendingResultDTO==null || (sendingResultDTO.getLocalqueued()!=null && sendingResultDTO.getLocalqueued()==false) )
                {
                    this.send(from, to, fromname, toname, cc, bcc, replyto, subject, subjectencoding, body, bodyencoding, attachments, bodyformat, true);
                }
            }
            else
            {
                this.send(from, to, fromname, toname, cc, bcc, replyto, subject, subjectencoding, body, bodyencoding, attachments, bodyformat, true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.send(from, to, fromname, toname, cc, bcc, replyto, subject, subjectencoding, body, bodyencoding, attachments, bodyformat, true);
        }
    }

    public void send(String from, String to, String fromname, String toname,
                     String cc, String bcc, String replyto,
                     String subject, String subjectencoding, /* UTF-8, ISO-8859-1, ... */
                     String body, String bodyencoding, /* UTF-8, ISO-8859-1, ... */
                     ArrayList<Attachment> attachments,
                     String bodyformat  /* html o plain */, boolean dummy)  throws Exception
    {

        if(!active_profile.equals("prod"))
        {
            to = test_emailto;
            if(cc!=null && !cc.equals("")) cc = "cc" + test_emailto;
            if(bcc!=null && !bcc.equals("")) bcc = "bcc" + test_emailto;
            if(replyto!=null && !replyto.equals("")) replyto = "replyto" + test_emailto;
        }

        to = StringUtils.replaceString(to, ";", ",");
        if(cc!=null) cc = StringUtils.replaceString(cc, ";", ",");
        if(bcc!=null)  bcc = StringUtils.replaceString(bcc, ";", ",");


    
        if(smtps==null || smtps.size()==0) init();
        if(smtps.size()>=0)
            for(int i=0; i<smtps.size(); i++)
            {
                System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - SENDMAIL: Enviando con SMTP: [" + smtps.get(i).getHost() + "]");

                /*
                System.out.println(smtps.get(i).getHost());
                System.out.println( smtps.get(i).getUser());
                System.out.println( smtps.get(i).getPass());
                System.out.println(  smtps.get(i).getPort());
                System.out.println( smtps.get(i).getAuth());
                */

                if(smtps.get(i).getStarttls())
                {
                    System.out.println("\tSTARTTLS ON");
                    Properties props = System.getProperties();
                    props.put("mail.smtp.starttls.enable", true); // added this line
                    props.put("mail.smtp.host", smtps.get(i).getHost());
                    props.put("mail.smtp.user", smtps.get(i).getUser());
                    props.put("mail.smtp.password", smtps.get(i).getPass());
                    props.put("mail.smtp.port",  smtps.get(i).getPort());
                    props.put("mail.smtp.auth",  smtps.get(i).getAuth());
                    props.put("mail.debug", "false");

                    session = Session.getInstance(props,null);
                }
                else
                {
                    System.out.println("\tSTARTTLS OFF");
                    if(session==null) session = getSession(i);
                }


                // 2 - CONSTRUÍMOS MENSAJE:
                // Establecemos Session
                MimeMessage mimemsg = new MimeMessage(session);

                // Construimos las partes del mensaje
                BodyPart messageBodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();

                // Cabeceras
                // Se aconseja dejarlo en blanco http://www.mailingcheck.com/faking-x-mailer-email-field-never-rewards/ -->
                //mimemsg.setHeader("X-Mailer", "Pedroche.NET Java FrameWork SMTP Server");
                // Necesarias? -->
                //mimemsg.setHeader("Content-Transfer-Encoding", "8bit");
                //mimemsg.setHeader("Content-Type", "text/" + bodyformat + "; charset=\"" + bodyencoding + "\"");
                mimemsg.setSentDate(new Date());

                // Remitente y destinatarios
                System.out.println("FROM:<"  + from + ">");
                mimemsg.setFrom(new InternetAddress(from, fromname));
                System.out.println("FROM:<"  + from + ">");

                StringTokenizer stk1 = new StringTokenizer(to, ",");
                StringTokenizer stk2 = new StringTokenizer(toname, ",");
                while(stk1.hasMoreElements())
                {
                    String rcptto = ((String) stk1.nextElement()).trim();
                    System.out.println("TO:<"  + rcptto + ">");
                    String rcptnameto = rcptto;
                    try{ rcptnameto = ( (String) stk2.nextElement()).trim(); } catch(NoSuchElementException e) {}
                    System.out.println("TO:"  + rcptto);
                    mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(rcptto, rcptnameto));
                }

                if (cc != null) {  System.out.println("cc:<"  + cc + ">"); mimemsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); }
                if (bcc != null) {  System.out.println("bcc:<"  + bcc + ">"); mimemsg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc)); }
                if (replyto != null) {  System.out.println("replyto:<"  + replyto + ">");  mimemsg.setReplyTo(new InternetAddress[]{ new InternetAddress(replyto, replyto)}); }

                // Contenido texto

                mimemsg.setSubject(subject, subjectencoding);
                if (attachments != null)
                    messageBodyPart.setContent(body ,"text/" + bodyformat + "; charset=" + bodyencoding);
                else
                    mimemsg.setContent(body ,"text/" + bodyformat + "; charset=" + bodyencoding);
                multipart.addBodyPart(messageBodyPart);

                // Fichero Adjuntos
                if(attachments!=null)
                {
                    Iterator<Attachment> msg_attachments = attachments.iterator();
                    while (msg_attachments.hasNext())
                    {
                        Attachment at = (Attachment) msg_attachments.next();
                        String filename = at.getFileName();
                        String wholepath = at.getWholePath();
                        messageBodyPart = new MimeBodyPart();
                        FileDataSource fds  = new FileDataSource(wholepath);
                        messageBodyPart.setDataHandler(new DataHandler(fds) );
                        messageBodyPart.setFileName(filename);
                        if(at.getContenttype()!=null) messageBodyPart.setHeader("Content-Type", at.getContenttype());
                        multipart.addBodyPart(messageBodyPart);
                    }
                    mimemsg.setContent(multipart);
                }

                mimemsg.saveChanges();

                // 3 - ENVÍO DEL MENSAJE:
                if(smtps.get(i).getStarttls())
                {
                    Transport transport = session.getTransport("smtp");
                    transport.connect(smtps.get(i).getHost(), smtps.get(i).getUser(), smtps.get(i).getPass());
                    System.out.println("\tTransport (1): "+transport.toString());
                    for(Address a : mimemsg.getAllRecipients()) System.out.println("\tTO: " + a.toString());
                    try { transport.sendMessage(mimemsg, mimemsg.getAllRecipients()); } catch(Exception e) { e.printStackTrace(); session = null; t=null;  continue; }
                    try { this.logsService.save("SendEmail", "SMTPEmail", null, null, to + "\n" + subject, smtps.get(i).getHost()); } catch(Exception e) {  }
                    break;
                }
                else
                {
                    try
                    {
                        if(t==null)
                        {
                            t = session.getTransport("smtp");
                            try { t.connect(); } catch(Exception e) { t.connect(smtps.get(i).getUser(), smtps.get(i).getPass()); }
                        }
                        System.out.println("\tTransport (2): "+t.toString());
                        for(Address a : mimemsg.getAllRecipients()) System.out.println("\tTO: " + a.toString());
                        t.sendMessage(mimemsg, mimemsg.getAllRecipients());
                        try { this.logsService.save("SendEmail", "SMTPEmail", null, null, to + "\n" + subject, smtps.get(i).getHost()); } catch(Exception e) {  }
                    }
                    catch(Exception e) { e.printStackTrace();  session = null; t=null;  continue; }
                    break;
                }
            }
        else
        {
            throw new Exception("No hay SMTP configurados.");
        }
    }


    private void init()
    {
        try
        {
            smtps = new ArrayList<>();

            int i=1;
            while(!this.configurationService.value("mail.smtp" + i + ".host").equals(""))
            {

                smtps.add( new SMTPServer(
                        this.configurationService.value("mail.smtp" + i + ".host").trim(),
                        Integer.parseInt(this.configurationService.value("mail.smtp" + i + ".port").trim()),
                        Boolean.parseBoolean(this.configurationService.value("mail.smtp" + i + ".authenticate").trim()),
                        this.configurationService.value("mail.smtp" + i + ".user").trim(),
                        this.configurationService.value("mail.smtp" + i + ".pass").trim(),
                        Boolean.parseBoolean(this.configurationService.value("mail.smtp" + i + ".starttls").trim())
                ) );
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }




    private Session getSession(int i)
    {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", smtps.get(i).getHost());
        properties.setProperty("mail.smtp.port", ""+ smtps.get(i).getPort());

        if(smtps.get(i).getAuth())
        {
            Authenticator authenticator = new Authenticator(i);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
            return Session.getInstance(properties, authenticator);
        }
        else
        {
            return Session.getDefaultInstance(properties, null);
        }
    }


    private class Authenticator extends jakarta.mail.Authenticator
    {
        private PasswordAuthentication authentication;

        public Authenticator(int i)
        {
            authentication = new PasswordAuthentication(smtps.get(i).getUser(), smtps.get(i).getPass());
        }

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return authentication;
        }
    }




    @Scheduled(fixedRate = 1000*60*15) // 15 minutes
    public void sendFailedLocalQueued()
    {
        try
        {
            List<QueueEmail> pendingemails = this.queueEmailsRepository.findAll(); //TODO: improve sorting in order to better selection based on created or numretries
            if(pendingemails==null || pendingemails.size()==0) return;

            System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - Sending failed local queued email.");

            for(QueueEmail queueEmail : pendingemails)
            {

                List<File> attachments = null;
                List<QueueEmailAttachment> qattachments = this.queueEmailAttachmentsRepository.findByEmail(queueEmail.get_id());
                if(qattachments!=null && qattachments.size()>0)
                {
                    attachments = new ArrayList<>(qattachments.size());
                    for(QueueEmailAttachment queueEmailAttachment : qattachments)
                        attachments.add(new File(queueEmailAttachment.getFilepath()));
                }


                // IF LIMIT REACHED REMOVE FROM QUEUE AND SEND IT USING TRADITIONAL MODE
                if(queueEmail.getNumretries() >= NUM_MAX_LOCAL_RETRIES)
                {
                    ArrayList<Attachment> listAttachments = null;
                    try
                    {
                        if(qattachments!=null && qattachments.size()>0)
                        {
                            listAttachments = new ArrayList<>();
                            for(QueueEmailAttachment queueEmailAttachment : qattachments)
                                if(queueEmailAttachment.getRemoveAfterSent())
                                {
                                    String filename = Paths.get(queueEmailAttachment.getFilepath()).getFileName().toString();
                                    listAttachments.add(new Attachment(queueEmailAttachment.getFilepath(),filename ,getContentTypeByFileName(filename)));
                                    new File(queueEmailAttachment.getFilepath()).delete();
                                }
                        }
                    }
                    catch (Exception e) { e.printStackTrace(); }

                    this.send(queueEmail.getFrom(), queueEmail.getTo(), queueEmail.getFromname(), queueEmail.getToname(), queueEmail.getCcs(), queueEmail.getBccs(), queueEmail.getReplyto(), queueEmail.getSubject(), queueEmail.getSubjectencoding(), queueEmail.getBody(), queueEmail.getBodyencoding(), listAttachments, StringUtils.replaceString(queueEmail.getMimetype(), "text/", ""), true );


                    if(qattachments!=null && qattachments.size()>0)
                    {
                        for(QueueEmailAttachment queueEmailAttachment : qattachments)
                            this.queueEmailAttachmentsRepository.delete(queueEmailAttachment);
                    }

                    this.queueEmailsRepository.delete(queueEmail);

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }





    private static final Map<String, String> fileExtensionMap;

    static
    {
        fileExtensionMap = new HashMap<String, String>();
        // MS Office
        fileExtensionMap.put("doc", "application/msword");
        fileExtensionMap.put("dot", "application/msword");
        fileExtensionMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        fileExtensionMap.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        fileExtensionMap.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        fileExtensionMap.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
        fileExtensionMap.put("xls", "application/vnd.ms-excel");
        fileExtensionMap.put("xlt", "application/vnd.ms-excel");
        fileExtensionMap.put("xla", "application/vnd.ms-excel");
        fileExtensionMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        fileExtensionMap.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        fileExtensionMap.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        fileExtensionMap.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        fileExtensionMap.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        fileExtensionMap.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        fileExtensionMap.put("ppt", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pot", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pps", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("ppa", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        fileExtensionMap.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        fileExtensionMap.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        fileExtensionMap.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        fileExtensionMap.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        fileExtensionMap.put("potm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        fileExtensionMap.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        // Open Office
        fileExtensionMap.put("odt", "application/vnd.oasis.opendocument.text");
        fileExtensionMap.put("ott", "application/vnd.oasis.opendocument.text-template");
        fileExtensionMap.put("oth", "application/vnd.oasis.opendocument.text-web");
        fileExtensionMap.put("odm", "application/vnd.oasis.opendocument.text-master");
        fileExtensionMap.put("odg", "application/vnd.oasis.opendocument.graphics");
        fileExtensionMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        fileExtensionMap.put("odp", "application/vnd.oasis.opendocument.presentation");
        fileExtensionMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        fileExtensionMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        fileExtensionMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        fileExtensionMap.put("odc", "application/vnd.oasis.opendocument.chart");
        fileExtensionMap.put("odf", "application/vnd.oasis.opendocument.formula");
        fileExtensionMap.put("odb", "application/vnd.oasis.opendocument.database");
        fileExtensionMap.put("odi", "application/vnd.oasis.opendocument.image");
        fileExtensionMap.put("oxt", "application/vnd.openofficeorg.extension");
        // PDF
        fileExtensionMap.put("pdf", "application/pdf");
    }


    private static String getContentTypeByFileName(String fileName)
    {
        String contentType;

        try
        {
            // 1. first use java's buildin utils
            FileNameMap mimeTypes = URLConnection.getFileNameMap();
            contentType = mimeTypes.getContentTypeFor(fileName);

            // 2. nothing found -> lookup our in extension map to find types like ".doc" or ".docx"
            if (StringUtils.isBlank(contentType))
            {
                String extension = FilenameUtils.getExtension(fileName);
                contentType = fileExtensionMap.get(extension);
            }
        }
        catch (Exception e)
        {
            return "application/octet-stream";
        }

        return contentType;
    }



}
