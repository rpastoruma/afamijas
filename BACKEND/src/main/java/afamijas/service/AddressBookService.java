package afamijas.service;

import afamijas.model.AddressBook;
import afamijas.model.Log;
import afamijas.model.dto.AddressBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface AddressBookService
{
	AddressBook saveAddressBook(String id, String fullname, String phone, String email, String observations);

	Page<AddressBookDTO> search(String type, String fullname, String phone, String email, int page, int size);

	void deleteAddressBook(String id);
}

