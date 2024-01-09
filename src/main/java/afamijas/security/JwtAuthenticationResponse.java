package afamijas.security;


import java.io.Serializable;
import java.util.List;

public class JwtAuthenticationResponse implements Serializable
{

    private static final long serialVersionUID = 1250165508152483573L;

    private final String token;

    private final List<String> roles;

    private final String userId;

    private final String username;

    private final String apikey;

    public JwtAuthenticationResponse(String token, List<String> roles, String userId, String username, String apikey)
    {
        this.token=token;
        this.roles=roles;
        this.userId=userId;
        this.username=username;
        this.apikey=apikey;
    }


    public String getToken() {
        return token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getApikey() {
        return apikey;
    }
}
