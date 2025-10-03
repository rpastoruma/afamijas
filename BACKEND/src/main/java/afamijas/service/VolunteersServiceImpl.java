package afamijas.service;

import afamijas.dao.*;
import afamijas.model.AddressBook;
import afamijas.model.Media;
import afamijas.model.User;
import afamijas.model.dto.MemberDTO;
import afamijas.model.dto.RelativeDTO;
import afamijas.model.dto.VolunteerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class VolunteersServiceImpl implements VolunteersService
{

	@Value("${debug.queries}")
	Boolean debug_queries;

	@Value("${media.path}")
	String mediapath;

	final MongoTemplate mongoTemplate;

    final UsersRepository usersRepository;

	final CitiesRepository citiesRepository;

	final StatesRepository statesRepository;

	final CountriesRepository countriesRepository;


	final MediaService mediaService;

	final NotificationsService notificationsService;

	final AddressBookRepository addressBookRepository;


	@Autowired
	public VolunteersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, CitiesRepository citiesRepository, StatesRepository statesRepository, CountriesRepository countriesRepository, MediaService mediaService, NotificationsService notificationsService, AddressBookRepository addressBookRepository)
	{
		this.mongoTemplate = mongoTemplate;
		this.usersRepository = usersRepository;
		this.citiesRepository = citiesRepository;
		this.statesRepository = statesRepository;
		this.countriesRepository = countriesRepository;
		this.mediaService = mediaService;
		this.notificationsService = notificationsService;
        this.addressBookRepository = addressBookRepository;
    }

	//TODO: REVISAR SI LA PAGINACIÓN ESTÁ BIEN HECHA YA QUE NO ESTÁ COMO EN LAS OTRAS
	@Override
	public Page<VolunteerDTO> getVolunteers(String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = new Query().addCriteria(Criteria.where("roles").in("VOLUNTEER")).with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));

		if(name_surnames!=null)
		{
			Criteria names_or_criteria = new Criteria();
			names_or_criteria.orOperator(Criteria.where("name").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname1").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname2").regex(".*"+name_surnames+".*", "i"));

			query.addCriteria(names_or_criteria);
		}
		if(documentid!=null) query.addCriteria(Criteria.where("documentid").is(documentid));
		if(status!=null) query.addCriteria(Criteria.where("status").is(status));

		if(debug_queries) System.out.println("getVolunteers: " + query.getQueryObject().toJson());
		List<VolunteerDTO> list = this.mongoTemplate.find(query, User.class).stream().map(x -> new VolunteerDTO(x, null, null, null)).toList();

		Query finalQuery = query;
		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> this.mongoTemplate.count(Query.of(finalQuery).limit(-1).skip(-1), User.class));
	}

	@Override
	public VolunteerDTO saveVolunteer(String id, String name, String surname1, String surname2, String email, String phone, String documentid, String documenttype,
								String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
								 String register_document_url, Boolean is_document_signed)
	{
		User volunteer = null;
		if(id!=null)
		{
			volunteer = this.usersRepository.findOne(id);
			if(volunteer==null) return null;
		}
		else
		{
			volunteer = new User();

			volunteer.setUnregister_document_url("");
			volunteer.setUnregister_document_url_signed("");

			volunteer.setRegister_document_url("");
			volunteer.setRegister_document_url_signed("");
		}

		if(register_document_url!=null)
		{
			if(is_document_signed)
			{
				volunteer.setRegister_document_url("");
				volunteer.setRegister_document_url_signed(register_document_url);
			}
			else
			{
				volunteer.setRegister_document_url(register_document_url);
				volunteer.setRegister_document_url_signed("");
			}
		}

		volunteer.setRoles(Arrays.asList("VOLUNTEER"));
		volunteer.setStatus("A");

		volunteer.setName(name);
		volunteer.setSurname1(surname1);
		volunteer.setSurname2(surname2);
		volunteer.setEmail(email);
		volunteer.setPhone(phone);
		volunteer.setDocumentid(documentid);
		volunteer.setDocumenttype(documenttype);

		volunteer.setIdcity(idcity);
		volunteer.setIdstate(idstate);
		volunteer.setIdcountry(idcountry);
		volunteer.setPostaladdress(postaladdress);
		volunteer.setPostalcode(postalcode);

		volunteer = this.usersRepository.save(volunteer);

		//ACTUALIZAMOS AGENDA
		try
		{
			List<AddressBook> addressBooks = this.addressBookRepository.findByIduser(volunteer.get_id());
			if(addressBooks!=null && addressBooks.size()>0)
				for(AddressBook addressBook:addressBooks)
				{
					addressBook.setFullname(volunteer.getFullname());
					addressBook.setPhone(volunteer.getPhone());
					addressBook.setEmail(volunteer.getEmail());
					this.addressBookRepository.save(addressBook);
				}
			else
			{
				AddressBook addressBook = new AddressBook(volunteer);
				this.addressBookRepository.save(addressBook);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return new VolunteerDTO(volunteer, this.citiesRepository.findOne(idcity), this.statesRepository.findOne(idstate), this.countriesRepository.findOne(idcountry));
	}




	@Override
	public void unregisterVolunteer (String id, String unregister_reason, String unregister_document_url, boolean is_document_signed)
	{
		User member = this.usersRepository.findOne(id);
		if(member!=null)
		{
			member.setStatus("D");
			member.setUnregister_reason(unregister_reason==null?"":unregister_reason);
			if(unregister_document_url!=null && !is_document_signed) member.setUnregister_document_url(unregister_document_url);
			if(unregister_document_url!=null && is_document_signed) member.setUnregister_document_url_signed(unregister_document_url);
			this.usersRepository.save(member);
		}

	}

	@Override
	public String uploadRegisterDocument(String id, MultipartFile file) throws Exception
	{
		User member = this.usersRepository.findOne(id);
		if(member!=null)
		{
			member.setStatus("D");
			Media media = this.mediaService.create(UUID.randomUUID().toString(), "member", "registerdocument", file);
			member.setRegister_document_url(media.getUrl());
			this.usersRepository.save(member);
			return media.getUrl();
		}
		else
			return null;

	}

	@Override
	public String uploadUnRegisterDocument(String id, MultipartFile file) throws Exception {
		User member = this.usersRepository.findOne(id);
		if(member!=null)
		{
			member.setStatus("D");
			Media media = this.mediaService.create(UUID.randomUUID().toString(), "member", "unregisterdocument", file);
			member.setRegister_document_url(media.getUrl());
			this.usersRepository.save(member);
			return media.getUrl();
		}
		else
			return null;
	}

	@Override
	public void signDocumentRegister(String idmember, String registerDocumentUrlSigned)
	{
		User member = this.usersRepository.findOne(idmember);
		if(member!=null)
		{
			member.setRegister_document_url_signed(registerDocumentUrlSigned);
			this.usersRepository.save(member);
		}
	}

	@Override
	public void signDocumentUnRegister(String idmember, String unregisterDocumentUrlSigned)
	{
		User member = this.usersRepository.findOne(idmember);
		if(member!=null)
		{
			member.setUnregister_document_url_signed(unregisterDocumentUrlSigned);
			this.usersRepository.save(member);
		}
	}



}
