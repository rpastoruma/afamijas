package afamijas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class Log
{
	@Id
	private String _id;

	private String actiontype;

	private String objecttype;

	private String idobject;

	private String iduser;

	private LocalDateTime actiondate;

	private String comments;

	private String ip;

	public Log()
	{
		this.actiontype = this.objecttype = "";
		this.actiondate = LocalDateTime.now();
	}

	public Log(String actiontype, String objecttype, String idobject, String iduser, LocalDateTime actiondate, String comments, String ip)
	{
		this.actiontype = actiontype;
		this.objecttype = objecttype;
		this.idobject = idobject;
		this.iduser = iduser;
		this.actiondate = actiondate;
		this.comments = comments;
		this.ip = ip;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getObjecttype() {
		return objecttype;
	}

	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}

	public String getIdobject() {
		return idobject;
	}

	public void setIdobject(String idobject) {
		this.idobject = idobject;
	}

	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public LocalDateTime getActiondate() {
		return actiondate;
	}

	public void setActiondate(LocalDateTime actiondate) {
		this.actiondate = actiondate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Log log = (Log) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
