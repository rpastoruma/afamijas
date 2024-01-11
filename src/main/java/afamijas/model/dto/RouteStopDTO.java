package afamijas.model.dto;

import afamijas.model.RouteStop;

public class RouteStopDTO
{

    private String idroutestop;

    private Integer order;

    private String name;

    private String hour;

    private String postaladdress;

    private String idcity;

    private String idstate;

    private String postalcode;

    private Double lat;

    private Double lon;



    public RouteStopDTO(RouteStop routeStop)
    {
        this.idroutestop = routeStop.get_id();
        this.order = routeStop.getOrder();
        this.name = routeStop.getName();
        this.hour = routeStop.getHour();
        this.postaladdress = routeStop.getPostaladdress();
        this.idcity = routeStop.getIdcity();
        this.idstate = routeStop.getIdstate();
        this.postalcode = routeStop.getPostalcode();
        this.lat = routeStop.getLat();
        this.lon = routeStop.getLon();
    }

    public String getIdroutestop() {
        return idroutestop;
    }

    public void setIdroutestop(String idroutestop) {
        this.idroutestop = idroutestop;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
