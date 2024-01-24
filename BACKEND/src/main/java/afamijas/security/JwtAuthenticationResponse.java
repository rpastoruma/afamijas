package afamijas.security;


import java.io.Serializable;
import java.util.ArrayList;

public class JwtAuthenticationResponse implements Serializable
{

    private static final long serialVersionUID = 1250165508152483573L;

    private final String token;

    private final ArrayList<String> roles;

    private final String userId;

    private final String username;

    private final String dni;

    private final String fullname;

    private final String photo_url;

    public JwtAuthenticationResponse(String token, ArrayList<String> roles, String userId, String username, String dni, String fullname, String photo_url)
    {
        this.token=token;
        this.roles=roles;
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

    public ArrayList<String> getRoles() {
        return roles;
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
