package afamijas.service;


public interface CaptchaService {


    public void processResponse(String response, String clientIP) throws Exception;
}
