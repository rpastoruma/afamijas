package afamijas.service;

import afamijas.model.dto.MemberDTO;
import afamijas.model.dto.RelativeDTO;
import afamijas.model.dto.VolunteerDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface VolunteersService
{

    Page<VolunteerDTO> getVolunteers(String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc);

    VolunteerDTO saveVolunteer(String id, String name, String surname1, String surname2, String email, String phone, String documentid, String documenttype,
                         String postaladdress, Integer idcity, Integer idstate,  Integer idcountry,  String postalcode,
                         String unregister_document_url, Boolean is_document_signed);


    void unregisterVolunteer(String id, String unregister_reason, String unregister_document_url, boolean is_document_signed);

    String uploadRegisterDocument(String id, MultipartFile file) throws Exception;

    String uploadUnRegisterDocument(String id, MultipartFile file) throws Exception;


    void signDocumentRegister(String idvolunteer, String registerDocumentUrlSigned);

    void signDocumentUnRegister(String idvolunteer, String unregisterDocumentUrlSigned);

}
