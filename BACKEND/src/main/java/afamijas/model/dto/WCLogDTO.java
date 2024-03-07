package afamijas.model.dto;

import afamijas.model.LegionellaLog;
import afamijas.model.User;
import afamijas.model.WCLog;

import java.time.LocalDate;

public class WCLogDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private LocalDate day;

    private String hour;

    private String point;

    public WCLogDTO(WCLog wcLog, User worker)
    {
        this.id = wcLog.get_id();
        this.idworker = wcLog.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = wcLog.getDay();
        this.hour = wcLog.getHour();
        this.point = wcLog.getPoint();
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
