package afamijas.service;

import afamijas.model.CalendarEvent;
import afamijas.model.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface RelativesService
{

    RouteDTO getRoute(String idpatient);

    RouteDTO changeRouteStop(String idpatient, String idroutestop, LocalDate from, LocalDate to);

    AbsenceDTO addAbsenceByRelative(String idpatient, String idrelative, LocalDate day, LocalDateTime from, LocalDateTime to, Boolean notransport, String comment);

    void deleteAbsence(String idpatient, String idabsence);

    MenuDTO getMenu(String idpatient);

    List<PermissionDTO> getPendingPermissions(String idrelative);

    PermissionDTO signPermission(String idpermission, String idpatient, MultipartFile file) throws Exception;

    List<CalendarEventDTO> getCalendarEvents(String idrelative);


}
