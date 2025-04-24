package afamijas.model.dto;

import afamijas.model.Nomina;
import afamijas.model.Receipt;
import afamijas.model.User;

import java.time.LocalDate;

public class NominaDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private String url;

    private LocalDate duedate;

    private String status;



    public NominaDTO(Nomina nomina, User worker)
    {
        this.id = nomina.get_id();
        this.idworker = nomina.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.url = nomina.getUrl();
        this.duedate = nomina.getDuedate();
        this.status = nomina.getStatus();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getDuedate() {
        return duedate;
    }

    public void setDuedate(LocalDate duedate) {
        this.duedate = duedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
