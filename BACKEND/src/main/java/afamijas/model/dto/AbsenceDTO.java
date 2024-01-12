package afamijas.model.dto;

import afamijas.model.Absence;
import afamijas.model.RouteStop;
import afamijas.model.User;

import java.time.LocalDate;

public class AbsenceDTO
{

    private String id;

    private String idpatient;

    private String patient_fullname;

    private String idroutestop;

    private String routestop_name;

    private String comment;

    private LocalDate day;


    public AbsenceDTO(Absence absence, User patient, RouteStop routeStop)
    {
        this.id = absence.get_id();
        this.idpatient = absence.getIdpatient();
        this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();
        this.idroutestop = routeStop.get_id();
        this.routestop_name = routeStop.getName();
        this.comment = absence.getComment();
        this.day = absence.getDay();
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
