package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "tempfridge")
public class TempFridge
{
	@Id
	private String _id;

	private String idworker;

	private LocalDate day;

	private Double temperature_fridge;

	private Double temperature_freezer;

	private Boolean isOk;




	private LocalDateTime when;

	public TempFridge() { this.when = LocalDateTime.now(); }

	public LocalDateTime getWhen() {
		return when;
	}

	public void setWhen(LocalDateTime when) {
		this.when = when;
	}


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

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Double getTemperature_fridge() {
		return temperature_fridge;
	}

	public void setTemperature_fridge(Double temperature_fridge) {
		this.temperature_fridge = temperature_fridge;
	}

	public Double getTemperature_freezer() {
		return temperature_freezer;
	}

	public void setTemperature_freezer(Double temperature_freezer) {
		this.temperature_freezer = temperature_freezer;
	}

	public Boolean getOk() {
		return isOk;
	}

	public void setOk(Boolean ok) {
		isOk = ok;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TempFridge log = (TempFridge) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
