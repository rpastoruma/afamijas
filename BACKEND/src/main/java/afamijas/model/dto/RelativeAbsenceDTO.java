package afamijas.model.dto;

import afamijas.model.RelativeAbsence;
import afamijas.model.RouteStop;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelativeAbsenceDTO
{

    private String id;

    private String idpatient;

    private String idrelative;
    private String patient_fullname;

    private String transport;

    private String comment;

    private Boolean allday;

    private LocalDateTime from;

    private LocalDateTime to;


    public RelativeAbsenceDTO(RelativeAbsence relativeAbsence, User patient)
    {
        this.id = relativeAbsence.get_id();
        this.idpatient = relativeAbsence.getIdpatient();
        if(patient!=null)  this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();

        this.transport = relativeAbsence.getTransport();
        this.comment = relativeAbsence.getComment();

        this.idrelative = relativeAbsence.getIdrelative();
        this.allday = relativeAbsence.getAllday();

        //this.from = relativeAbsence.getFrom()==null?null:"El " + relativeAbsence.getFrom().format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy' a las 'HH:mm")) + " h.";
        //this.to = relativeAbsence.getTo()==null?null:"El " + relativeAbsence.getTo().format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy' a las 'HH:mm")) + " h.";

        this.from = relativeAbsence.getFrom();
        this.to = relativeAbsence.getTo();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdpatient() {
        return idpatient;
    }

    public void setIdpatient(String idpatient) {
        this.idpatient = idpatient;
    }

    public String getPatient_fullname() {
        return patient_fullname;
    }

    public void setPatient_fullname(String patient_fullname) {
        this.patient_fullname = patient_fullname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdrelative() {
        return idrelative;
    }

    public void setIdrelative(String idrelative) {
        this.idrelative = idrelative;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Boolean getAllday() {
        return allday;
    }

    public void setAllday(Boolean allday) {
        this.allday = allday;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
