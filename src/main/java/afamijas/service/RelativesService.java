package afamijas.service;

import afamijas.model.dto.AbsenceDTO;
import afamijas.model.dto.MenuDTO;
import afamijas.model.dto.PermissionDTO;
import afamijas.model.dto.RouteDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RelativesService
{

    RouteDTO getRoute(String idpatient);

    RouteDTO changeRouteStop(String idpatient, String idroutestop, LocalDate from, LocalDate to);

    AbsenceDTO addAbsence(String idpatient, LocalDate day, String comment);

    void deleteAbsence(String idabsence);

    MenuDTO getMenu(String idpatient);

    List<PermissionDTO> getPendingPermissions(String idrelative);

    PermissionDTO signPermission(String idpermission, MultipartFile file) throws Exception;



}
