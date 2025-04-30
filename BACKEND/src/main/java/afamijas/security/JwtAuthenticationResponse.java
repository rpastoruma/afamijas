package afamijas.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationResponse {

    private String token;
    private List<String> roles;
    private String userId;
    private String username;
    private String documentId;
    private String fullname;
    private String photoUrl;

    // Nuevos campos para 2FA
    private boolean requires2FA;
    private String otpAuthUrl; // para mostrar QR si es la primera vez
    private String message;     // mensajes como "Código 2FA requerido"

    private Boolean passworChanged;

    // Constructor para login exitoso
    public JwtAuthenticationResponse(String token, List<String> roles, String userId, String username,
                                     String documentId, String fullname, String photoUrl, Boolean passworChanged) {
        this.token = token;
        this.roles = roles;
        this.userId = userId;
        this.username = username;
        this.documentId = documentId;
        this.fullname = fullname;
        this.photoUrl = photoUrl;
        this.requires2FA = false;
        this.passworChanged = passworChanged;
    }

    // Constructor para respuesta de 2FA (sin token todavía)
    public JwtAuthenticationResponse(boolean requires2FA, String otpAuthUrl, String message) {
        this.requires2FA = requires2FA;
        this.otpAuthUrl = otpAuthUrl;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isRequires2FA() {
        return requires2FA;
    }

    public void setRequires2FA(boolean requires2FA) {
        this.requires2FA = requires2FA;
    }

    public String getOtpAuthUrl() {
        return otpAuthUrl;
    }

    public void setOtpAuthUrl(String otpAuthUrl) {
        this.otpAuthUrl = otpAuthUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getPassworChanged() {
        return passworChanged;
    }

    public void setPassworChanged(Boolean passworChanged) {
        this.passworChanged = passworChanged;
    }
}
