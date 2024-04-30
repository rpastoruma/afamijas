package afamijas.service;

import afamijas.model.Notification;
import afamijas.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Component
public interface NotificationsService
{
    List<Notification> findMyNotifications(User user);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void create(String iduser, List<String> roles, String title, String type, String message, String url);

    void deleteAll(String iduser);

    void delete(String idnotification);
}

