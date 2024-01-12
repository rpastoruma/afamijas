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

    public JwtAuthenticationResponse(String token, String role, String userId, String username, String dni)
    {
        this.token=token;
        this.role=role;
        this.userId=userId;
        this.username=username;
        this.dni=dni;
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
}
