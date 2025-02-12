package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "atenciones")
public class Atencion
{
	@Id
	private String _id;

	private String idworker;

	private String number;

	private LocalDate datedone;

	private String clientfullname;

	private String sex;

	private String nationality;

	private String relationship;

	private String why;

	private String via;

	private String professional;

	private String observations;

	private LocalDateTime created;

	public Atencion() { this.created = LocalDateTime.now(); }

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public LocalDate getDatedone() {
		return datedone;
	}

	public void setDatedone(LocalDate datedone) {
		this.datedone = datedone;
	}

	public String getClientfullname() {
		return clientfullname;
	}

	public void setClientfullname(String clientfullname) {
		this.clientfullname = clientfullname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getWhy() {
		return why;
	}

	public void setWhy(String why) {
		this.why = why;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
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

		Atencion log = (Atencion) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
