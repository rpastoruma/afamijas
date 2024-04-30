package afamijas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
public class Notification
{
    @Id
    private String _id;

    private String iduser;

    private String role;

    private String title;

    private String type;

    private String message;

    private String url;

    private LocalDateTime created;


    public Notification(String iduser, String role, String title, String type, String message, String url)
    {
        this();
        this.iduser = iduser;
        this.role = role;
        this.title = title;
        this.type = type;
        this.message = message;
        this.url = url;
    }

    public Notification()
    {
        this.created = LocalDateTime.now();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification question = (Notification) o;

        return _id.equals(question._id);
    }


    @Override
    public int hashCode() {
        return _id.hashCode();
    }

}
