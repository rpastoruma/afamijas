package afamijas.service;

import afamijas.model.dto.MemberDTO;
import afamijas.model.dto.PatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public interface PatientsService
{

    Page<PatientDTO> getPatients(String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc);

    PatientDTO savePatient(String id, String name, String surname1, String surname2, LocalDate birthdate, String documentid, String documenttype,
                           String idrelative, String relativerelation,
                           String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
                           String servicetype, Boolean tallerpsico, Boolean transportservice, String transportservice_text, Boolean comedorservice, String comedorservice_text, Boolean ayudadomicilioservice, String ayudadomicilioservice_text, Boolean duchaservice, String duchaservice_text,
                           String register_document_url, Boolean is_document_signed);


    void unregisterPatient(String id, String unregister_reason, String unregister_document_url, boolean is_document_signed);

    String uploadRegisterDocument(String id, MultipartFile file) throws Exception;

    String uploadUnRegisterDocument(String id, MultipartFile file) throws Exception;


    void signDocumentRegister(String idpatient, String registerDocumentUrlSigned);

    void signDocumentUnRegister(String idpatient, String unregisterDocumentUrlSigned);
}
