package afamijas.model.dto;

import afamijas.model.Atencion;
import afamijas.model.Invoice;
import afamijas.model.User;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AtencionDTO
{

    private String id;

    private String idworker;

    private String workerfullname;

    private String number;

    private LocalDate datedone;

    private String clientfullname;

    private String sex;

    private String nationality;

    private String relationship;

    private String why;

    private String via;

    private String professional;

    private String observations;

    private LocalDateTime created;



    public AtencionDTO(Atencion atencion, User worker)
    {
        this.id = atencion.get_id();
        this.idworker = worker.get_id();
        this.workerfullname = worker.getFullname();
        this.number = atencion.getNumber();
        this.datedone = atencion.getDatedone();
        this.clientfullname = atencion.getClientfullname();
        this.sex = atencion.getSex();
        this.nationality = atencion.getNationality();
        this.relationship = atencion.getRelationship();
        this.why = atencion.getWhy();
        this.via = atencion.getVia();
        this.professional = atencion.getProfessional();
        this.observations = atencion.getObservations();
        this.created = atencion.getCreated();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdworker() {
        return idworker;
    }

    public void setIdworker(String idworker) {
        this.idworker = idworker;
    }

    public String getWorkerfullname() {
        return workerfullname;
    }

    public void setWorkerfullname(String workerfullname) {
        this.workerfullname = workerfullname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDatedone() {
        return datedone;
    }

    public void setDatedone(LocalDate datedone) {
        this.datedone = datedone;
    }

    public String getClientfullname() {
        return clientfullname;
    }

    public void setClientfullname(String clientfullname) {
        this.clientfullname = clientfullname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
