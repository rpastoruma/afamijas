package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "calendardays")
public class CalendarDay
{
	@Id
	private String _id;

	private LocalDate day;

	private Boolean dayoff; //festivo

	private String title;

	private String desciption;

	private String role;

	private String iduser;

	private LocalDateTime created;

	private LocalDateTime modified;


	public CalendarDay() { this.created = this.modified = LocalDateTime.now();  }

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Boolean getDayoff() {
		return dayoff;
	}

	public void setDayoff(Boolean dayoff) {
		this.dayoff = dayoff;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}


	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CalendarDay user = (CalendarDay) o;

		return _id.equals(user._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
