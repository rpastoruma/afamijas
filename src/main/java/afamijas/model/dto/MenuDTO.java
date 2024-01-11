package afamijas.model.dto;

import afamijas.model.Menu;
import afamijas.model.User;

import java.time.LocalDate;

public class MenuDTO
{

    private String idmenu;

    private String idpatient;

    private String patient_fullname;

    private String name;

    private String description;

    private String menu_url;

    private LocalDate from;

    private LocalDate to;
    public MenuDTO(Menu menu, User patient)
    {
        this.idmenu = menu.get_id();
        this.idpatient = menu.getIdpatient();
        this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();
        this.name = menu.getName();
        this.description = menu.getDescription();
        this.menu_url = menu.getMenu_url();
        this.from = menu.getFrom();
        this.to = menu.getTo();
    }

    public String getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(String idmenu) {
        this.idmenu = idmenu;
    }

    public String getIdpatient() {
        return idpatient;
    }

    public void setIdpatient(String idpatient) {
        this.idpatient = idpatient;
    }

    public String getPatient_fullname() {
        return patient_fullname;
    }

    public void setPatient_fullname(String patient_fullname) {
        this.patient_fullname = patient_fullname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenu_url() {
        return menu_url;
    }

    public void setMenu_url(String menu_url) {
        this.menu_url = menu_url;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
