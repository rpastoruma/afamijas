package afamijas.model.dto;

import afamijas.model.Doc;
import afamijas.model.Receipt;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReceiptDTO
{

    private String id;

    private String idmember;

    private String member_fullname;

    private Double total;

    private String url;

    private LocalDate duedate;

    private LocalDate paiddate;

    private String status;



    public ReceiptDTO(Receipt receipt, User member)
    {
        this.id = receipt.get_id();
        this.idmember = receipt.getIdmember();
        if(member!=null) this.member_fullname = member.getFullname();
        this.total = receipt.getTotal();
        this.url = receipt.getUrl();
        this.duedate = receipt.getDuedate();
        this.paiddate = receipt.getPaiddate();
        this.status = receipt.getStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdmember() {
        return idmember;
    }

    public void setIdmember(String idmember) {
        this.idmember = idmember;
    }

    public String getMember_fullname() {
        return member_fullname;
    }

    public void setMember_fullname(String member_fullname) {
        this.member_fullname = member_fullname;
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
