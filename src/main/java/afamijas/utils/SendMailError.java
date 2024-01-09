package afamijas.utils;


import afamijas.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Configuration
public class SendMailError
{
    private ArrayList<SMTPServer> smtps = null;
    private Session session = null;
    private Transport t = null;

    @Value("${spring.profiles.active}")
    private String active_profile;

    @Value("${test.emailto}")
    private String test_emailto;

    final ConfigurationService configurationService;
    final Environment env;

    @Autowired
    public SendMailError(ConfigurationService configurationService, Environment env)
    {
        this.configurationService = configurationService;
        this.env = env;
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
                System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - "  + "SENDMAILERROR: Enviando con SMTP: [" + smtps.get(i).getHost() + "]");

                if(smtps.get(i).getStarttls())
                {
                    System.out.println("\tSTARTTLS ON");
                    Properties props = System.getProperties();
                    props.put("mail.smtp.starttls.enable", true); // added this line
                    props.put("mail.smtp.host", smtps.get(i).getHost());
                    props.put("mail.smtp.user", smtps.get(i).getUser());
                    props.put("mail.smtp.password", smtps.get(i).getPass());
                    props.put("mail.smtp.port", smtps.get(i).getPort());
                    props.put("mail.smtp.auth", smtps.get(i).getAuth());
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

                StringTokenizer stk1 = new StringTokenizer(to, ",");
                StringTokenizer stk2 = new StringTokenizer(toname, ",");
                while(stk1.hasMoreElements())
                {
                    String rcptto = ((String) stk1.nextElement()).trim();
                    String rcptnameto = rcptto;
                    try{ rcptnameto = ( (String) stk2.nextElement()).trim(); } catch(NoSuchElementException e) {}
                    mimemsg.addRecipient(Message.RecipientType.TO, new InternetAddress(rcptto, rcptnameto));
                }

                if (cc != null) mimemsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
                if (bcc != null) mimemsg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
                if (replyto != null) mimemsg.setReplyTo(new InternetAddress[]{ new InternetAddress(replyto, replyto)});

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
                    try { transport.sendMessage(mimemsg, mimemsg.getAllRecipients()); } catch(Exception e) { session = null; t=null;  continue; }
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
                    }
                    catch(Exception e) { session = null; t=null;  continue; }
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

            int i=1; //principal desde bd
            while(!this.configurationService.value("mail.smtp" + i + ".host").equals(""))
            {
                smtps.add( new SMTPServer(
                        this.configurationService.value("errors.mail.smtp" + i + ".host").trim(),
                        Integer.parseInt(this.configurationService.value("errors.mail.smtp" + i + ".port").trim()),
                        Boolean.parseBoolean(this.configurationService.value("errors.mail.smtp" + i + ".authenticate").trim()),
                        this.configurationService.value("errors.mail.smtp" + i + ".user").trim(),
                        this.configurationService.value("errors.mail.smtp" + i + ".pass").trim(),
                        Boolean.parseBoolean(this.configurationService.value("errors.mail.smtp" + i + ".starttls").trim())
                ) );
                i++;
            }

            i=2; //siempre auxiliar desde properties
            while(!(env.getProperty("mail.smtp" + i + ".host")==null))
            {
                smtps.add( new SMTPServer(
                        env.getProperty("mail.smtp" + i + ".host").trim(),
                        Integer.parseInt(env.getProperty("mail.smtp" + i + ".port").trim()),
                        Boolean.parseBoolean(env.getProperty("mail.smtp" + i + ".authenticate").trim()),
                        env.getProperty("mail.smtp" + i + ".user").trim(),
                        env.getProperty("mail.smtp" + i + ".pass").trim(),
                        Boolean.parseBoolean(env.getProperty("mail.smtp" + i + ".starttls").trim())
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










}
