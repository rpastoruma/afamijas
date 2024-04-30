package afamijas.model.dto;

import afamijas.model.Doc;
import afamijas.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UrlDTO
{

    private String url;


    public UrlDTO(String url)
    {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
