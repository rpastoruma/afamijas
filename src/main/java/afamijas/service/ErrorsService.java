package afamijas.service;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component
public interface ErrorsService
{
    public void sendError(Exception ex);

    public void sendError(Exception ex, String txt);

    public void sendError(String subject, String txt);

    public void sendWarning(String html);

    public void sendWarning(String subject, String html);
}
