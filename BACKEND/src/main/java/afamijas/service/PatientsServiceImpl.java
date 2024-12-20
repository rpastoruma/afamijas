package afamijas.service;

import afamijas.dao.CitiesRepository;
import afamijas.dao.CountriesRepository;
import afamijas.dao.StatesRepository;
import afamijas.dao.UsersRepository;
import afamijas.model.Media;
import afamijas.model.User;
import afamijas.model.dto.MemberDTO;
import afamijas.model.dto.PatientDTO;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PatientsServiceImpl implements PatientsService
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


	@Autowired
	public PatientsServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, CitiesRepository citiesRepository, StatesRepository statesRepository, CountriesRepository countriesRepository, MediaService mediaService, NotificationsService notificationsService)
	{
		this.mongoTemplate = mongoTemplate;
		this.usersRepository = usersRepository;
		this.citiesRepository = citiesRepository;
		this.statesRepository = statesRepository;
		this.countriesRepository = countriesRepository;
		this.mediaService = mediaService;
		this.notificationsService = notificationsService;
	}

	//TODO: REVISAR SI LA PAGINACIÓN ESTÁ BIEN HECHA YA QUE NO ESTÁ COMO EN LAS OTRAS
	@Override
	public Page<PatientDTO> getPatients(String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = new Query().addCriteria(Criteria.where("roles").in("PATIENT")).with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));

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

		if(debug_queries) System.out.println("getPatients: " + query.getQueryObject().toJson());
		List<PatientDTO> list = this.mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, this.usersRepository.findOne(x.getIdrelative()), null)).toList();

		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> this.mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
	}

	@Override
	public PatientDTO savePatient(String id, String name, String surname1, String surname2, LocalDate birthdate, String documentid, String documenttype,
								  String idrelative, String relativerelation,
								  String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
								  String servicetype, Boolean tallerpsico, Boolean transportservice, String transportservice_text, Boolean comedorservice, String comedorservice_text, Boolean ayudadomicilioservice, String ayudadomicilioservice_text, Boolean duchaservice, String duchaservice_text,
								  String register_document_url, Boolean is_document_signed)
	{
		User patient = null;
		if(id!=null)
		{
			patient = this.usersRepository.findOne(id);
			if(patient==null) return null;
		}
		else
		{
			patient = new User();

			patient.setUnregister_document_url("");
			patient.setUnregister_document_url_signed("");

			patient.setRegister_document_url("");
			patient.setRegister_document_url_signed("");
		}

		if(register_document_url!=null)
		{
			if(is_document_signed)
			{
				patient.setRegister_document_url("");
				patient.setRegister_document_url_signed(register_document_url);
			}
			else
			{
				patient.setRegister_document_url(register_document_url);
				patient.setRegister_document_url_signed("");
			}
		}

		patient.setRoles(Arrays.asList("PATIENT"));
		patient.setStatus("A");

		patient.setName(name);
		patient.setSurname1(surname1);
		patient.setSurname2(surname2);
		patient.setBirthdate(birthdate);
		patient.setDocumentid(documentid);
		patient.setDocumenttype(documenttype);
		
		patient.setIdrelative(idrelative);
		patient.setRelativerelation(relativerelation);

		patient.setIdcity(idcity);
		patient.setIdstate(idstate);
		patient.setIdcountry(idcountry);
		patient.setPostaladdress(postaladdress);
		patient.setPostalcode(postalcode);
		
		patient.setTallerpsico(tallerpsico);
		
		patient.setTransportservice(transportservice);
		if(transportservice)
			patient.setTransportservice_text(transportservice_text);
		else
			patient.setTransportservice_text("");

		patient.setComedorservice(comedorservice);
		if(comedorservice)
			patient.setComedorservice_text(comedorservice_text);
		else
			patient.setComedorservice_text("");

		patient.setAyudadomicilioservice(ayudadomicilioservice);
		if(ayudadomicilioservice)
			patient.setAyudadomicilioservice_text(ayudadomicilioservice_text);
		else
			patient.setAyudadomicilioservice_text("");

		patient.setDuchaservice(duchaservice);
		if(duchaservice)
			patient.setDucha_text(duchaservice_text);
		else
			patient.setDucha_text("");

		return new PatientDTO(this.usersRepository.save(patient), this.citiesRepository.findOne(idcity), this.statesRepository.findOne(idstate), this.countriesRepository.findOne(idcountry), this.usersRepository.findOne(patient.getIdrelative()), null);
	}


	@Override
	public void unregisterPatient (String id, String unregister_reason, String unregister_document_url, boolean is_document_signed)
	{
		User patient = this.usersRepository.findOne(id);
		if(patient!=null)
		{
			patient.setStatus("D");
			patient.setUnregister_reason(unregister_reason==null?"":unregister_reason);
			if(unregister_document_url!=null && !is_document_signed) patient.setUnregister_document_url(unregister_document_url);
			if(unregister_document_url!=null && is_document_signed) patient.setUnregister_document_url_signed(unregister_document_url);
			this.usersRepository.save(patient);
		}

	}

	@Override
	public String uploadRegisterDocument(String id, MultipartFile file) throws Exception
	{
		User patient = this.usersRepository.findOne(id);
		if(patient!=null)
		{
			patient.setStatus("D");
			Media media = this.mediaService.create(UUID.randomUUID().toString(), "patient", "registerdocument", file);
			patient.setRegister_document_url(media.getUrl());
			this.usersRepository.save(patient);
			return media.getUrl();
		}
		else
			return null;

	}

	@Override
	public String uploadUnRegisterDocument(String id, MultipartFile file) throws Exception {
		User patient = this.usersRepository.findOne(id);
		if(patient!=null)
		{
			patient.setStatus("D");
			Media media = this.mediaService.create(UUID.randomUUID().toString(), "patient", "unregisterdocument", file);
			patient.setRegister_document_url(media.getUrl());
			this.usersRepository.save(patient);
			return media.getUrl();
		}
		else
			return null;
	}

	@Override
	public void signDocumentRegister(String idpatient, String registerDocumentUrlSigned)
	{
		User patient = this.usersRepository.findOne(idpatient);
		if(patient!=null)
		{
			patient.setRegister_document_url_signed(registerDocumentUrlSigned);
			this.usersRepository.save(patient);
		}
	}

	@Override
	public void signDocumentUnRegister(String idpatient, String unregisterDocumentUrlSigned)
	{
		User patient = this.usersRepository.findOne(idpatient);
		if(patient!=null)
		{
			patient.setUnregister_document_url_signed(unregisterDocumentUrlSigned);
			this.usersRepository.save(patient);
		}
	}


}
