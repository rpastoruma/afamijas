package afamijas.model.dto;

import afamijas.model.RelativeAbsence;
import afamijas.model.RouteStop;
import afamijas.model.User;
import afamijas.model.WorkerAbsence;

import java.time.LocalDateTime;

public class WorkerAbsenceDTO
{

    private String id;

    private String idpatient;

    private String patient_fullname;

    private String idworker;

    private String worker_fullname;

    private String idroutestop;

    private String routestop_name;

    private String comment;

    private LocalDateTime when;


    public WorkerAbsenceDTO(WorkerAbsence workerAbsence, User patient, User worker, RouteStop routeStop)
    {
        this.id = workerAbsence.get_id();

        this.idpatient = workerAbsence.getIdpatient();
        if(patient!=null)  this.patient_fullname =  patient.getFullname();

        this.idworker = workerAbsence.getIdworker();
        if(worker!=null)  this.worker_fullname = worker.getFullname();

        this.idroutestop = workerAbsence.getIdroutestop();
        if(routeStop!=null) this.routestop_name = routeStop.getName();

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

    public String getWorker_fullname() {
        return worker_fullname;
    }

    public void setWorker_fullname(String worker_fullname) {
        this.worker_fullname = worker_fullname;
    }

    public String getRoutestop_name() {
        return routestop_name;
    }

    public void setRoutestop_name(String routestop_name) {
        this.routestop_name = routestop_name;
    }
}
