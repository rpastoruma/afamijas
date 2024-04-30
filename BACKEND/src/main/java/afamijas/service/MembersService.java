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
public interface MembersService
{

    Page<MemberDTO> getMembers(Integer membernumber, String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc);

    MemberDTO saveMember(String id, String name, String surname1, String surname2, String email, String phone, String documentid, String documenttype,
                         String postaladdress, String idcity, String idstate, String postalcode,
                         Double fee_euros, String fee_period, String fee_payment,
                         String bank_name, String bank_account_holder_fullname, String bank_account_holder_dni, String bank_account_iban);


    void unregisterMember(String id, String unregister_reason);

    String uploadRegisterDocument(String id, MultipartFile file) throws Exception;

    String uploadUnRegisterDocument(String id, MultipartFile file) throws Exception;




}
