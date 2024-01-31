package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "calendarevents")
public class CalendarEvent
{
	@Id
	private String _id;

	private LocalDateTime start;

	private LocalDateTime end;

	private Boolean allDay;

	private String title;

	private Boolean dayoff; //festivo

	private String description;

	private List<String> roles;

	private List<String> idsusers;

	private LocalDateTime publishdate;

	private LocalDateTime created;

	private LocalDateTime modified;


	public CalendarEvent() { this.created = this.modified = LocalDateTime.now();  }

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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


	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getIdsusers() {
		return idsusers;
	}

	public void setIdsusers(List<String> idsusers) {
		this.idsusers = idsusers;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public Boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

	public LocalDateTime getPublishdate() {
		return publishdate;
	}

	public void setPublishdate(LocalDateTime publishdate) {
		this.publishdate = publishdate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CalendarEvent user = (CalendarEvent) o;

		return _id.equals(user._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
