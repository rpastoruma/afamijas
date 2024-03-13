package afamijas.model.dto;

import afamijas.model.HealthLog;
import afamijas.model.User;
import afamijas.model.WCLog;

import java.time.LocalDate;

public class HealthLogDTO
{

    private String id;

    private String idpatient;

    private String patient_fullname;

    private String idworker;

    private String worker_fullname;


    private LocalDate day;

    private Double low_pressure;

    private Double high_pressure;

    private String hour_presure;

    private Double sugar;

    private String hour_sugar;

    public HealthLogDTO(HealthLog healthLog, User patient, User worker)
    {
        this.id = healthLog.get_id();
        this.idpatient = healthLog.getIdpatient();
        if(patient!=null) this.patient_fullname = patient.getFullname();
        this.idworker = healthLog.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = healthLog.getDay();
        this.low_pressure = healthLog.getLow_pressure();
        this.high_pressure = healthLog.getHigh_pressure();
        this.hour_presure = healthLog.getHour_presure();
        this.sugar = healthLog.getSugar();
        this.hour_sugar = healthLog.getHour_sugar();
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

    public Double getLow_pressure() {
        return low_pressure;
    }

    public void setLow_pressure(Double low_pressure) {
        this.low_pressure = low_pressure;
    }

    public Double getHigh_pressure() {
        return high_pressure;
    }

    public void setHigh_pressure(Double high_pressure) {
        this.high_pressure = high_pressure;
    }

    public String getHour_presure() {
        return hour_presure;
    }

    public void setHour_presure(String hour_presure) {
        this.hour_presure = hour_presure;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public String getHour_sugar() {
        return hour_sugar;
    }

    public void setHour_sugar(String hour_sugar) {
        this.hour_sugar = hour_sugar;
    }
}
