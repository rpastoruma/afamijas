package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "permissions")
public class Permission
{
	@Id
	private String _id;

	private String idpatient;

	private String idrelative;

	private String type;

	private String comment;

	private String permission_url;

	private String permission_signed_url;

	private LocalDateTime created;

	public Permission()
	{
		this.created = LocalDateTime.now();
	}

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

	public String getIdrelative() {
		return idrelative;
	}

	public void setIdrelative(String idrelative) {
		this.idrelative = idrelative;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPermission_url() {
		return permission_url;
	}

	public void setPermission_url(String permission_url) {
		this.permission_url = permission_url;
	}

	public String getPermission_signed_url() {
		return permission_signed_url;
	}

	public void setPermission_signed_url(String permission_signed_url) {
		this.permission_signed_url = permission_signed_url;
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

		Permission user = (Permission) o;

		return _id.equals(user._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
