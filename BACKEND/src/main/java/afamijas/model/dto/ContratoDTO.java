package afamijas.model.dto;

import afamijas.model.Contrato;
import afamijas.model.User;

import java.time.LocalDate;

public class ContratoDTO
{
    private String id;

    private String idworker;

    private String worker_fullname;

    private String url;

    private LocalDate startdate;

    private LocalDate enddate;

    private String status;

    public ContratoDTO(Contrato contrato, User worker)
    {
        this.id = contrato.get_id();
        this.idworker = contrato.getIdworker();

        if(worker != null)
            this.worker_fullname = worker.getFullname();

        this.url = contrato.getUrl();
        this.startdate = contrato.getStartdate();
        this.enddate = contrato.getEnddate();
        this.status = contrato.getStatus();
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

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}