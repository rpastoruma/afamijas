package afamijas.model.dto;

import afamijas.model.User;

public class FoodDTO
{

    private String idpatient;

    private String patient_fullname;

    private String menu_type;

    private String breakfast_description;

    public FoodDTO(User patient)
    {
        this.idpatient = patient.get_id();
        this.patient_fullname = patient.getFullname();
        this.menu_type = patient.getMenu_type();
        this.breakfast_description = patient.getBreakfast_description();
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
}
