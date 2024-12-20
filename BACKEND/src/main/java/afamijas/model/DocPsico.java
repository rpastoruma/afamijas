package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "docs")
public class Doc
{
	@Id
	private String _id;

	private String idworker;

	private String title;

	private String description;

	private String url;

	private LocalDate dayfrom;

	private LocalDate dayto;

	private List<String> roles;

	private LocalDateTime created;

	public Doc() { this.created = LocalDateTime.now(); }

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Doc log = (Doc) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
