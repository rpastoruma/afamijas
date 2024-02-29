package afamijas.model.dto;

import afamijas.model.MealSample;
import afamijas.model.TempFridge;
import afamijas.model.User;

import java.time.LocalDate;

public class MealSampleDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private LocalDate day;

    private String dish;

    private Boolean orgenolepticoOk;

    private Boolean cuerposExtraOk;

    private String comments;





    public MealSampleDTO(MealSample mealSample, User worker)
    {
        this.id = mealSample.get_id();
        this.idworker = mealSample.getIdworker();
        if(worker!=null) this.worker_fullname = worker.getFullname();
        this.day = mealSample.getDay();
        this.dish = mealSample.getDish();
        this.orgenolepticoOk = mealSample.getOrgenolepticoOk();
        this.cuerposExtraOk = mealSample.getCuerposExtraOk();
        this.comments = mealSample.getComments();
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

    public Boolean getOrgenolepticoOk() {
        return orgenolepticoOk;
    }

    public void setOrgenolepticoOk(Boolean orgenolepticoOk) {
        this.orgenolepticoOk = orgenolepticoOk;
    }

    public Boolean getCuerposExtraOk() {
        return cuerposExtraOk;
    }

    public void setCuerposExtraOk(Boolean cuerposExtraOk) {
        this.cuerposExtraOk = cuerposExtraOk;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
