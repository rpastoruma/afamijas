package afamijas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "users")
public class User 
{
	// COMMON:

	@Id
	private String _id;
	
	private String username;

	@JsonIgnore
	private String password;

	private String role;  // (admin), relative, patient, member

	private String email;

	private String token;

	private String name;

	private String surname1;

	private String surname2;

	private String dni;

	private String phone;

	private String postaladdres;

	private String idcity;

	private String idstate;

	private String postalcode;

	private String signature;

	private String photo_url;

	private String gender;

	private LocalDateTime created;

	private LocalDateTime modified;

	private String status;


	//RELATIVES:

	private String relative_type;

	private Boolean is_principal_keeper;

	private String principal_keeper_fullname;

	private String principal_keeper_phone;


	// PATIENTS:

	private String idrelative;

	private String idroute;

	private String idroutestop;

	private String idroutestop_especial;

	private LocalDateTime routestop_especial_from; // day + 00

	private LocalDateTime routestop_especial_to;  // day +  59

	private String menu_type;

	private String breakfast_description;

	private String medication_description_morning;

	private String medication_description_evening;

	private String groupcode;


	public String getIdRouteStopForDay(LocalDateTime thetime)
	{
		if(this.idroutestop_especial==null) return idroutestop;

		if(routestop_especial_from!=null && routestop_especial_to!=null)
		{
			if(thetime.isAfter(this.routestop_especial_from) && thetime.isBefore(routestop_especial_to))
				return idroutestop_especial;
			else
				return idroutestop;
		}

		return idroutestop;
	}



	// MEMBERS:

	private String fee_euros;

	private String fee_period;  //mes, trimestre...

	private String fee_payment; // pago en sede / domiciliación

	private String bank_name;

	private String bank_account_holder_fullname; // titular

	private String bank_account_holder_dni;

	private String bank_account_iban;


	public User()
	{
		this.created = this.modified = LocalDateTime.now();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname1() {
		return surname1==null?"":surname1;
	}

	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}

	public String getSurname2() {
		return surname2==null?"":surname2;
	}

	public void setSurname2(String surname2) {
		this.surname2 = surname2;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostaladdres() {
		return postaladdres;
	}

	public void setPostaladdres(String postaladdres) {
		this.postaladdres = postaladdres;
	}

	public String getIdcity() {
		return idcity;
	}

	public void setIdcity(String idcity) {
		this.idcity = idcity;
	}

	public String getIdstate() {
		return idstate;
	}

	public void setIdstate(String idstate) {
		this.idstate = idstate;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRelative_type() {
		return relative_type;
	}

	public void setRelative_type(String relative_type) {
		this.relative_type = relative_type;
	}

	public Boolean getIs_principal_keeper() {
		return is_principal_keeper;
	}

	public void setIs_principal_keeper(Boolean is_principal_keeper) {
		this.is_principal_keeper = is_principal_keeper;
	}

	public String getPrincipal_keeper_fullname() {
		return principal_keeper_fullname;
	}

	public void setPrincipal_keeper_fullname(String principal_keeper_fullname) {
		this.principal_keeper_fullname = principal_keeper_fullname;
	}

	public String getPrincipal_keeper_phone() {
		return principal_keeper_phone;
	}

	public void setPrincipal_keeper_phone(String principal_keeper_phone) {
		this.principal_keeper_phone = principal_keeper_phone;
	}

	public String getFee_euros() {
		return fee_euros;
	}

	public void setFee_euros(String fee_euros) {
		this.fee_euros = fee_euros;
	}

	public String getFee_period() {
		return fee_period;
	}

	public void setFee_period(String fee_period) {
		this.fee_period = fee_period;
	}

	public String getFee_payment() {
		return fee_payment;
	}

	public void setFee_payment(String fee_payment) {
		this.fee_payment = fee_payment;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_account_holder_fullname() {
		return bank_account_holder_fullname;
	}

	public void setBank_account_holder_fullname(String bank_account_holder_fullname) {
		this.bank_account_holder_fullname = bank_account_holder_fullname;
	}

	public String getBank_account_holder_dni() {
		return bank_account_holder_dni;
	}

	public void setBank_account_holder_dni(String bank_account_holder_dni) {
		this.bank_account_holder_dni = bank_account_holder_dni;
	}

	public String getBank_account_iban() {
		return bank_account_iban;
	}

	public void setBank_account_iban(String bank_account_iban) {
		this.bank_account_iban = bank_account_iban;
	}

	public String getIdroute() {
		return idroute;
	}

	public void setIdroute(String idroute) {
		this.idroute = idroute;
	}

	public String getIdroutestop() {
		return idroutestop;
	}

	public void setIdroutestop(String idroutestop) {
		this.idroutestop = idroutestop;
	}

	public String getIdroutestop_especial() {
		return idroutestop_especial;
	}

	public void setIdroutestop_especial(String idroutestop_especial) {
		this.idroutestop_especial = idroutestop_especial;
	}

	public LocalDateTime getRoutestop_especial_from() {
		return routestop_especial_from;
	}

	public void setRoutestop_especial_from(LocalDateTime routestop_especial_from) {
		this.routestop_especial_from = routestop_especial_from;
	}

	public LocalDateTime getRoutestop_especial_to() {
		return routestop_especial_to;
	}

	public void setRoutestop_especial_to(LocalDateTime routestop_especial_to) {
		this.routestop_especial_to = routestop_especial_to;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	public String getIdrelative() {
		return idrelative;
	}

	public void setIdrelative(String idrelative) {
		this.idrelative = idrelative;
	}

	public String getMenu_type() {
		return menu_type;
	}

	public void setMenu_type(String menu_type) {
		this.menu_type = menu_type;
	}

	public String getBreakfast_description() {
		return breakfast_description;
	}

	public void setBreakfast_description(String breakfast_description) {
		this.breakfast_description = breakfast_description;
	}

	public String getMedication_description_morning() {
		return medication_description_morning;
	}

	public void setMedication_description_morning(String medication_description_morning) {
		this.medication_description_morning = medication_description_morning;
	}

	public String getMedication_description_evening() {
		return medication_description_evening;
	}

	public void setMedication_description_evening(String medication_description_evening) {
		this.medication_description_evening = medication_description_evening;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	@Transient
	public String getFullname()
	{
		String n = this.name == null?"": this.name;
		String s1 = this.surname1 == null?"": this.surname1;
		String s2 = this.surname2 == null?"": this.surname2;
		String fullname = ((n + " " + s1).trim() + " " + s2).trim();
		return fullname.equals("")?this.username:fullname;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return _id.equals(user._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
