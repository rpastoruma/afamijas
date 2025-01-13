package afamijas.model.dto;

import afamijas.model.Doc;
import afamijas.model.DocPsico;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DocPsicoDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private String idpatient;

    private String patient_fullname;

    private String type;

    private String description;

    private String url;

    private LocalDateTime created;

    public DocPsicoDTO(DocPsico doc, User worker, User patient)
    {
        this.id = doc.get_id();
        this.idworker = doc.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.idpatient = doc.getIdpatient();
        if(patient!=null) this.patient_fullname = patient.getFullname();
        this.type = doc.getType();
        this.description = doc.getDescription();
        this.url = doc.getUrl();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
