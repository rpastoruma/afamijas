package afamijas.model.dto;

import afamijas.model.Permission;
import afamijas.model.User;

public class PermissionDTO
{
    private String idpermission;

    private String idpatient;

    private String patient_fullname;

    private String patient_dni;

    private String idrelative;

    private String relative_fullname;

    private String relative_dni;

    private String type;

    private String comment;

    private String permission_url;

    private String permission_signed_url;


    public PermissionDTO(Permission permission, User relative, User patient)
    {
        this.idpermission = permission.get_id();
        this.idpatient = permission.getIdpatient();
        this.patient_fullname = ((patient.getName() + " " + patient.getSurname1()).trim() + " " + patient.getSurname2()).trim();
        this.patient_dni = patient.getDni();
        this.idrelative = relative.get_id();
        this.relative_fullname = ((relative.getName() + " " + relative.getSurname1()).trim() + " " + relative.getSurname2()).trim();
        this.relative_dni = relative.getDni();
        this.type = permission.getType();
        this.comment = permission.getComment();
        this.permission_url = permission.getPermission_url();
        this.permission_signed_url = permission.getPermission_signed_url();
    }

    public String getIdpermission() {
        return idpermission;
    }

    public void setIdpermission(String idpermission) {
        this.idpermission = idpermission;
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

    public String getPatient_dni() {
        return patient_dni;
    }

    public void setPatient_dni(String patient_dni) {
        this.patient_dni = patient_dni;
    }

    public String getIdrelative() {
        return idrelative;
    }

    public void setIdrelative(String idrelative) {
        this.idrelative = idrelative;
    }

    public String getRelative_fullname() {
        return relative_fullname;
    }

    public void setRelative_fullname(String relative_fullname) {
        this.relative_fullname = relative_fullname;
    }

    public String getRelative_dni() {
        return relative_dni;
    }

    public void setRelative_dni(String relative_dni) {
        this.relative_dni = relative_dni;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPermission_url() {
        return permission_url;
    }

    public void setPermission_url(String permission_url) {
        this.permission_url = permission_url;
    }

    public String getPermission_signed_url() {
        return permission_signed_url;
    }

    public void setPermission_signed_url(String permission_signed_url) {
        this.permission_signed_url = permission_signed_url;
    }
}
