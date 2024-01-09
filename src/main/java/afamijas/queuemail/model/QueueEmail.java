package afamijas.queuemail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "queuemails")
public class QueueEmail
{
    @Id
    private String _id;

    private String idapp;

    private String idmailing;

    private String from;

    private String fromname;

    private String to;

    private String toname;

    private String ccs;

    private String bccs;

    private String replyto;

    private String subject;

    private String subjectencoding;

    private String body;

    private String bodyencoding;

    private String mimetype;

    private String attachmentstype;

    private List<String> urlattachments;

    private Boolean includetracking;

    private Boolean includeUnregisterLink;

    private LocalDateTime senddate;

    private Integer numretries;

    private LocalDateTime created;

    private LocalDateTime modified;


    public QueueEmail()
    {
        this.numretries = 0;
        this.created = this.modified = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueEmail that = (QueueEmail) o;

        return _id.equals(that._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdapp() {
        return idapp;
    }

    public void setIdapp(String idapp) {
        this.idapp = idapp;
    }

    public String getIdmailing() {
        return idmailing;
    }

    public void setIdmailing(String idmailing) {
        this.idmailing = idmailing;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getCcs() {
        return ccs;
    }

    public void setCcs(String ccs) {
        this.ccs = ccs;
    }

    public String getBccs() {
        return bccs;
    }

    public void setBccs(String bccs) {
        this.bccs = bccs;
    }

    public String getReplyto() {
        return replyto;
    }

    public void setReplyto(String replyto) {
        this.replyto = replyto;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectencoding() {
        return subjectencoding;
    }

    public void setSubjectencoding(String subjectencoding) {
        this.subjectencoding = subjectencoding;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyencoding() {
        return bodyencoding;
    }

    public void setBodyencoding(String bodyencoding) {
        this.bodyencoding = bodyencoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getAttachmentstype() {
        return attachmentstype;
    }

    public void setAttachmentstype(String attachmentstype) {
        this.attachmentstype = attachmentstype;
    }

    public Boolean getIncludetracking() {
        return includetracking;
    }

    public void setIncludetracking(Boolean includetracking) {
        this.includetracking = includetracking;
    }

    public Boolean getIncludeUnregisterLink() {
        return includeUnregisterLink;
    }

    public void setIncludeUnregisterLink(Boolean includeUnregisterLink) {
        this.includeUnregisterLink = includeUnregisterLink;
    }

    public LocalDateTime getSenddate() {
        return senddate;
    }

    public void setSenddate(LocalDateTime senddate) {
        this.senddate = senddate;
    }

    public Integer getNumretries() {
        return numretries;
    }

    public void setNumretries(Integer numretries) {
        this.numretries = numretries;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public List<String> getUrlattachments() {
        return urlattachments;
    }

    public void setUrlattachments(List<String> urlattachments) {
        this.urlattachments = urlattachments;
    }
}
