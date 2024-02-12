package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "relative_absences")
public class RelativeAbsence
{
	@Id
	private String _id;

	private String idpatient;

	private String idrelative;

	private String transport; // SOLO IDA, SOLO VUELTA, IDA Y VUELTA, NO

	private String comment;

	private LocalDateTime from;

	private LocalDateTime to;

	private Boolean allday;

	private LocalDateTime created;

	public RelativeAbsence()
	{
		this.created = LocalDateTime.now();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getIdpatient() {
		return idpatient;
	}

	public void setIdpatient(String idpatient) {
		this.idpatient = idpatient;
	}

	public String getIdrelative() {
		return idrelative;
	}

	public void setIdrelative(String idrelative) {
		this.idrelative = idrelative;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getFrom() {
		return from;
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public LocalDateTime getTo() {
		return to;
	}

	public void setTo(LocalDateTime to) {
		this.to = to;
	}

	public Boolean getAllday() {
		return allday;
	}

	public void setAllday(Boolean allday) {
		this.allday = allday;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RelativeAbsence user = (RelativeAbsence) o;

		return _id.equals(user._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
