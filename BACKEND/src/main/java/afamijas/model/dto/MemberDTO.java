package afamijas.model.dto;

import afamijas.model.City;
import afamijas.model.State;
import afamijas.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class MemberDTO
{

    private String id;

    private Integer membernumber;

    private String email;

    private String name;

    private String surname1;

    private String surname2;

    private String fullname;

    private String documentid;

    private String documenttype;

    private String phone;

    private String postaladdress;

    private String idcity;

    private String cityname;

    private String idstate;

    private String statename;

    private String postalcode;

    private Double fee_euros;

    private String fee_period;  //mes, trimestre...

    private String fee_payment; // pago en sede / domiciliación

    private String bank_name;

    private String bank_account_holder_fullname; // titular

    private String bank_account_holder_dni;

    private String bank_account_iban;

    private String register_document_url;

    private String unregister_document_url;

    private String unregister_reason;




    public MemberDTO(User user, City city, State state)
    {
        this.id = user.get_id();
        this.membernumber = user.getMembernumber();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname1 = user.getSurname1();
        this.surname2 = user.getSurname2();
        this.fullname = ((user.getName() + " " + user.getSurname1()).trim() + " " + user.getSurname2()).trim();
        this.documentid = user.getDocumentid();
        this.documenttype = user.getDocumenttype();
        this.phone = user.getPhone();
        this.postaladdress = user.getPostaladdress();
        this.idcity = user.getIdcity();
        if(city!=null) this.cityname = city.getName();
        this.idstate = user.getIdstate();
        if(state!=null) this.statename = state.getName();
        this.postalcode = user.getPostalcode();
        this.fee_euros = user.getFee_euros();
        this.fee_period = user.getFee_period();
        this.fee_payment = user.getFee_payment();
        this.bank_name = user.getBank_name();
        this.bank_account_holder_fullname = user.getBank_account_holder_fullname();
        this.bank_account_holder_dni = user.getBank_account_holder_dni();
        this.bank_account_iban = user.getBank_account_iban();
        this.register_document_url = user.getRegister_document_url();
        this.unregister_document_url = user.getUnregister_document_url();
        this.unregister_reason = user.getUnregister_reason();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(String documenttype) {
        this.documenttype = documenttype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getIdcity() {
        return idcity;
    }

    public void setIdcity(String idcity) {
        this.idcity = idcity;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getIdstate() {
        return idstate;
    }

    public void setIdstate(String idstate) {
        this.idstate = idstate;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public Double getFee_euros() {
        return fee_euros;
    }

    public void setFee_euros(Double fee_euros) {
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

    public String getUnregister_reason() {
        return unregister_reason;
    }

    public void setUnregister_reason(String unregister_reason) {
        this.unregister_reason = unregister_reason;
    }

    public String getRegister_document_url() {
        return register_document_url;
    }

    public void setRegister_document_url(String register_document_url) {
        this.register_document_url = register_document_url;
    }

    public String getUnregister_document_url() {
        return unregister_document_url;
    }

    public void setUnregister_document_url(String unregister_document_url) {
        this.unregister_document_url = unregister_document_url;
    }

    public Integer getMembernumber() {
        return membernumber;
    }

    public void setMembernumber(Integer membernumber) {
        this.membernumber = membernumber;
    }
}
