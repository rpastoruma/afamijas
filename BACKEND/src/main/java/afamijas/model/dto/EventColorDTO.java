package afamijas.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include. NON_NULL)
public class EventColorDTO
{

    private String primary;

    private String secondary;

    private String secondaryText;

    public EventColorDTO(String primary, String secondary, String secondaryText) {
        this.primary = primary;
        this.secondary = secondary;
        this.secondaryText = secondaryText;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }
}
