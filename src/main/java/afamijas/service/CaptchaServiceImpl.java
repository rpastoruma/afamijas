package afamijas.service;


import com.fasterxml.jackson.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class CaptchaServiceImpl implements CaptchaService
{
    final ConfigurationService configurationService;
    final ReCaptchaAttemptService reCaptchaAttemptService;

    @Autowired
    public CaptchaServiceImpl(ConfigurationService configurationService, ReCaptchaAttemptService reCaptchaAttemptService)
    {
        this.configurationService = configurationService;
        this.reCaptchaAttemptService = reCaptchaAttemptService;
    }

    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public void processResponse(String response, String clientIP) throws Exception
    {
        if(!responseSanityCheck(response)) {
            throw new Exception("Response contains invalid characters");
        }

        if(reCaptchaAttemptService.isBlocked(clientIP)) {
            throw new Exception("Client exceeded maximum number of failed attempts");
        }

        URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",  configurationService.value("google.recaptcha.apisecret"), response, clientIP));

        GoogleResponse googleResponse = new RestTemplate().getForObject(verifyUri, GoogleResponse.class);


        if(!googleResponse.isSuccess()) {
            if(googleResponse.hasClientError()) {
                reCaptchaAttemptService.reCaptchaFailed(clientIP);
            }
            throw new Exception("reCaptcha was not successfully validated");
        }
        reCaptchaAttemptService.reCaptchaSucceeded(clientIP);

    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
}



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "success",
        "challenge_ts",
        "hostname",
        "error-codes"
})
class GoogleResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;

    @JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if(errors == null) {
            return false;
        }
        for(ErrorCode error : errors) {
            if(error==null) return false;
            switch(error) {
                case InvalidResponse:
                case MissingResponse:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    static enum ErrorCode {
        MissingSecret,     InvalidSecret,
        MissingResponse,   InvalidResponse;

        private static Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>(4);

        static {
            errorsMap.put("missing-input-secret",   MissingSecret);
            errorsMap.put("invalid-input-secret",   InvalidSecret);
            errorsMap.put("missing-input-response", MissingResponse);
            errorsMap.put("invalid-input-response", InvalidResponse);
        }

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public ErrorCode[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(ErrorCode[] errorCodes) {
        this.errorCodes = errorCodes;
    }


}