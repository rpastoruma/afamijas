package afamijas.service;

import afamijas.dao.AddressBookRepository;
import afamijas.dao.LogsRepository;
import afamijas.model.AddressBook;
import afamijas.model.Log;
import afamijas.model.Nomina;
import afamijas.model.dto.AddressBookDTO;
import afamijas.model.dto.NominaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService
{
    final AddressBookRepository addressBookRepository;

	final MongoTemplate mongoTemplate;

	@Value("${debug.queries}")
	Boolean debug_queries;

	@Autowired
	public AddressBookServiceImpl(AddressBookRepository addressBookRepository, MongoTemplate mongoTemplate)
	{
		this.addressBookRepository = addressBookRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<AddressBookDTO> search(String type, String fullname, String phone, String email, int page, int size)
	{
		page = Math.max(0, page);
		size = Math.max(1, size);

		List<Criteria> criteriaList = new ArrayList<>();
		Pageable pageable = PageRequest.of(page, size);

		if (type != null && !type.isEmpty()) {
			criteriaList.add(Criteria.where("type").is(type));
		}

		if (fullname != null && !fullname.isEmpty()) {
			criteriaList.add(Criteria.where("fullname").regex(".*" + fullname + ".*", "i"));
		}

		if (phone != null && !phone.isEmpty()) {
			criteriaList.add(Criteria.where("phone").is(phone));
		}

		if (email != null && !email.isEmpty()) {
			criteriaList.add(Criteria.where("email").is(email));
		}

		Query query = new Query();

		if (!criteriaList.isEmpty()) {
			query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
		}


		long total = this.mongoTemplate.count(query, AddressBook.class);

		Query paginatedQuery = query.with(pageable).with(Sort.by(Sort.Direction.ASC, "fullname"));
		try { if(debug_queries) System.out.println("search: " + paginatedQuery.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		List<AddressBook> results = mongoTemplate.find(paginatedQuery, AddressBook.class);
		List<AddressBookDTO> dtos = results.stream()
				.map(AddressBookDTO::new)
				.toList();

		return new PageImpl<>(dtos, pageable, total);
	}

	@Override
	public AddressBook saveAddressBook(String id, String fullname, String phone, String email, String observations)
	{
		AddressBook addressBook = null;
		if(id != null && !id.isEmpty())
		{
			addressBook = this.addressBookRepository.findById(id).orElse(null);
			if(addressBook == null) return null;
		}
		else
		{
			addressBook = new AddressBook();
			addressBook.setType("OTHER");
			addressBook.setCreated(LocalDateTime.now());
		}

		addressBook.setFullname(fullname);
		addressBook.setPhone(phone);
		addressBook.setEmail(email);
		addressBook.setObservations(observations);

		return addressBookRepository.save(addressBook);
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteAddressBook(String id)
	{
		AddressBook addressBook = this.addressBookRepository.findOne(id);
		if(addressBook!=null) this.addressBookRepository.delete(addressBook);
	}



}
