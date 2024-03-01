package afamijas.model.dto;

import afamijas.model.LegionellaLog;
import afamijas.model.TempService;
import afamijas.model.User;

import java.time.LocalDate;

public class LegionellaLogDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private LocalDate day;

    private Double value;

    private Double temperature;

    private String point;

    private Boolean isOk;

    public LegionellaLogDTO(LegionellaLog legionellaLog, User worker)
    {
        this.id = legionellaLog.get_id();
        this.idworker = legionellaLog.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = legionellaLog.getDay();
        this.value = legionellaLog.getValue();
        this.temperature = legionellaLog.getTemperature();
        this.point = legionellaLog.getPoint();
        this.isOk = legionellaLog.getOk();
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }
}
