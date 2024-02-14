package afamijas.model.dto;

import afamijas.model.Menu;
import afamijas.model.User;

import java.time.LocalDate;

public class MenuDTO
{

    private String idmenu;

    private String type;

    private String description;

    private String menu_url;

    private LocalDate from;

    private LocalDate to;
    public MenuDTO(Menu menu)
    {
        this.idmenu = menu.get_id();
        this.type = menu.getType();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
