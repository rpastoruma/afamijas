package afamijas.model.dto;

import afamijas.model.Menu;
import afamijas.model.User;

import java.time.LocalDate;

public class MedicationDTO
{

    private String idpatient;

    private String patient_fullname;

    private String medication_description_morning;

    private String medication_description_evening;

    public MedicationDTO(User patient)
    {
        this.idpatient = patient.get_id();
        this.patient_fullname = patient.getFullname();
        this.medication_description_morning = patient.getMedication_description_morning();
        this.medication_description_evening = patient.getMedication_description_evening();
    }

    public String getIdpatient() {
        return idpatient;
    }

    public void setIdpatient(String idpatient) {
        this.idpatient = idpatient;
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

    public String getPatient_fullname() {
        return patient_fullname;
    }

    public void setPatient_fullname(String patient_fullname) {
        this.patient_fullname = patient_fullname;
    }
}
