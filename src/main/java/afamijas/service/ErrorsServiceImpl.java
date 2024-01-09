package afamijas.service;

import afamijas.utils.FileUtils;
import afamijas.utils.SendMail;
import afamijas.utils.SendMailError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ErrorsServiceImpl implements ErrorsService
{
    @Value("${spring.profiles.active}")
    private String active_profile;

    @Value("${media.path}")
    private String mediapath;

    @Value("${main.name}")
    private String mainname;

    @Value("${errors.fromemail}")
    private String errorsfromemail;

    @Value("${warnings.fromemail}")
    private String warningsfromemail;


    final ConfigurationService configurationService;

    final SendMail sendMail;

    final SendMailError sendMailError;

    @Autowired
    public ErrorsServiceImpl(ConfigurationService configurationService, SendMail sendMail, SendMailError sendMailError)
    {
        this.configurationService = configurationService;
        this.sendMail = sendMail;
        this.sendMailError = sendMailError;
    }

    @Override
    public void sendError(String subject, String txt)
    {
        if(this.configurationService.value("errors.sendemail").equals("true"))
        {

            if(!active_profile.equals("pre") && !active_profile.equals("prod"))
            {
                System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - " + subject + "\n\n" + txt);
                return;
            }

            try
            {
                String emailto = this.configurationService.value("errors.emailto");
                this.sendMailError.send(errorsfromemail, emailto, mainname + " - " + active_profile, emailto, null, null, null, subject , "UTF-8", txt, "UTF-8", null, "plain");
            }
            catch(Exception e)
            {
                try
                {
                    FileUtils.string2File("\n"  + subject + "\n\n" + txt + "\n\n",  mediapath + File.separator + "not-sent-errors.log", "UTF-8", true);
                }
                catch(Exception ee)
                {
                    System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - "   + ":\n " + subject + "\n\n" + txt + "\n\n");
                }
            }
        }
    }


    @Override
    public void sendError(Exception ex, String txt)
    {
        try
        {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));

            if(this.configurationService.value("errors.sendemail").equals("true"))
            {

                if(!active_profile.equals("pre") && !active_profile.equals("prod"))
                {
                    System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - " + errors.toString() + "\n\n" + txt);
                    return;
                }

                try
                {
                    String emailto = this.configurationService.value("errors.emailto");
                    this.sendMailError.send(errorsfromemail, emailto, mainname + " - " + active_profile, emailto, null, null, null, "Error" , "UTF-8", errors.toString() + "\n\n" + txt, "UTF-8", null, "plain");
                }
                catch(Exception e)
                {
                    try
                    {
                        FileUtils.string2File("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - "   + ":\n " + errors.toString() + "\n\n" + txt + "\n\n",  mediapath + File.separator + "not-sent-errors.log", "UTF-8", true);
                    }
                    catch(Exception ee)
                    {
                        System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - "   + ":\n " + errors.toString() + "\n\n" + txt + "\n\n");
                    }
                }
            }
            else
            {
                System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - " + errors.toString() + "\n\n" + txt);
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void sendError(Exception ex)
    {
        this.sendError(ex, "");
    }


    @Override
    public void sendWarning(String subject, String html)
    {
        if(this.configurationService.value("errors.sendemail").equals("true"))
        {
            try
            {
                String emailto = this.configurationService.value("errors.emailto");
                sendMailError.send(warningsfromemail, emailto, mainname + " - " + active_profile, emailto, null, null, null, subject , "UTF-8", html, "UTF-8", null, "html");
            }
            catch(Exception e)
            {
                try
                {
                    FileUtils.string2File("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - "   + ":\n " + html + "\n\n", mediapath + File.separator + "not-sent-warnings.log", "UTF-8", true);
                }
                catch(Exception ee)
                {
                    System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " - "   + ":\n " + html + "\n\n");
                }
            }
        }
    }

    @Override
    public void sendWarning(String html)
    {
        this.sendWarning("Warning", html);
    }


}