package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "mealsamples")
public class MealSample
{
	@Id
	private String _id;

	private String idworker;

	private LocalDate day;

	private String dish;

	private Boolean orgenolepticoOk;

	private Boolean cuerposExtraOk;

	private String comments;

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

	public Boolean getOrgenolepticoOk() {
		return orgenolepticoOk;
	}

	public void setOrgenolepticoOk(Boolean orgenolepticoOk) {
		this.orgenolepticoOk = orgenolepticoOk;
	}

	public Boolean getCuerposExtraOk() {
		return cuerposExtraOk;
	}

	public void setCuerposExtraOk(Boolean cuerposExtraOk) {
		this.cuerposExtraOk = cuerposExtraOk;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MealSample log = (MealSample) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
