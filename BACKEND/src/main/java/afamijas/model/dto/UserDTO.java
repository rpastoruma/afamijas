package afamijas.model.dto;

import afamijas.model.Menu;
import afamijas.model.User;

import java.time.LocalDate;
import java.util.List;

public class UserDTO
{

    private String id;

    private String fullname;

    private List<String> roles;

    private Boolean passwordChanged;

    public UserDTO(User user)
    {
        this.id = user.get_id();
        this.fullname = ((user.getName() + " " + user.getSurname1()).trim() + " " + user.getSurname2()).trim();
        this.roles = user.getRoles();
        this.passwordChanged = user.getPassworChanged();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(Boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }
}
