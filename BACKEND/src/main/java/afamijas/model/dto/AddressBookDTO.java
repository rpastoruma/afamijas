package afamijas.model.dto;

import afamijas.model.AddressBook;
import afamijas.model.Atencion;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddressBookDTO
{

    private String id;

    private String type; //relative, user, volunteer, worker, member, other

    private String iduser;

    private String fullname;

    private String phone;

    private String email;

    private String observations;

    public AddressBookDTO(AddressBook addressBook)
    {
        this.id = addressBook.get_id();
        this.type = addressBook.getType();
        this.iduser = addressBook.getIduser();
        this.fullname = addressBook.getFullname();
        this.phone = addressBook.getPhone();
        this.email = addressBook.getEmail();
        this.observations = addressBook.getObservations();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
