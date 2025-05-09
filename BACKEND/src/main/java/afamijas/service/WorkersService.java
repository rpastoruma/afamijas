package afamijas.service;

import afamijas.model.Doc;
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

    void saveCalendarEvent(String idworker, String idcalendarevent, LocalDateTime start, LocalDateTime end, Boolean allDay, String title, Boolean dayoff, String description, List<String> roles, List<String> idsusers, LocalDateTime publishdate, String url);

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

    Doc saveDoc(String id, String idworker, String title, String description, String url, LocalDate dayfrom, LocalDate dayto, List<String> roles, boolean isAdmin, boolean createEvent)  throws Exception;

    void deleteAdminDoc(String id);

    void deleteWorkerDoc(String id, String idworker) throws Exception;


    Page<ReceiptDTO> getReceipts(User user, String idmember, LocalDate dayfrom, LocalDate dayto, String status, Integer page, Integer size, String orderby, String orderasc);


    void saveReceipt(String id, String idmember, String url, Double total, LocalDate duedate, String status, LocalDate paiddate);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    void deleteReceipt(String id);







    Page<InvoiceDTO> getInvoices(User user, String idpatient, LocalDate dayfrom, LocalDate dayto, String status, Integer page, Integer size, String orderby, String orderasc);


    void saveInvoice(String id, String idpatient, String url, Double total, LocalDate duedate, String status, LocalDate paiddate);

    void deleteInvoice(String id);





    Page<DocPsicoDTO> getDocsPsico(User user, String idptient, String type, String text, Integer page, Integer size, String orderby, String orderasc);

    void saveDocPsico(String id, String idworker, String idpatient, String type, String description, String url);

    void deleteDocPsico(String id);




    Page<AtencionDTO> getAtenciones(LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String order, String orderasc);

    void registerAtencion(String id, String idworker, String number, LocalDate datedone, String clientfullname, String sex, String nationality, String relationship, String why, String via, String professional, String observations);

    void deleteAtencion(String id);


    Page<WorkerDTO> getWorkers(String role, String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc);

    WorkerDTO saveWorker(String id, List<String> roles, String name, String surname1, String surname2, String email, String password, String phone, String documentid, String documenttype,
                         String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
                         String nss, String categoria_profesional, String tipo_contrato, String jornada_laboral, String horario);

    void unregisterWorker(String id);

    Page<NominaDTO>  getNominas(User user, String idworker, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String order, String orderasc);

    void saveNomina(String id, String idworker, String url, LocalDate duedate);

    void deleteNomina(String id);

    List<WorkerDTO> getAllWorkers();

    Page<WorkerAbsenceDTO> getWorkerAbsences(String idpatient, LocalDateTime from, LocalDateTime to, int page, int size, String orderby, String orderasc);

    WorkerAbsenceDTO saveWorkerAbsence(String id, String idpatient, String id1, String idroutestop, String comment, LocalDateTime when);

    void deleteWorkerAbsence(String idabsence);

    Page<ProjectDTO> getProjects(String text, LocalDate dayfrom, LocalDate dayto, Boolean subvencion_concedida, Integer page, Integer size, String order, String orderasc);

    ProjectDTO saveProject(String id, String id1, String nombre, LocalDate fechaPresentacion, LocalDate fechaResolucion, String plazoEjecucion, Boolean subvencionConcedida, Double importeSolicitado, Double importeConcedido);

    void deleteProject(String id);

    DocDTO saveDocProject(String id, String idworker, String idproject, String title, String description, String url, LocalDate dayfrom) throws Exception;

    void deleteDocProject(String id, String idproject);
}
