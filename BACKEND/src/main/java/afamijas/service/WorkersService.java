package afamijas.service;

import afamijas.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface WorkersService
{

    Page<PatientDTO> getActivePatients(String name_surnames, String dni, String groupcode, Integer page, Integer size, String order, String orderasc);

    void registerFeeding(String idpatient, String idworker, String dish, String result, String daymeal);

    void registerTempFridge(String idworker, Double temperature);

    void registerTempService(String idworker, String dish, String dishtype, Double tempreception, Double tempservice);

    void registerMealSample(String idworker, String dish, Boolean organoletico, Boolean cuerposextra, String comments);

    void registerLegionella(String idworker, Double value, String point, String signature);

    void registerWC(String idworker, String point, String signature);

    String uploadTimetable(MultipartFile file) throws Exception;

    String uploadActivities(MultipartFile file) throws Exception;

    WorkerAbsenceDTO addAbsenceByWorker(String idpatient, String idworker, String idroutestop, String comment);

    void deleteAbsence(String idpatient, String idabsence);

    void saveCalendarEvent(String idworker, String idcalendarevent, LocalDateTime start, LocalDateTime end, Boolean allDay, String title, Boolean dayoff, String description, List<String> roles, List<String> idsusers, LocalDateTime publishdate);

    List<CalendarEventDTO> getCalendarEvents(String idworker, List<String> roles, Boolean admin);

    List<UserDTO> getAllUsers(List<String> roles);


    void deleteCalendarEvent(String idcalendarevent);
}
