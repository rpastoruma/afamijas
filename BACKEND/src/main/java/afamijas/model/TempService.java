package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "tempservices")
public class TempService
{
	@Id
	private String _id;

	private String idworker;

	private LocalDate day;

	private String dish;

	private String dish_type; // COLD, HOT

	private Double temperature_reception;

	private Double temperature_service;

	private Boolean isOk;

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

	public String getDish() {
		return dish;
	}

	public void setDish(String dish) {
		this.dish = dish;
	}

	public Double getTemperature_reception() {
		return temperature_reception;
	}

	public void setTemperature_reception(Double temperature_reception) {
		this.temperature_reception = temperature_reception;
	}

	public Double getTemperature_service() {
		return temperature_service;
	}

	public void setTemperature_service(Double temperature_service) {
		this.temperature_service = temperature_service;
	}

	public Boolean getOk() {
		return isOk;
	}

	public void setOk(Boolean ok) {
		isOk = ok;
	}

	public String getDish_type() {
		return dish_type;
	}

	public void setDish_type(String dish_type) {
		this.dish_type = dish_type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TempService log = (TempService) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
