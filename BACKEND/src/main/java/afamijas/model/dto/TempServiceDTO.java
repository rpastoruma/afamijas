package afamijas.model.dto;

import afamijas.model.TempFridge;
import afamijas.model.TempService;
import afamijas.model.User;

import java.time.LocalDate;

public class TempServiceDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private LocalDate day;

    private String dish;

    private String dish_type; // FRÍO, CALIENTE

    private Double temperature_reception;

    private Double temperature_service;

    private Boolean isOk;




    public TempServiceDTO(TempService tempService, User worker)
    {
        this.id = tempService.get_id();
        this.idworker = tempService.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = tempService.getDay();
        this.dish = tempService.getDish();
        this.dish_type = tempService.getDish_type();
        this.temperature_reception = tempService.getTemperature_reception();
        this.temperature_service = tempService.getTemperature_service();
        this.isOk = tempService.getOk();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdworker() {
        return idworker;
    }

    public void setIdworker(String idworker) {
        this.idworker = idworker;
    }

    public String getWorker_fullname() {
        return worker_fullname;
    }

    public void setWorker_fullname(String worker_fullname) {
        this.worker_fullname = worker_fullname;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getDish_type() {
        return dish_type;
    }

    public void setDish_type(String dish_type) {
        this.dish_type = dish_type;
    }

    public Double getTemperature_reception() {
        return temperature_reception;
    }

    public void setTemperature_reception(Double temperature_reception) {
        this.temperature_reception = temperature_reception;
    }

    public Double getTemperature_service() {
        return temperature_service;
    }

    public void setTemperature_service(Double temperature_service) {
        this.temperature_service = temperature_service;
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }
}
