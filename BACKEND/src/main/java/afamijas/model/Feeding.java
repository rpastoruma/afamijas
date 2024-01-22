package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "feeding")
public class Feeding
{
	@Id
	private String _id;

	private String idpatient;

	private String idworker;

	private LocalDate day;

	private String daymeal; // DESAYUNO, COMIDA

	private String dish;  //PRIMERO, SEGUNDO, TERCERO

	private String result; // COMPLETO, PARCIAL, NADA


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

	public String getDaymeal() {
		return daymeal;
	}

	public void setDaymeal(String daymeal) {
		this.daymeal = daymeal;
	}

	public String getDish() {
		return dish;
	}

	public void setDish(String dish) {
		this.dish = dish;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Feeding log = (Feeding) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
