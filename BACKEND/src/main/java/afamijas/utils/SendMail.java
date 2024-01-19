package afamijas.utils;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import afamijas.queuemail.model.QueueEmail;
import afamijas.queuemail.model.QueueEmailAttachment;
import afamijas.queuemail.repositories.QueueEmailAttachmentsRepository;
import afamijas.queuemail.repositories.QueueEmailsRepository;
import afamijas.service.ConfigurationService;
import afamijas.service.LogsService;

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
    final static String BASE64LOGO = "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";
    private ArrayList<SMTPServer> smtps = null;
    private Session session = null;
    private Transport t = null;

    @Value("${spring.profiles.active}")
    private String active_profile;

    @Value("${test.emailto}")
    private String test_emailto;

    final ConfigurationService configurationService;
    final LogsService logsService;
    final QueueEmailsRepository queueEmailsRepository;
    final QueueEmailAttachmentsRepository queueEmailAttachmentsRepository;

    final static int NUM_MAX_LOCAL_RETRIES = 3;

    @Autowired
    public SendMail(ConfigurationService configurationService, LogsService logsService, QueueEmailsRepository queueEmailsRepository, QueueEmailAttachmentsRepository queueEmailAttachmentsRepository)
    {
        this.configurationService = configurationService;
        this.logsService = logsService;
        this.queueEmailsRepository = queueEmailsRepository;
        this.queueEmailAttachmentsRepository = queueEmailAttachmentsRepository;
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
                mimemsg.setFrom(new InternetAddress(from, fromname));
                System.out.println("FROM: <"  + from + ">");

                StringTokenizer stk1 = new StringTokenizer(to, ",");
                StringTokenizer stk2 = new StringTokenizer(toname, ",");
                while(stk1.hasMoreElements())
                {
                    String rcptto = ((String) stk1.nextElement()).trim();
                    System.out.println("TO: <"  + rcptto + ">");
                    String rcptnameto = rcptto;
                    try{ rcptnameto = ( (String) stk2.nextElement()).trim(); } catch(NoSuchElementException e) {}
                    mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(rcptto, rcptnameto));
                }

                if (cc != null) {  System.out.println("cc:<"  + cc + ">"); mimemsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); }
                if (bcc != null) {  System.out.println("bcc:<"  + bcc + ">"); mimemsg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc)); }
                if (replyto != null) {  System.out.println("replyto:<"  + replyto + ">");  mimemsg.setReplyTo(new InternetAddress[]{ new InternetAddress(replyto, replyto)}); }

                // Contenido texto

                mimemsg.setSubject(subject, subjectencoding);
                //if (attachments != null)
                messageBodyPart.setContent(body ,"text/" + bodyformat + "; charset=" + bodyencoding);
                //else
                //mimemsg.setContent(body ,"text/" + bodyformat + "; charset=" + bodyencoding);
                multipart.addBodyPart(messageBodyPart);

                //AÑADIMOS SIEMPRE BASE64LOGO
                byte[] rawImage = Base64.getDecoder().decode(BASE64LOGO);
                BodyPart imagePart = new MimeBodyPart();
                ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage,"image/png");

                imagePart.setDataHandler(new DataHandler(imageDataSource));
                imagePart.setHeader("Content-ID", "AFALogo");
                imagePart.setFileName("AFALogo.png");
                multipart.addBodyPart(imagePart);

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

                    // se quita de aquí y se pone abajo pq ahora siempre hay adjunto (BASE64LOGO)
                    //mimemsg.setContent(multipart);
                }

                mimemsg.setContent(multipart);

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






    @Scheduled(fixedRate = 1000*60*5) // 5 minutes
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

                    this.send(queueEmail.getFrom(), queueEmail.getTo(), queueEmail.getFromname(), queueEmail.getToname(), queueEmail.getCcs(), queueEmail.getBccs(), queueEmail.getReplyto(), queueEmail.getSubject(), queueEmail.getSubjectencoding(), queueEmail.getBody(), queueEmail.getBodyencoding(), listAttachments, StringUtils.replaceString(queueEmail.getMimetype(), "text/", "") );


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
