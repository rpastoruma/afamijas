package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "healthlog")
public class HealthLog
{
	@Id
	private String _id;

	private String idpatient;

	private String idworker;

	private LocalDate day;

	private Double low_pressure;

	private Double high_pressure;

	private String hour_presure;

	private Double sugar;

	private String hour_sugar;

	private LocalDateTime created;

	public HealthLog() { this.created = LocalDateTime.now(); }


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

	public Double getLow_pressure() {
		return low_pressure;
	}

	public void setLow_pressure(Double low_pressure) {
		this.low_pressure = low_pressure;
	}

	public Double getHigh_pressure() {
		return high_pressure;
	}

	public void setHigh_pressure(Double high_pressure) {
		this.high_pressure = high_pressure;
	}

	public String getHour_presure() {
		return hour_presure;
	}

	public void setHour_presure(String hour_presure) {
		this.hour_presure = hour_presure;
	}

	public Double getSugar() {
		return sugar;
	}

	public void setSugar(Double sugar) {
		this.sugar = sugar;
	}

	public String getHour_sugar() {
		return hour_sugar;
	}

	public void setHour_sugar(String hour_sugar) {
		this.hour_sugar = hour_sugar;
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

		HealthLog log = (HealthLog) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
