package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "addressbook")
public class AddressBook
{
	@Id
	private String _id;

	private String type; //relative, user, volunteer, worker, member, other

	private String iduser;

	private String fullname;

	private String phone;

	private String email;

	private String observations;

	private LocalDateTime created;

	public AddressBook() { this.created = LocalDateTime.now(); }

	public AddressBook(User user)
	{
		List all_worker_roles = Arrays.asList(Roles.WORKER_ROLES);

		if(user.getRoles()!=null && user.getRoles().size()>0)
			for(String role : user.getRoles())
			{
				if(all_worker_roles.contains(role))
				{
					this.type = "WORKER";
					break;
				}
				else
				{
					this.type = role;
					break;
				}
			}
		else
			this.type = "OTHER";

		this.iduser = user.get_id();
		this.fullname = user.getFullname();
		this.phone = user.getPhone();
		this.email = user.getEmail();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

		AddressBook log = (AddressBook) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
