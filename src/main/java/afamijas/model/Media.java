package afamijas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "media")
public class Media
{
    @Id
    private String _id;

    private String idobject;  // idsending para attachments y cualquier otro id para un media genérico de la aplicación

    private String objecttype;   // Sending para attachments

    private String mediatype; // Attachment

    private String filename; // si se ha subido como URL será null

    private String url;  // url final del media independientemente sea subido por URL o fichero

    private String mimetype;  // aportado en la subida o si no calculado automáticamente

    private LocalDateTime created;

    private LocalDateTime modified;

    private String status;


    public Media()
    {
        this.status = "A";
        this.created = LocalDateTime.now();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdobject() {
        return idobject;
    }

    public void setIdobject(String idobject) {
        this.idobject = idobject;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Media x = (Media) o;

        return _id.equals(x._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
