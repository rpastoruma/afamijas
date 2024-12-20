package afamijas.model.dto;

import afamijas.model.Doc;
import afamijas.model.HealthLog;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DocDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private String title;

    private String description;

    private String url;

    private LocalDate dayfrom;

    private LocalDate dayto;

    private List<String> roles;

    private LocalDateTime created;

    public DocDTO(Doc doc, User worker)
    {
        this.id = doc.get_id();
        this.idworker = doc.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.title = doc.getTitle();
        this.description = doc.getDescription();
        this.url = doc.getUrl();
        this.dayfrom = doc.getDayfrom();
        this.dayto = doc.getDayto();
        this.roles = doc.getRoles();
        this.created = doc.getCreated();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getDayfrom() {
        return dayfrom;
    }

    public void setDayfrom(LocalDate dayfrom) {
        this.dayfrom = dayfrom;
    }

    public LocalDate getDayto() {
        return dayto;
    }

    public void setDayto(LocalDate dayto) {
        this.dayto = dayto;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
