package afamijas.model.dto;

import afamijas.model.Absence;
import afamijas.model.RouteStop;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AbsenceDTO
{

    private String id;

    private String idpatient;

    private String idrelative;

    private String idworker;

    private String patient_fullname;

    private String idroutestop;

    private String routestop_name;

    private String comment;

    private LocalDate day;

    private Boolean allday;

    private LocalDateTime from;

    private LocalDateTime to;

    public AbsenceDTO(Absence absence, User patient, RouteStop routeStop)
    {
        this.id = absence.get_id();
        this.idpatient = absence.getIdpatient();
        if(patient!=null)  this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();
        if(routeStop!=null) this.idroutestop = routeStop.get_id();
        if(routeStop!=null) this.routestop_name = routeStop.getName();
        this.comment = absence.getComment();
        this.day = absence.getDay();

        this.idrelative = absence.getIdrelative();
        this.idworker = absence.getIdworker();
        this.allday = absence.getAllday();
        this.from = absence.getFrom();
        this.to = absence.getTo();
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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }


    public String getIdroutestop() {
        return idroutestop;
    }

    public void setIdroutestop(String idroutestop) {
        this.idroutestop = idroutestop;
    }

    public String getRoutestop_name() {
        return routestop_name;
    }

    public void setRoutestop_name(String routestop_name) {
        this.routestop_name = routestop_name;
    }
}
