package afamijas.model.dto;

import afamijas.model.TempFridge;
import afamijas.model.User;

import java.time.LocalDate;

public class TempFridgeDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private LocalDate day;

    private Double temperature;

    private Boolean isOk;



    public TempFridgeDTO(TempFridge tempFridge, User worker)
    {
        this.id = tempFridge.get_id();
        this.idworker = tempFridge.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = tempFridge.getDay();
        this.temperature = tempFridge.getTemperature();
        this.isOk = tempFridge.getOk();
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

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }
}
