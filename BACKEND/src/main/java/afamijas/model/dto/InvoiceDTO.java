package afamijas.model.dto;

import afamijas.model.Invoice;
import afamijas.model.Receipt;
import afamijas.model.User;

import java.time.LocalDate;

public class InvoiceDTO
{

    private String id;

    private String idpatient;

    private String patient_fullname;

    private Double total;

    private String url;

    private LocalDate duedate;

    private LocalDate paiddate;

    private String status;



    public InvoiceDTO(Invoice invoice, User patient)
    {
        this.id = invoice.get_id();
        this.idpatient = invoice.getIdpatient();
        if(patient!=null) this.patient_fullname = patient.getFullname();
        this.total = invoice.getTotal();
        this.url = invoice.getUrl();
        this.duedate = invoice.getDuedate();
        this.paiddate = invoice.getPaiddate();
        this.status = invoice.getStatus();
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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

    public LocalDate getPaiddate() {
        return paiddate;
    }

    public void setPaiddate(LocalDate paiddate) {
        this.paiddate = paiddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
