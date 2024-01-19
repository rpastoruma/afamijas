package afamijas.security;


import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable
{

    private static final long serialVersionUID = 1250165508152483573L;

    private final String token;

    private final String role;

    private final String userId;

    private final String username;

    private final String dni;

    private final String fullname;

    private final String photo_url;

    public JwtAuthenticationResponse(String token, String role, String userId, String username, String dni, String fullname, String photo_url)
    {
        this.token=token;
        this.role=role;
        this.userId=userId;
        this.username=username;
        this.dni=dni;
        this.fullname=fullname;
        this.photo_url = photo_url;
    }


    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getDni() {
        return dni;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
