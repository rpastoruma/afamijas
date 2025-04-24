package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "nominas")
public class Nomina
{
	@Id
	private String _id;

	private String idworker;

	private String url;

	private LocalDate duedate;

	private String status;

	private LocalDateTime created;

	public Nomina() { this.created = LocalDateTime.now(); }


	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getIdworker() {
		return idworker;
	}

	public void setIdworker(String idworker) {
		this.idworker = idworker;
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

		Nomina log = (Nomina) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
