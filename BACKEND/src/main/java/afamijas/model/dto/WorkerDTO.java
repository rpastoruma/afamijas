package afamijas.model.dto;

import afamijas.model.*;

import java.util.List;

public class WorkerDTO {

    private String id;

    private String email;

    private String name;

    private String surname1;

    private String surname2;

    private String fullname;

    private String documentid;

    private String documenttype;

    private String nss; //trabajador

    private String categoria_profesional; //trabajador

    private String tipo_contrato; //trabajador

    private String jornada_laboral; //trabajador

    private String horario; //trabajador

    private String phone;

    private String postaladdress;

    private Integer idcity;

    private String cityname;

    private Integer idstate;

    private String statename;

    private Integer idcountry;

    private String countryname;


    private String postalcode;

    private List<String> roles;

    private String status;

    private List<CalendarEvent> events;


    public WorkerDTO(User user, City city, State state, Country country, List<CalendarEvent> events) {
        this.id = user.get_id();
        this.roles = user.getRoles();
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
        if (city != null) this.cityname = city.getName();
        this.idstate = user.getIdstate();
        if (state != null) this.statename = state.getName();
        this.idcountry = user.getIdcountry();
        if (country != null) this.countryname = country.getName();
        this.postalcode = user.getPostalcode();

        this.status = user.getStatus();

        this.nss = user.getNss();
        this.categoria_profesional = user.getCategoria_profesional();
        this.tipo_contrato = user.getTipo_contrato();
        this.jornada_laboral = user.getJornada_laboral();
        this.horario = user.getHorario();
        this.events = events;
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

    public Integer getIdcity() {
        return idcity;
    }

    public void setIdcity(Integer idcity) {
        this.idcity = idcity;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public Integer getIdstate() {
        return idstate;
    }

    public void setIdstate(Integer idstate) {
        this.idstate = idstate;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
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

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getCategoria_profesional() {
        return categoria_profesional;
    }

    public void setCategoria_profesional(String categoria_profesional) {
        this.categoria_profesional = categoria_profesional;
    }

    public String getTipo_contrato() {
        return tipo_contrato;
    }

    public void setTipo_contrato(String tipo_contrato) {
        this.tipo_contrato = tipo_contrato;
    }

    public String getJornada_laboral() {
        return jornada_laboral;
    }

    public void setJornada_laboral(String jornada_laboral) {
        this.jornada_laboral = jornada_laboral;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
    }
}

