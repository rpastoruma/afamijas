package afamijas.model.dto;

import afamijas.model.RelativeAbsence;
import afamijas.model.User;
import afamijas.model.WorkerAbsence;

import java.time.LocalDateTime;

public class WorkerAbsenceDTO
{

    private String id;

    private String idpatient;

    private String patient_fullname;

    private String idworker;

    private String idroutestop;

    private String comment;


    private LocalDateTime when;



    public WorkerAbsenceDTO(WorkerAbsence workerAbsence, User patient)
    {
        this.id = workerAbsence.get_id();
        this.idpatient = workerAbsence.getIdpatient();
        if(patient!=null)  this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();

        this.idworker = workerAbsence.getIdworker();

        this.idroutestop = workerAbsence.getIdroutestop();

        this.comment = workerAbsence.getComment();

        this.when = workerAbsence.getWhen();
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

    public String getIdworker() {
        return idworker;
    }

    public void setIdworker(String idworker) {
        this.idworker = idworker;
    }

    public String getIdroutestop() {
        return idroutestop;
    }

    public void setIdroutestop(String idroutestop) {
        this.idroutestop = idroutestop;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }
}
