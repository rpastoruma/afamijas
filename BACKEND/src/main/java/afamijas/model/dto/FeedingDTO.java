package afamijas.model.dto;

import afamijas.model.Feeding;
import afamijas.model.User;

import java.time.LocalDate;

public class FeedingDTO
{

    private String id;

    private String idpatient;

    private String patient_fullname;

    private String idworker;

    private String worker_fullname;

    private LocalDate day;

    private String daymeal; // DESAYUNO, COMIDA

    private String dish;  //PRIMERO, SEGUNDO, POSTRE

    private String result; // COMPLETO, PARCIAL, NADA

    private String indications;

    private String incidences;


    public FeedingDTO(Feeding feeding, User patient, User worker)
    {
        this.id = feeding.get_id();
        this.idpatient = feeding.getIdpatient();
        if(patient!=null) this.patient_fullname = patient.getFullname();
        this.idworker = feeding.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = feeding.getDay();
        this.daymeal = feeding.getDaymeal();
        this.dish = feeding.getDish();
        this.result = feeding.getResult();
        this.indications = feeding.getIndications();
        this.incidences = feeding.getIncidences();
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

    public String getWorker_fullname() {
        return worker_fullname;
    }

    public void setWorker_fullname(String worker_fullname) {
        this.worker_fullname = worker_fullname;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getDaymeal() {
        return daymeal;
    }

    public void setDaymeal(String daymeal) {
        this.daymeal = daymeal;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getIncidences() {
        return incidences;
    }

    public void setIncidences(String incidences) {
        this.incidences = incidences;
    }
}
