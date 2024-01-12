package afamijas.utils;

public class SMTPServer
{
    private String host;
    private int port;
    private boolean auth;
    private String user;
    private String pass;
    private boolean starttls;

    public SMTPServer(String host, int port, boolean auth, String user, String pass, boolean starttls)
    {
        this.host = host.toLowerCase();  //forzamos para comparaciones
        this.port = port;
        this.auth = auth;
        this.user = user;
        this.pass = pass;
        this.starttls = starttls;
    }

    public String getHost() { return this.host; }
    public int getPort() { return this.port; }
    public boolean getAuth() { return this.auth; }
    public String getUser() { return this.user; }
    public String getPass() { return this.pass; }
    public boolean getStarttls() { return this.starttls; }



}
