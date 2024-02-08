package afamijas.model.dto;

import afamijas.model.Route;
import afamijas.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RouteDTO
{

    public String idroute;

    private String route_name;

    private String idpatient;

    private String patient_fullname;

    private List<RouteStopDTO> routestops;

    private String idroutestop_selected_today;

    private String idroutestop_selected_tomorrow;

    private String routestop_especial_from; // day + 00

    private String routestop_especial_to;  // day +  59

    public RouteDTO(Route route, User patient)
    {
        this.idroute = route.get_id();
        this.route_name = route.getName();
        this.idpatient = patient.get_id();
        this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();
        this.routestop_especial_from = this.formatDate(patient.getRoutestop_especial_from());
        this.routestop_especial_to = this.formatDate(patient.getRoutestop_especial_to());
    }

    private String formatDate(LocalDateTime thedate)
    {
        if(thedate==null) return null;
        else return thedate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getIdroute() {
        return idroute;
    }

    public void setIdroute(String idroute) {
        this.idroute = idroute;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
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

    public List<RouteStopDTO> getRoutestops() {
        return routestops;
    }

    public void setRoutestops(List<RouteStopDTO> routestops) {
        this.routestops = routestops;
    }

    public String getIdroutestop_selected_today() {
        return idroutestop_selected_today;
    }

    public void setIdroutestop_selected_today(String idroutestop_selected_today) {
        this.idroutestop_selected_today = idroutestop_selected_today;
    }

    public String getIdroutestop_selected_tomorrow() {
        return idroutestop_selected_tomorrow;
    }

    public void setIdroutestop_selected_tomorrow(String idroutestop_selected_tomorrow) {
        this.idroutestop_selected_tomorrow = idroutestop_selected_tomorrow;
    }

    public String getRoutestop_especial_from() {
        return routestop_especial_from;
    }

    public void setRoutestop_especial_from(String routestop_especial_from) {
        this.routestop_especial_from = routestop_especial_from;
    }

    public String getRoutestop_especial_to() {
        return routestop_especial_to;
    }

    public void setRoutestop_especial_to(String routestop_especial_to) {
        this.routestop_especial_to = routestop_especial_to;
    }
}
