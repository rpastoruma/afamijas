package afamijas.service;

import afamijas.model.User;
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

    List<PatientDTO> getAllPatients(String groupcode);



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


    void saveMenu(String id, String type, String description, LocalDate from, LocalDate to, MultipartFile file) throws Exception;




    Page<MedicationDTO> getMedications(String idpatient, Integer page, Integer size, String order, String orderasc);

    void modifyMedication(String idpatient, String medicationDescriptionMorning, String medicationDescriptionEvening);



    Page<FoodDTO> getFoods(String idpatient, Integer page, Integer size, String order, String orderasc);

    void modifyFood(String idpatient, String menu_type, String breakfast_description);



    Page<FeedingDTO> getFeedings(User worker, String groupcode, String idpatient, LocalDate day, Integer page, Integer size, String order, String orderasc);

    void registerFeeding(String id, String idpatient, String idworker, String dish, String result, String daymeal, String indications, String incidences);

    void deleteFeeding(String id);



    Page<TempFridgeDTO> getTempFridges(User worker, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc);

    void registerTempFridge(String id, String idworker, Double temperature);

    void deleteTempFridge(String id);



    Page<TempServiceDTO> getTempServices(User worker, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc);

    void registerTempService(String id, String idworker, String dish, String dishtype, Double tempreception, Double tempservice);

    void deleteTempService(String id);

    Page<MealSampleDTO>  getMealSamples(User user, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String order, String orderasc);

    void registerMealSample(String id, String idworker, String dish, Boolean orgenolepticoOk, Boolean cuerposExtraOk, String comments);

    void deleteMealSample(String id);
}
