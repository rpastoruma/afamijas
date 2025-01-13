package afamijas.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationResponse implements Serializable
{

    private static final long serialVersionUID = 1250165508152483573L;

    private final String token;

    private final List<String> roles;

    private final String userId;

    private final String username;

    private final String documentid;

    private final String fullname;

    private final String photo_url;

    public JwtAuthenticationResponse(String token, List<String> roles, String userId, String username, String documentid, String fullname, String photo_url)
    {
        this.token=token;
        this.roles=roles;
        this.userId=userId;
        this.username=username;
        this.documentid=documentid;
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

    public List<String> getRoles() {
        return roles;
    }



    public String getFullname() {
        return fullname;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
