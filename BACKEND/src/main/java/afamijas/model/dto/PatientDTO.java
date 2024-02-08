package afamijas.model.dto;


import afamijas.model.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class PatientDTO
{


    @Id
    private String id;

    private String username;

    private String email;

    private String name;

    private String surname1;

    private String surname2;

    private String dni;

    private String phone;

    private String postaladdres;

    private String idcity;

    private String cityname;

    private String idstate;

    private String statename;

    private String postalcode;

    private String signature;

    private String photo_url;

    private String gender;



    private String idrelative;

    private String relativefullname;

    private Boolean is_principal_keeper;

    private String principal_keeper_fullname;

    private String principal_keeper_phone;



    private RouteDTO routeDTO;



    private String menu_type;

    private String breakfast_description;

    private String medication_description_morning;

    private String medication_description_evening;

    private String groupcode;


    public PatientDTO(User user, City city, State state, User relative, RouteDTO routeDTO)
    {
        this.id = user.get_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname1 = user.getSurname1();
        this.surname2 = user.getSurname2();
        this.dni = user.getDni();
        this.phone = user.getPhone();
        this.postaladdres = user.getPostaladdres();

        this.idcity  = user.getIdcity();
        if(city!=null) this.cityname = city.getName();

        this.idstate = user.getIdstate();
        if(state!=null) this.statename = state.getName();

        this.postalcode = user.getPostalcode();

        this.signature = user.getSignature();
        this.photo_url = user.getPhoto_url();
        this.gender = user.getGender();

        this.idrelative = user.getIdrelative();
        if(relative!=null)
        {
            this.relativefullname = relative.getFullname();
            if(!relative.getIs_principal_keeper())
            {
                this.principal_keeper_fullname = relative.getPrincipal_keeper_fullname();
                this.principal_keeper_phone = relative.getPrincipal_keeper_phone();
            }
        }

        this.routeDTO = routeDTO;

        this.menu_type = user.getMenu_type();
        this.breakfast_description = user.getBreakfast_description();
        this.medication_description_morning = user.getMedication_description_morning();
        this.medication_description_evening = user.getMedication_description_evening();
        this.groupcode = user.getGroupcode();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupcode() {
        return groupcode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
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

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdrelative() {
        return idrelative;
    }

    public void setIdrelative(String idrelative) {
        this.idrelative = idrelative;
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

    public String getRelativefullname() {
        return relativefullname;
    }

    public void setRelativefullname(String relativefullname) {
        this.relativefullname = relativefullname;
    }

    public RouteDTO getRouteDTO() {
        return routeDTO;
    }

    public void setRouteDTO(RouteDTO routeDTO) {
        this.routeDTO = routeDTO;
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
}
