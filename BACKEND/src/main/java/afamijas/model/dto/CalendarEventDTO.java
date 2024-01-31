package afamijas.model.dto;

import afamijas.model.CalendarEvent;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonInclude(JsonInclude.Include. NON_NULL)
public class CalendarEventDTO {

    private String id;

    private String start;

    private String end;

    private String title;

    private Boolean allDay;


    private EventColorDTO color;

    private Object actions; // TODO: IMPLEMENTAR

    private String cssClass;  //TODO : IMPLEMENTAR. Por ahora será null siempre

    private Object resizable; //TODO: IMPLEMENTAR

    private Boolean draggable; //TODO: IMPLEMENTAR. Por ahora será false siempre

    private Object meta; //TODO: IMPLEMENTAR


    private Boolean dayoff;

    private String description;

    private List<String> roles;

    private List<String> idsusers;


    private String publishdate;

    public CalendarEventDTO(CalendarEvent calendarEvent)
    {
        this.id = calendarEvent.get_id();
        this.start = formatDate(calendarEvent.getStart());
        this.end = formatDate(calendarEvent.getEnd());
        this.title = calendarEvent.getTitle();
        this.allDay = calendarEvent.getAllDay();

        if(calendarEvent.getDayoff()!=null && calendarEvent.getDayoff())
            this.color = new EventColorDTO("#AD2121", "#FAE3E3", "Festivo");
        else if((calendarEvent.getRoles()==null ||  calendarEvent.getRoles().size()==0) && (calendarEvent.getIdsusers()==null ||  calendarEvent.getIdsusers().size()==0))
            this.color = new EventColorDTO("#9bf542", "#eaf5df", "General");
        else if((calendarEvent.getRoles()!=null &&  calendarEvent.getRoles().size()>0) && calendarEvent.getRoles().contains("RELATIVE"))
            this.color = new EventColorDTO("#f5ad1d", "#f5e9d3", "Familiares");
        else if((calendarEvent.getRoles()!=null &&  calendarEvent.getRoles().size()>0) && !calendarEvent.getRoles().contains("RELATIVE"))
            this.color = new EventColorDTO("#1f6fbf", "#aed0f2", "Trabajadores");
        else
            this.color = new EventColorDTO("#0a051a", "#c6c5c9", "Específico");

        this.actions = null;
        this.cssClass = null;
        this.resizable = null;
        this.draggable = false;
        this.meta = null;

        this.dayoff = calendarEvent.getDayoff();
        this.description = calendarEvent.getDescription();

        this.roles = calendarEvent.getRoles();
        this.idsusers = calendarEvent.getIdsusers();

        this.publishdate = formatDate(calendarEvent.getPublishdate());
    }


    private String formatDate(LocalDateTime thedate)
    {
        if(thedate==null) return null;
        else return thedate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public EventColorDTO getColor() {
        return color;
    }

    public void setColor(EventColorDTO color) {
        this.color = color;
    }

    public Object getActions() {
        return actions;
    }

    public void setActions(Object actions) {
        this.actions = actions;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Object getResizable() {
        return resizable;
    }

    public void setResizable(Object resizable) {
        this.resizable = resizable;
    }

    public Boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public Boolean getDayoff() {
        return dayoff;
    }

    public void setDayoff(Boolean dayoff) {
        this.dayoff = dayoff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getIdsusers() {
        return idsusers;
    }

    public void setIdsusers(List<String> idsusers) {
        this.idsusers = idsusers;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }
}
