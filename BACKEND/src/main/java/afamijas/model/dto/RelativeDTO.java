package afamijas.model.dto;


import afamijas.model.City;
import afamijas.model.Country;
import afamijas.model.State;
import afamijas.model.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class RelativeDTO
{

    @Id
    private String id;

    private String username;

    private String email;

    private String name;

    private String surname1;

    private String surname2;

    private String documentid;

    private String documenttype;

    private String phone;

    private Integer idcity;

    private Integer idstate;

    private Integer idcountry;

    private String cityname;

    private String statename;

    private String countryname;

    private String postalcode;

    private String postaladdress;

    private String fullname;

    public RelativeDTO(User user)
    {
        this.id = user.get_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname1 = user.getSurname1();
        this.surname2 = user.getSurname2();
        this.documentid = user.getDocumentid();
        this.documenttype = user.getDocumenttype();
        this.phone = user.getPhone();
        this.fullname = user.getFullname();
        this.postalcode = user.getPostalcode();
        this.postaladdress = user.getPostaladdress();

    }



    public RelativeDTO(User user, City city, State state, Country country)
    {
        this(user);
        this.idcity = user.getIdcity();
        if (city != null) this.cityname = city.getName();
        this.idstate = user.getIdstate();
        if (state != null) this.statename = state.getName();
        this.idcountry = user.getIdcountry();
        if (country != null) this.countryname = country.getName();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
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

    public Integer getIdcountry() {
        return idcountry;
    }

    public void setIdcountry(Integer idcountry) {
        this.idcountry = idcountry;
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

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getFullname() {
        return fullname;
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
}
