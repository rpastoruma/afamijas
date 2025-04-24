package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MembersServiceImpl implements MembersService
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
	public MembersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, CitiesRepository citiesRepository, StatesRepository statesRepository, CountriesRepository countriesRepository, MediaService mediaService, NotificationsService notificationsService, AddressBookRepository addressBookRepository)
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
	public Page<MemberDTO> getMembers(Integer membernumber, String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = new Query().addCriteria(Criteria.where("roles").in("MEMBER")).with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));

		if(name_surnames!=null)
		{
			Criteria names_or_criteria = new Criteria();
			names_or_criteria.orOperator(Criteria.where("name").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname1").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname2").regex(".*"+name_surnames+".*", "i"));

			query.addCriteria(names_or_criteria);
		}
		if(membernumber!=null) query.addCriteria(Criteria.where("membernumber").is(membernumber));
		if(documentid!=null) query.addCriteria(Criteria.where("documentid").is(documentid));
		if(status!=null) query.addCriteria(Criteria.where("status").is(status));

		if(debug_queries) System.out.println("findMyNotifications: " + query.getQueryObject().toJson());
		List<MemberDTO> list = this.mongoTemplate.find(query, User.class).stream().map(x -> new MemberDTO(x, null, null, null)).toList();

		Query finalQuery = query;
		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> this.mongoTemplate.count(Query.of(finalQuery).limit(-1).skip(-1), User.class));
	}

	@Override
	public MemberDTO saveMember(String id, String name, String surname1, String surname2, String email, String phone, String documentid, String documenttype,
								String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
								Double fee_euros, String fee_period, String fee_payment,
								String bank_name, String bank_account_holder_fullname, String bank_account_holder_dni, String bank_account_iban, String register_document_url, Boolean is_document_signed)
	{
		User member = null;
		if(id!=null)
		{
			member = this.usersRepository.findOne(id);
			if(member==null) return null;
		}
		else
		{
			member = new User();
			member.setMembernumber(this.getLastMemberNumber()+1);

			member.setUnregister_document_url("");
			member.setUnregister_document_url_signed("");

			member.setRegister_document_url("");
			member.setRegister_document_url_signed("");
		}

		if(register_document_url!=null)
		{
			if(is_document_signed)
			{
				member.setRegister_document_url("");
				member.setRegister_document_url_signed(register_document_url);
			}
			else
			{
				member.setRegister_document_url(register_document_url);
				member.setRegister_document_url_signed("");
			}
		}

		member.setRoles(Arrays.asList("MEMBER"));
		member.setStatus("A");

		member.setName(name);
		member.setSurname1(surname1);
		member.setSurname2(surname2);
		member.setEmail(email);
		member.setPhone(phone);
		member.setDocumentid(documentid);
		member.setDocumenttype(documenttype);

		member.setIdcity(idcity);
		member.setIdstate(idstate);
		member.setIdcountry(idcountry);
		member.setPostaladdress(postaladdress);
		member.setPostalcode(postalcode);

		member.setFee_euros(fee_euros);
		member.setFee_period(fee_period);
		member.setFee_payment(fee_payment);

		member.setBank_name(bank_name);
		member.setBank_account_holder_fullname(bank_account_holder_fullname);
		member.setBank_account_holder_dni(bank_account_holder_dni);
		member.setBank_account_iban(bank_account_iban);

		member = this.usersRepository.save(member);

		//ACTUALIZAMOS AGENDA
		try
		{
			List<AddressBook> addressBooks = this.addressBookRepository.findByIduser(member.get_id());
			if(addressBooks!=null && addressBooks.size()>0)
				for(AddressBook addressBook:addressBooks)
				{
					addressBook.setFullname(member.getFullname());
					addressBook.setPhone(member.getPhone());
					addressBook.setEmail(member.getEmail());
					this.addressBookRepository.save(addressBook);
				}
			else
			{
				AddressBook addressBook = new AddressBook(member);
				this.addressBookRepository.save(addressBook);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return new MemberDTO(member, this.citiesRepository.findOne(idcity), this.statesRepository.findOne(idstate), this.countriesRepository.findOne(idcountry));
	}

	private Integer getLastMemberNumber()
	{
		final Query query = new Query().addCriteria(Criteria.where("roles").in("MEMBER")).with(Sort.by(Sort.Direction.DESC, "membernumber")).limit(1);
		User user = mongoTemplate.findOne(query, User.class);
		if(user==null)
			return 0;
		else return
				user.getMembernumber();
	}


	@Override
	public void unregisterMember (String id, String unregister_reason, String unregister_document_url, boolean is_document_signed)
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
