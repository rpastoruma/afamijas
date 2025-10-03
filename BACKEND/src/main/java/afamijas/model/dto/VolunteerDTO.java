package afamijas.model.dto;

import afamijas.model.City;
import afamijas.model.Country;
import afamijas.model.State;
import afamijas.model.User;

public class VolunteerDTO
{

    private String id;

    private String email;

    private String name;

    private String surname1;

    private String surname2;

    private String fullname;

    private String documentid;

    private String documenttype;

    private String phone;

    private String postaladdress;

    private Integer idcity;

    private String cityname;

    private Integer idstate;

    private String statename;

    private Integer idcountry;

    private String countryname;


    private String postalcode;

    private String register_document_url;

    private String unregister_document_url;

    private String register_document_url_signed;

    private String unregister_document_url_signed;

    private String unregister_reason;

    private String status;



    public VolunteerDTO(User user, City city, State state, Country country)
    {
        this.id = user.get_id();
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
        this.idcountry = user.getIdcountry();
        if(country!=null) this.countryname = country.getName();
        this.postalcode = user.getPostalcode();

        this.register_document_url = user.getRegister_document_url();
        this.unregister_document_url = user.getUnregister_document_url();
        this.register_document_url_signed = user.getRegister_document_url_signed();
        this.unregister_document_url_signed = user.getUnregister_document_url_signed();
        this.unregister_reason = user.getUnregister_reason();
        this.status = user.getStatus();
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

    public Integer getIdcountry() {
        return idcountry;
    }

    public void setIdcountry(Integer idcountry) {
        this.idcountry = idcountry;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
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

    public Integer getIdcity() {
        return idcity;
    }

    public void setIdcity(Integer idcity) {
        this.idcity = idcity;
    }

    public Integer getIdstate() {
        return idstate;
    }

    public void setIdstate(Integer idstate) {
        this.idstate = idstate;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
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

    public String getRegister_document_url_signed() {
        return register_document_url_signed;
    }

    public void setRegister_document_url_signed(String register_document_url_signed) {
        this.register_document_url_signed = register_document_url_signed;
    }

    public String getUnregister_document_url_signed() {
        return unregister_document_url_signed;
    }

    public void setUnregister_document_url_signed(String unregister_document_url_signed) {
        this.unregister_document_url_signed = unregister_document_url_signed;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
