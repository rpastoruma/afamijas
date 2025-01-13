package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "docs_psico")
public class DocPsico
{
	@Id
	private String _id;

	private String idworker;

	private String idpatient;

	private String type;

	private String description;

	private String url;

	private LocalDateTime created;

	public DocPsico() { this.created = LocalDateTime.now(); }

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

	public String getIdpatient() {
		return idpatient;
	}

	public void setIdpatient(String idpatient) {
		this.idpatient = idpatient;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DocPsico log = (DocPsico) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
