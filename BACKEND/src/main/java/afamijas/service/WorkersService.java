package afamijas.service;

import afamijas.model.CalendarDay;
import afamijas.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public interface WorkersService
{

    Page<PatientDTO> getActivePatients(String name_surnames, String dni, String groupcode, Integer page, Integer size, String order, String orderasc);

    void registerFeeding(String idpatient, String idworker, String dish, String result, String daymeal);

}
