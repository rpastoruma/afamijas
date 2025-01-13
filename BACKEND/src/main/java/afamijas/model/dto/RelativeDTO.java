package afamijas.model.dto;


import afamijas.model.City;
import afamijas.model.Country;
import afamijas.model.State;
import afamijas.model.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class RelativeDTO
{

    @Id
    private String id;

    private String username;

    private String email;

    private String name;

    private String surname1;

    private String surname2;

    private String documentid;


    public RelativeDTO(User user)
    {
        this.id = user.get_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname1 = user.getSurname1();
        this.surname2 = user.getSurname2();
        this.documentid = user.getDocumentid();
    }


    @Transient
    public String getFullname()
    {
        String n = this.name == null?"": this.name;
        String s1 = this.surname1 == null?"": this.surname1;
        String s2 = this.surname2 == null?"": this.surname2;
        String fullname = ((n + " " + s1).trim() + " " + s2).trim();
        return fullname.equals("")?this.username:fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }
}
