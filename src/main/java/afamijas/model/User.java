package afamijas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
public class User 
{
	@Id
	private String _id;
	
	private String username;

	@JsonIgnore
	private String password;

	private String role;

	private String email;

	private String token;

	//RELATIVES:
	private String name;

	private String surname1;

	private String surname2;

	private String dni;

	private String phone;

	private String postaladdres;

	private String idcity;

	private String idstate;

	private String postalcode;

	private String fee_euros;

	private String fee_period;  //mes, trimestre...

	private String fee_payment; // pago en sede / domiciliación

	private String bank_name;

	private String bank_account_holder_fullname; // titular

	private String bank_account_holder_dni;

	private String bank_account_iban;

	private String relative_signature;

	private LocalDateTime created;  // fecha de alta

	private LocalDateTime modified;

	private String status;
	
	
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

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname1() {
		return surname1;
	}

	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}

	public String getSurname2() {
		return surname2;
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

	public String getRelative_signature() {
		return relative_signature;
	}

	public void setRelative_signature(String relative_signature) {
		this.relative_signature = relative_signature;
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
