package afamijas.service;

import afamijas.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    RelativeAbsenceDTO saveAbsenceByRelative(String id, String idpatient, String idrelative, LocalDateTime from, LocalDateTime to, Boolean allday, String transport, String comment);

    void deleteAbsence(String idpatient, String idabsence);

    List<MenuDTO> getMenu(String idpatient);

    PermissionDTO signPermission(String idrelative, String idpermission, String idpatient, String url_signed_file) throws Exception;

    List<CalendarEventDTO> getCalendarEvents(String idrelative);


    List<PatientDTO> getPatients(String idrelative);

    Page<RelativeAbsenceDTO> getRelativeAbsences(String idpatient, String idrelative, LocalDateTime from, LocalDateTime to, int page, int size, String orderby, String orderasc);

    Page<PermissionDTO> getPermissions(String idpatient, String status, int page, int size, String orderby, String orderasc);


    List<RelativeDTO> getAllRelatives();

}
