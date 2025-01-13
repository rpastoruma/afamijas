package afamijas.service;

import afamijas.model.User;
import afamijas.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface WorkersService
{

    Page<PatientDTO> getActivePatients(String name_surnames, String documentid, String groupcode, Integer page, Integer size, String order, String orderasc);

    List<PatientDTO> getAllPatients(String groupcode);

    PatientDTO getPatientById(String id);

    List<MemberDTO> getAllMembers();

    String uploadTimetable(MultipartFile file) throws Exception;

    String uploadActivities(MultipartFile file) throws Exception;

    WorkerAbsenceDTO addAbsenceByWorker(String idpatient, String idworker, String idroutestop, String comment);

    void deleteAbsence(String idpatient, String idabsence);

    void saveCalendarEvent(String idworker, String idcalendarevent, LocalDateTime start, LocalDateTime end, Boolean allDay, String title, Boolean dayoff, String description, List<String> roles, List<String> idsusers, LocalDateTime publishdate);

    List<CalendarEventDTO> getCalendarEvents(String idworker, List<String> roles, Boolean admin);

    List<UserDTO> getAllUsers(List<String> roles);


    void deleteCalendarEvent(String idcalendarevent);


    void saveMenu(String idadmin, String id, String type, String description, LocalDate from, LocalDate to, MultipartFile file) throws Exception;




    Page<MedicationDTO> getMedications(String idpatient, Integer page, Integer size, String order, String orderasc);

    void modifyMedication(String idpatient, String medicationDescriptionMorning, String medicationDescriptionEvening);




    Page<FoodDTO> getFoods(String idpatient, Integer page, Integer size, String order, String orderasc);

    void modifyFood(String idpatient, String menu_type, String breakfast_description);




    Page<FeedingDTO> getFeedings(User worker, String groupcode, String idpatient, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String order, String orderasc);

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




    Page<LegionellaLogDTO> getLegionellaLogs(User user, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void registerLegionellaLog(String id, String idworker, String point, Double value, Double temperature);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void deleteLegionellaLog(String id);




    Page<WCLogDTO> getWCLogs(User user, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void registerWCLog(String id, String idworker, String point, String hour);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void deleteWCLog(String id);



    Page<HealthLogDTO> getHealthLogs(User user, String groupcode, String idpatient, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String order, String orderasc);


    void registerHealthLog(String id, String idpatient, String id1, Double lowPressure, Double highPressure, Double sugar);

    void deleteHealthLog(String id);




    Page<DocDTO> getDocs(User user, String text, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc);

    void saveDoc(String id, String idworker, String title, String description, String url, LocalDate dayfrom, LocalDate dayto, List<String> roles);

    void deleteDoc(String id);



    Page<ReceiptDTO> getReceipts(User user, String idmember, LocalDate dayfrom, LocalDate dayto, String status, Integer page, Integer size, String orderby, String orderasc);


    void saveReceipt(String id, String idmember, String url, Double total, LocalDate duedate, String status, LocalDate paiddate);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void deleteReceipt(String id);







    Page<InvoiceDTO> getInvoices(User user, String idpatient, LocalDate dayfrom, LocalDate dayto, String status, Integer page, Integer size, String orderby, String orderasc);


    void saveInvoice(String id, String idpatient, String url, Double total, LocalDate duedate, String status, LocalDate paiddate);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void deleteInvoice(String id);





    Page<DocPsicoDTO> getDocsPsico(User user, String idptient, String type, String text, Integer page, Integer size, String orderby, String orderasc);

    void saveDocPsico(String id, String idworker, String idpatient, String type, String description, String url);

    void deleteDocPsico(String id);


}
