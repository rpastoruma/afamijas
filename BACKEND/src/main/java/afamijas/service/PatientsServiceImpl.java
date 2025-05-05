package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.PatientDTO;
import afamijas.queuemail.model.dto.SendingResultDTO;
import afamijas.utils.FileUtils;
import afamijas.utils.StringUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import afamijas.utils.Template;
import org.springframework.web.multipart.MultipartFile;

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

	final Template template;


	final AddressBookRepository addressBookRepository;


	@Autowired
	public PatientsServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, CitiesRepository citiesRepository, StatesRepository statesRepository, CountriesRepository countriesRepository, MediaService mediaService, NotificationsService notificationsService, Template template, AddressBookRepository addressBookRepository)
	{
		this.mongoTemplate = mongoTemplate;
		this.usersRepository = usersRepository;
		this.citiesRepository = citiesRepository;
		this.statesRepository = statesRepository;
		this.countriesRepository = countriesRepository;
		this.mediaService = mediaService;
		this.notificationsService = notificationsService;
		this.template = template;
        this.addressBookRepository = addressBookRepository;
    }

	//TODO: REVISAR SI LA PAGINACIÓN ESTÁ BIEN HECHA YA QUE NO ESTÁ COMO EN LAS OTRAS
	@Override
	public Page<PatientDTO> getPatients(String idpatient, String name_surnames, String documentid, String status,
										String gender, String servicetype, Boolean transportservice, Boolean tallerpsico,
										Boolean comedorservice, Boolean ayudadomicilioservice, Boolean hs_ley_dependencia_solicitada,
										Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = new Query()
				.addCriteria(Criteria.where("roles").in("PATIENT"))
				.with(pageable)
				.with(Sort.by(orderasc.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, order));

		if (idpatient != null)
			query.addCriteria(Criteria.where("_id").is(idpatient));

		if (name_surnames != null) {
			Criteria namesOrCriteria = new Criteria().orOperator(
					Criteria.where("name").regex(".*" + name_surnames + ".*", "i"),
					Criteria.where("surname1").regex(".*" + name_surnames + ".*", "i"),
					Criteria.where("surname2").regex(".*" + name_surnames + ".*", "i")
			);
			query.addCriteria(namesOrCriteria);
		}

		if (documentid != null)
			query.addCriteria(Criteria.where("documentid").is(documentid));

		if (status != null)
			query.addCriteria(Criteria.where("status").is(status));

		if (gender != null)
			query.addCriteria(Criteria.where("gender").is(gender));

		if (servicetype != null)
			query.addCriteria(Criteria.where("servicetype").is(servicetype));

		if (transportservice != null)
			query.addCriteria(Criteria.where("transportservice").is(transportservice));

		if (tallerpsico != null)
			query.addCriteria(Criteria.where("tallerpsico").is(tallerpsico));

		if (comedorservice != null)
			query.addCriteria(Criteria.where("comedorservice").is(comedorservice));

		if (ayudadomicilioservice != null)
			query.addCriteria(Criteria.where("ayudadomicilioservice").is(ayudadomicilioservice));

		if (hs_ley_dependencia_solicitada != null)
			query.addCriteria(Criteria.where("hs_ley_dependencia_solicitada").is(hs_ley_dependencia_solicitada));

		if (debug_queries)
			System.out.println("getPatients: " + query.getQueryObject().toJson());

		List<PatientDTO> list = this.mongoTemplate.find(query, User.class).stream()
				.map(x -> new PatientDTO(x, null, null, null, this.usersRepository.findOne(x.getIdrelative()), null))
				.toList();

		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> this.mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
	}


	String getCDNURL(String documenttemplate, String patient_id, HashMap<String, String> values) throws Exception
	{
		String body = template.parse(documenttemplate + ".html", values);

		String cdnurl = null;
		String fileName = documenttemplate + "-" + UUID.randomUUID() + ".html";
		String wholePath = mediapath + File.separator + fileName;

		FileUtils.string2File(body, wholePath, "UTF-8");

		try { cdnurl = this.mediaService.uploadFileFTP( documenttemplate + "-" + patient_id, fileName,  new FileInputStream(wholePath)); } catch (Exception e) { throw new Exception("ERROR: Cannot upload the file " + fileName);  }

		try { File f = new File(wholePath); f.delete(); } catch (Exception e) { e.printStackTrace(); }

		return cdnurl;
	}

	@Override
	public PatientDTO savePatient(String id, String name, String surname1, String surname2, LocalDate birthdate, String gender, String documentid, String documenttype,
								  String idrelative, String relativerelation,
								  String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
								  String num_contrato, String fs_num_expediente, LocalDate fs_fecha_inscripcion, String fs_num_ss, String fs_estado_civil, String phone,
								  String servicetype, Boolean tallerpsico, Boolean transportservice, String transportservice_text, Boolean comedorservice, String comedorservice_text, Boolean ayudadomicilioservice, String ayudadomicilioservice_text, Boolean duchaservice, String duchaservice_text,
								  String register_document_url, String register_document_url_signed,
								  String register19_document_url, String register19_document_url_signed,
								  String register20_document_url, String register20_document_url_signed,
								  String register21_document_url, String register21_document_url_signed,
								  String register22_document_url, String register22_document_url_signed,
								  String register23_document_url, String register23_document_url_signed,
								  String register24_document_url, String register24_document_url_signed,
								  String register25_document_url, String register25_document_url_signed,
								  String register26_document_url, String register26_document_url_signed,
								  String register27_document_url, String register27_document_url_signed,
								  String register28_document_url, String register28_document_url_signed,
								  String register29_document_url, String register29_document_url_signed,
								  String register30_document_url, String register30_document_url_signed
	) throws Exception
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

			patient.setRoles(Arrays.asList("PATIENT"));

			patient = this.usersRepository.save(patient);
		}
		patient.setRegister_document_url(register_document_url);
		patient.setRegister_document_url_signed(register_document_url_signed);

		if(register_document_url_signed!=null && register_document_url_signed.startsWith("https://"))
			patient.setStatus("A");
		else
			patient.setStatus("P");

		patient.setName(name);
		patient.setSurname1(surname1);
		patient.setSurname2(surname2);
		patient.setBirthdate(birthdate);
		patient.setGender(gender);
		patient.setDocumentid(documentid);
		patient.setDocumenttype(documenttype);

		patient.setIdrelative(idrelative);
		patient.setRelativerelation(relativerelation);

		patient.setIdcity(idcity);
		patient.setIdstate(idstate);
		patient.setIdcountry(idcountry);
		patient.setPostaladdress(postaladdress);
		patient.setPostalcode(postalcode);

		if(num_contrato==null || num_contrato.trim().equals(""))
		{
			String last_num_contrato = this.usersRepository.findHighestNumContrato();
			if(last_num_contrato==null || last_num_contrato.trim().equals("")) last_num_contrato = "1";
			num_contrato = StringUtils.incrementLastNumber(last_num_contrato);
			patient.setNum_contrato(num_contrato);
		}
		else
		{
			num_contrato = StringUtils.numberFoString(num_contrato);
			patient.setNum_contrato(num_contrato);
		}

		if(fs_num_expediente==null || fs_num_expediente.trim().equals(""))
		{
			String last_fs_num_expediente = this.usersRepository.findHighestFsNumExpediente();
			if(last_fs_num_expediente==null || last_fs_num_expediente.trim().equals("")) last_fs_num_expediente = "1";
			fs_num_expediente = StringUtils.incrementLastNumber(last_fs_num_expediente);
			patient.setFs_num_expediente(fs_num_expediente);
		}
		else
		{
			fs_num_expediente = StringUtils.numberFoString(fs_num_expediente);
			patient.setFs_num_expediente(fs_num_expediente);
		}

		patient.setFs_fecha_inscripcion(fs_fecha_inscripcion);
		patient.setFs_num_ss(fs_num_ss);
		patient.setFs_estado_civil(fs_estado_civil);
		patient.setPhone(phone);

		patient.setServicetype(servicetype);

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
			patient.setDuchaservice_text(duchaservice_text);
		else
			patient.setDuchaservice_text("");

		if(patient.getServicetype().equals("CENTRO_DIA_CONCERTADO"))
		{
			if(register19_document_url==null || register19_document_url.equals(""))
			{
				register19_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register19_document_url = this.getCDNURL("register19", patient.get_id(), values);
			}
			patient.setRegister19_document_url(register19_document_url);
			patient.setRegister19_document_url_signed(register19_document_url_signed);

			if(register20_document_url==null || register20_document_url.equals(""))
			{
				register20_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register20_document_url = this.getCDNURL("register20", patient.get_id(), values);
			}
			patient.setRegister20_document_url(register20_document_url);
			patient.setRegister20_document_url_signed(register20_document_url_signed);

			if(register24_document_url==null || register24_document_url.equals(""))
			{
				register24_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register24_document_url = this.getCDNURL("register24", patient.get_id(), values);
			}
			patient.setRegister24_document_url(register24_document_url);
			patient.setRegister24_document_url_signed(register24_document_url_signed);

			if(register25_document_url==null || register25_document_url.equals(""))
			{
				register25_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register25_document_url = this.getCDNURL("register25", patient.get_id(), values);
			}
			patient.setRegister25_document_url(register25_document_url);
			patient.setRegister25_document_url_signed(register25_document_url_signed);

			if(register26_document_url==null || register26_document_url.equals(""))
			{
				register26_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register26_document_url = this.getCDNURL("register26", patient.get_id(), values);
			}
			patient.setRegister26_document_url(register26_document_url);
			patient.setRegister26_document_url_signed(register26_document_url_signed);

			if(register27_document_url==null || register27_document_url.equals(""))
			{
				register27_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register27_document_url = this.getCDNURL("register27", patient.get_id(), values);
			}
			patient.setRegister27_document_url(register27_document_url);
			patient.setRegister27_document_url_signed(register27_document_url_signed);

		}
		else if(patient.getServicetype().equals("CENTRO_DIA_PRIVADO"))
		{
			if(register21_document_url==null || register21_document_url.equals(""))
			{
				register21_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register21_document_url = this.getCDNURL("register21", patient.get_id(), values);
			}
			patient.setRegister21_document_url(register21_document_url);
			patient.setRegister21_document_url_signed(register21_document_url_signed);

			/*
			if(register22_document_url==null || register22_document_url.equals(""))
			{
				register22_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register22_document_url = this.getCDNURL("register22", patient.get_id(), values);
			}
			patient.setRegister22_document_url(register22_document_url);
			patient.setRegister22_document_url_signed(register22_document_url_signed);*/

			if(register23_document_url==null || register23_document_url.equals(""))
			{
				register23_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register23_document_url = this.getCDNURL("register23", patient.get_id(), values);
			}
			patient.setRegister23_document_url(register23_document_url);
			patient.setRegister23_document_url_signed(register23_document_url_signed);


			if(register28_document_url==null || register28_document_url.equals(""))
			{
				register28_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register28_document_url = this.getCDNURL("register28", patient.get_id(), values);
			}
			patient.setRegister28_document_url(register28_document_url);
			patient.setRegister28_document_url_signed(register28_document_url_signed);
		}
		else if(patient.getServicetype().equals("TALLER"))
		{
			if(register25_document_url==null || register25_document_url.equals(""))
			{
				register25_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register25_document_url = this.getCDNURL("register25", patient.get_id(), values);
			}
			patient.setRegister25_document_url(register25_document_url);
			patient.setRegister25_document_url_signed(register25_document_url_signed);

			if(register26_document_url==null || register26_document_url.equals(""))
			{
				register26_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register26_document_url = this.getCDNURL("register26", patient.get_id(), values);
			}
			patient.setRegister26_document_url(register26_document_url);
			patient.setRegister26_document_url_signed(register26_document_url_signed);

			if(register28_document_url==null || register28_document_url.equals(""))
			{
				register28_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register28_document_url = this.getCDNURL("register28", patient.get_id(), values);
			}
			patient.setRegister28_document_url(register28_document_url);
			patient.setRegister28_document_url_signed(register28_document_url_signed);

			if(patient.getLanguage().equals("ES") && ( register29_document_url==null || register29_document_url.equals("") ))
			{
				register29_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register29_document_url = this.getCDNURL("register29", patient.get_id(), values);
			}
			patient.setRegister29_document_url(register29_document_url);
			patient.setRegister29_document_url_signed(register29_document_url_signed);

			if(patient.getLanguage().equals("EN") && ( register30_document_url==null || register30_document_url.equals("") ))
			{
				register30_document_url_signed = null;

				HashMap<String, String> values = new HashMap<String, String>();
				//values.put("var", var);
				register30_document_url = this.getCDNURL("register30", patient.get_id(), values);
			}
			patient.setRegister30_document_url(register30_document_url);
			patient.setRegister30_document_url_signed(register30_document_url_signed);

		}

		patient = this.usersRepository.save(patient);

		//ACTUALIZAMOS AGENDA
		try
		{
			List<AddressBook> addressBooks = this.addressBookRepository.findByIduser(patient.get_id());
			if(addressBooks!=null && addressBooks.size()>0)
				for(AddressBook addressBook:addressBooks)
				{
					addressBook.setFullname(patient.getFullname());
					addressBook.setPhone(patient.getPhone());
					addressBook.setEmail(patient.getEmail());
					this.addressBookRepository.save(addressBook);
				}
			else
			{
				AddressBook addressBook = new AddressBook(patient);
				this.addressBookRepository.save(addressBook);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		return new PatientDTO(patient, this.citiesRepository.findOne(idcity), this.statesRepository.findOne(idstate), this.countriesRepository.findOne(idcountry), this.usersRepository.findOne(patient.getIdrelative()), null);
	}



	@Override
	public PatientDTO savePatientFichaSocial(String id,

											 Boolean transportservice,

											 Boolean fs_talleres_estimulacion,

											 Boolean fs_gradior_stimmulus,

											 Boolean fs_sad,

											 Boolean fs_other,

											 String fs_other_text,

											 Boolean fs_comer_solo,

											 Boolean fs_lavarse_solo,

											 Boolean fs_salir_sin_perderse,

											 Boolean fs_reconocer_caras,

											 Boolean fs_leer_y_escribir,

											 Boolean fs_incontenencia_urinaria,

											 Boolean fs_conversar,

											 Boolean fs_reconocer_objetos_cotidianos,

											 Boolean fs_sufrir_alucinaciones,

											 Boolean fs_fases_agitacion,

											 Boolean fs_dificultad_orientarse,

											 String fs_movilizarse,

											 String fs_datos_medicos,

											 Boolean fs_grado_minusvalia,

											 String fs_grado_minusvalia_text,

											 Boolean fs_grado_dependencia,

											 String fs_grado_dependencia_text,

											 Boolean fs_incapacitacion_judicial,

											 Boolean fs_ayudas_externas,

											 String fs_ayudas_externas_text

	) throws Exception
	{
		User patient = this.usersRepository.findOne(id);
		if(patient==null) return null;

		patient.setTransportservice(transportservice);
		patient.setFs_talleres_estimulacion(fs_talleres_estimulacion==null?false:fs_talleres_estimulacion);
		patient.setFs_gradior_stimmulus(fs_gradior_stimmulus==null?false:fs_gradior_stimmulus);
		patient.setFs_sad(fs_sad==null?false:fs_sad);
		patient.setFs_other(fs_other==null?false:fs_other);

		patient.setFs_other_text(fs_other_text);

		patient.setFs_comer_solo(fs_comer_solo==null?false:fs_comer_solo);
		patient.setFs_lavarse_solo(fs_lavarse_solo==null?false:fs_lavarse_solo);
		patient.setFs_salir_sin_perderse(fs_salir_sin_perderse==null?false:fs_salir_sin_perderse);
		patient.setFs_reconocer_caras(fs_reconocer_caras==null?false:fs_reconocer_caras);
		patient.setFs_leer_y_escribir(fs_leer_y_escribir==null?false:fs_leer_y_escribir);
		patient.setFs_incontenencia_urinaria(fs_incontenencia_urinaria==null?false:fs_incontenencia_urinaria);
		patient.setFs_conversar(fs_conversar==null?false:fs_conversar);
		patient.setFs_reconocer_objetos_cotidianos(fs_reconocer_objetos_cotidianos==null?false:fs_reconocer_objetos_cotidianos);
		patient.setFs_sufrir_alucinaciones(fs_sufrir_alucinaciones==null?false:fs_sufrir_alucinaciones);
		patient.setFs_fases_agitacion(fs_fases_agitacion==null?false:fs_fases_agitacion);
		patient.setFs_dificultad_orientarse(fs_dificultad_orientarse==null?false:fs_dificultad_orientarse);

		patient.setFs_movilizarse(fs_movilizarse);
		patient.setFs_datos_medicos(fs_datos_medicos);

		patient.setFs_grado_minusvalia(fs_grado_minusvalia==null?false:fs_grado_minusvalia);
		patient.setFs_grado_minusvalia_text(fs_grado_minusvalia_text);
		patient.setFs_grado_dependencia(fs_grado_dependencia==null?false:fs_grado_dependencia);
		patient.setFs_grado_dependencia_text(fs_grado_dependencia_text);
		patient.setFs_incapacitacion_judicial(fs_incapacitacion_judicial==null?false:fs_incapacitacion_judicial);
		patient.setFs_ayudas_externas(fs_ayudas_externas==null?false:fs_ayudas_externas);
		patient.setFs_ayudas_externas_text(fs_ayudas_externas_text);


		patient = this.usersRepository.save(patient);
		User relative = this.usersRepository.findOne(patient.getIdrelative());
		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());

		HashMap<String, String> values = new HashMap<String, String>();

		values.put("FS_NUM_EXPEDIENTE", patient.getFs_num_expediente());
		values.put("FS_FECHA_INSCRIPCION", patient.getFs_fecha_inscripcion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("AGE", Period.between(patient.getBirthdate(), LocalDate.now()).getYears()+"");
		values.put("DOCUMENTID", patient.getDocumentid());
		values.put("FS_NUM_SS", patient.getFs_num_ss());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());
		values.put("RELATIVEFULLNAME", relative!=null?relative.getFullname() + " (" + patient.getRelativerelation() + ")":"");
		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city!=null?city.getName():"");
		values.put("STATENAME", state!=null?state.getName():"");
		values.put("PHONE", patient.getPhone());

		String services = "";
		if(patient.getFs_talleres_estimulacion()) services += "<li>Talleres de Psicoestimulación</li>";
		if(patient.getTransportservice()) services += "<li>Servicio de Transporte</li>";
		if(patient.getFs_gradior_stimmulus()) services += "<li>Gradior/Stimmulus</li>";
		if(patient.getFs_sad()) services += "<li>S.A.D.</li>";
		if(patient.getFs_other()) services += "<li>Otros: " + patient.getFs_other_text() + "</li>";
		values.put("SERVICES", services);

		String user_evaluation = "";
		if(patient.getFs_comer_solo()) user_evaluation += "<li>Puede comer sólo</li>";
		if(patient.getFs_lavarse_solo()) user_evaluation += "<li>Puede lavarse sólo  </li>";
		if(patient.getFs_salir_sin_perderse()) user_evaluation += "<li>Puede salir sin perderse</li>";
		if(patient.getFs_reconocer_caras()) user_evaluation += "<li>Puede reconocer caras</li>";
		if(patient.getFs_leer_y_escribir()) user_evaluation += "<li>Puede escribir y leer</li>";
		if(patient.getFs_conversar()) user_evaluation += "<li>Puede conversar</li>";
		if(patient.getFs_reconocer_objetos_cotidianos()) user_evaluation += "<li>Puede reconocer objetos cotidianos</li>";
		if(patient.getFs_incontenencia_urinaria()) user_evaluation += "<li>Tiene incontinencia urinaria</li>";
		if(patient.getFs_dificultad_orientarse()) user_evaluation += "<li>Tiene dificultad para orientarse en espacio tiempo</li>";
		if(patient.getFs_sufrir_alucinaciones()) user_evaluation += "<li>Ha sufrido alucinaciones</li>";
		if(patient.getFs_fases_agitacion()) user_evaluation += "<li>Ha pasado por fases de agitación y/o conducta violenta (Verbal o Física)</li>";
		if(!patient.getFs_movilizarse().equals("")) user_evaluation += "<li>Puede movilizarse como: " + patient.getFs_movilizarse() + "</li>";
		values.put("USER_EVALUATION", user_evaluation);

		values.put("FS_DATOS_MEDICOS", patient.getFs_datos_medicos());

		String user_social_procedures = "";
		if(patient.getFs_grado_minusvalia()) user_social_procedures += "<li>Grado de minusvalía: " + patient.getFs_grado_minusvalia_text() + "</li>";
		if(patient.getFs_grado_dependencia()) user_social_procedures += "<li>Grado de dependencia: " + patient.getFs_grado_dependencia_text() + "</li>";
		if(patient.getFs_incapacitacion_judicial()) user_social_procedures += "<li>Tiene incapacitación judicial</li>";
		if(patient.getFs_ayudas_externas()) user_social_procedures += "<li>Ayudas externas: " + patient.getFs_ayudas_externas_text() + "</li>";
		values.put("USER_SOCIAL_PROCEDURES", user_social_procedures);

		//fs_url no va a ser un campo de la entidad si no una variable del DTO
		String fs_url = this.getCDNURL("ficha_social", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);

		res.setFs_url(fs_url);
		return res;
	}




	@Override
	public PatientDTO savePatientHistoriaSocial(String id,
												Boolean hs_beca,
												String hs_diagnostico,
												Boolean hs_autonomia,
												Boolean hs_ayuda_abd,
												Boolean hs_uc_solo,
												Boolean hs_uc_conyuge,
												Boolean hs_uc_hijos,
												Boolean hs_uc_other,
												String hs_uc_other_text,
												String hs_nivel_formativo,
												String hs_interaccion_demas,
												String hs_interaccion_profesioneales,
												String hs_participacion_actividades,
												String hs_integracion_dinamica,
												String hs_grado_minusvalia_tipo,
												String hs_grado_minusvalia_cuando,
												Boolean hs_ley_dependencia_solicitada,
												String hs_ley_dependencia_grado,
												Boolean hs_recibe_servicio_administracion,
												String hs_patologias,
												Boolean hs_diabetico,
												Boolean hs_hipertenso,
												Boolean hs_alimenta_bien,
												Boolean hs_duerme_bien,
												Boolean hs_fuma_bebe,
												Boolean hs_drogas,
												String hs_drogas_text,
												String hs_valoracion_salud,
												Boolean hs_fam_dificultades_convivencia,
												Boolean hs_fam_dificultades_economicas,
												Boolean hs_fam_dificultad_cuidados,
												Boolean hs_fam_sin_apoyo,
												Boolean hs_fam_agotamiento_cuidador,
												Boolean hs_viv_sin_domicilio,
												Boolean hs_viv_ruinas,
												Boolean hs_viv_barreras,
												Boolean hs_viv_inhabitabilidad,
												Boolean hs_alquiler_elevado,
												Boolean hs_escaleras_exteriores,
												Boolean hs_escaleras_interiores,
												Boolean hs_banera,
												Boolean hs_alfombras,
												Boolean hs_otros,
												String hs_otros_text,
												String hs_nombre1,
												String hs_parentesco1,
												Integer hs_edad1,
												String hs_profesion1,
												String hs_nombre2,
												String hs_parentesco2,
												Integer hs_edad2,
												String hs_profesion2,
												String hs_nombre3,
												String hs_parentesco3,
												Integer hs_edad3,
												String hs_profesion3,
												String hs_nombre4,
												String hs_parentesco4,
												Integer hs_edad4,
												String hs_profesion4,
												Boolean hs_tiene_pareja,
												String hs_relacion_pareja,
												Boolean hs_tiene_hijos,
												String hs_relacion_hijos,
												Boolean hs_tiene_hermanos,
												String hs_relacion_hermanos,
												Boolean hs_visitas_familiares,
												String hs_visitas_cuanto,
												Boolean hs_apoyo_amigos,
												Boolean hs_relacion_familia,
												Boolean hs_acude_otras,
												Boolean hs_recibe_pension,
												Double hs_cuantia_pension,
												String hs_otra_prestacion,
												String hs_otros_ingresos,
												String hs_otros_recursos,
												String hs_valoracion_profesional,
												String hs_observaciones

	) throws Exception
	{
		User patient = this.usersRepository.findOne(id);
		if(patient==null) return null;

		// Boolean variables
		patient.setHs_beca(hs_beca == null ? false : hs_beca);
		patient.setHs_autonomia(hs_autonomia == null ? false : hs_autonomia);
		patient.setHs_ayuda_abd(hs_ayuda_abd == null ? false : hs_ayuda_abd);
		patient.setHs_uc_solo(hs_uc_solo == null ? false : hs_uc_solo);
		patient.setHs_uc_conyuge(hs_uc_conyuge == null ? false : hs_uc_conyuge);
		patient.setHs_uc_hijos(hs_uc_hijos == null ? false : hs_uc_hijos);
		patient.setHs_uc_other(hs_uc_other == null ? false : hs_uc_other);
		patient.setHs_ley_dependencia_solicitada(hs_ley_dependencia_solicitada == null ? false : hs_ley_dependencia_solicitada);
		patient.setHs_recibe_servicio_administracion(hs_recibe_servicio_administracion == null ? false : hs_recibe_servicio_administracion);
		patient.setHs_diabetico(hs_diabetico == null ? false : hs_diabetico);
		patient.setHs_hipertenso(hs_hipertenso == null ? false : hs_hipertenso);
		patient.setHs_alimenta_bien(hs_alimenta_bien == null ? false : hs_alimenta_bien);
		patient.setHs_duerme_bien(hs_duerme_bien == null ? false : hs_duerme_bien);
		patient.setHs_fuma_bebe(hs_fuma_bebe == null ? false : hs_fuma_bebe);
		patient.setHs_drogas(hs_drogas == null ? false : hs_drogas);
		patient.setHs_fam_dificultades_convivencia(hs_fam_dificultades_convivencia == null ? false : hs_fam_dificultades_convivencia);
		patient.setHs_fam_dificultades_economicas(hs_fam_dificultades_economicas == null ? false : hs_fam_dificultades_economicas);
		patient.setHs_fam_dificultad_cuidados(hs_fam_dificultad_cuidados == null ? false : hs_fam_dificultad_cuidados);
		patient.setHs_fam_sin_apoyo(hs_fam_sin_apoyo == null ? false : hs_fam_sin_apoyo);
		patient.setHs_fam_agotamiento_cuidador(hs_fam_agotamiento_cuidador == null ? false : hs_fam_agotamiento_cuidador);
		patient.setHs_viv_sin_domicilio(hs_viv_sin_domicilio == null ? false : hs_viv_sin_domicilio);
		patient.setHs_viv_ruinas(hs_viv_ruinas == null ? false : hs_viv_ruinas);
		patient.setHs_viv_barreras(hs_viv_barreras == null ? false : hs_viv_barreras);
		patient.setHs_viv_inhabitabilidad(hs_viv_inhabitabilidad == null ? false : hs_viv_inhabitabilidad);
		patient.setHs_alquiler_elevado(hs_alquiler_elevado == null ? false : hs_alquiler_elevado);
		patient.setHs_escaleras_exteriores(hs_escaleras_exteriores == null ? false : hs_escaleras_exteriores);
		patient.setHs_escaleras_interiores(hs_escaleras_interiores == null ? false : hs_escaleras_interiores);
		patient.setHs_banera(hs_banera == null ? false : hs_banera);
		patient.setHs_alfombras(hs_alfombras == null ? false : hs_alfombras);
		patient.setHs_otros(hs_otros == null ? false : hs_otros);
		patient.setHs_tiene_pareja(hs_tiene_pareja == null ? false : hs_tiene_pareja);
		patient.setHs_tiene_hijos(hs_tiene_hijos == null ? false : hs_tiene_hijos);
		patient.setHs_tiene_hermanos(hs_tiene_hermanos == null ? false : hs_tiene_hermanos);
		patient.setHs_visitas_familiares(hs_visitas_familiares == null ? false : hs_visitas_familiares);
		patient.setHs_apoyo_amigos(hs_apoyo_amigos == null ? false : hs_apoyo_amigos);
		patient.setHs_relacion_familia(hs_relacion_familia == null ? false : hs_relacion_familia);
		patient.setHs_acude_otras(hs_acude_otras == null ? false : hs_acude_otras);
		patient.setHs_recibe_pension(hs_recibe_pension == null ? false : hs_recibe_pension);

		// String variables
		patient.setHs_uc_other_text(hs_uc_other == null || hs_uc_other == false ? null : hs_uc_other_text);
		patient.setHs_otros_text(hs_otros == null || hs_otros == false ? null : hs_otros_text);
		patient.setHs_ley_dependencia_grado(hs_ley_dependencia_solicitada == null || hs_ley_dependencia_solicitada == false? "" : hs_ley_dependencia_grado);

		patient.setHs_diagnostico(hs_diagnostico);
		patient.setHs_nivel_formativo(hs_nivel_formativo);
		patient.setHs_interaccion_demas(hs_interaccion_demas);
		patient.setHs_interaccion_profesioneales(hs_interaccion_profesioneales);
		patient.setHs_participacion_actividades(hs_participacion_actividades);
		patient.setHs_integracion_dinamica(hs_integracion_dinamica);
		patient.setHs_grado_minusvalia_tipo(hs_grado_minusvalia_tipo);
		patient.setHs_grado_minusvalia_cuando(hs_grado_minusvalia_cuando);
		patient.setHs_patologias(hs_patologias);
		patient.setHs_drogas_text(hs_drogas_text);
		patient.setHs_valoracion_salud(hs_valoracion_salud);
		patient.setHs_visitas_cuanto(hs_visitas_cuanto);
		patient.setHs_relacion_pareja(hs_relacion_pareja);
		patient.setHs_relacion_hijos(hs_relacion_hijos);
		patient.setHs_relacion_hermanos(hs_relacion_hermanos);
		patient.setHs_nombre1(hs_nombre1);
		patient.setHs_parentesco1(hs_parentesco1);
		patient.setHs_profesion1(hs_profesion1);
		patient.setHs_nombre2(hs_nombre2);
		patient.setHs_parentesco2(hs_parentesco2);
		patient.setHs_profesion2(hs_profesion2);
		patient.setHs_nombre3(hs_nombre3);
		patient.setHs_parentesco3(hs_parentesco3);
		patient.setHs_profesion3(hs_profesion3);
		patient.setHs_nombre4(hs_nombre4);
		patient.setHs_parentesco4(hs_parentesco4);
		patient.setHs_profesion4(hs_profesion4);
		patient.setHs_otra_prestacion(hs_otra_prestacion);
		patient.setHs_otros_ingresos(hs_otros_ingresos);
		patient.setHs_otros_recursos(hs_otros_recursos);
		patient.setHs_valoracion_profesional(hs_valoracion_profesional);
		patient.setHs_observaciones(hs_observaciones);

		// Integer variables
		patient.setHs_edad1(hs_edad1);
		patient.setHs_edad2(hs_edad2);
		patient.setHs_edad3(hs_edad3);
		patient.setHs_edad4(hs_edad4);

		// Double variables
		patient.setHs_cuantia_pension(hs_cuantia_pension);

		patient = this.usersRepository.save(patient);
		User relative = this.usersRepository.findOne(patient.getIdrelative());
		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());

		HashMap<String, String> values = new HashMap<String, String>();

		values.put("FS_FECHA_INSCRIPCION", patient.getFs_fecha_inscripcion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("AGE", Period.between(patient.getBirthdate(), LocalDate.now()).getYears()+"");
		values.put("DOCUMENTID", patient.getDocumentid());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());
		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city!=null?city.getName():"");
		values.put("STATENAME", state!=null?state.getName():"");
		values.put("NATIONALITY", patient.getNationality());

		values.put("fs_grado_minusvalia", patient.getFs_grado_minusvalia()?"SÍ":"NO");
		values.put("fs_grado_minusvalia_text", patient.getFs_grado_minusvalia_text());

		values.put("hs_beca", patient.getHs_beca()?"SÍ":"NO");
		values.put("hs_diagnostico", patient.getHs_diagnostico());
		values.put("hs_autonomia", patient.getHs_autonomia()?"SÍ":"NO");
		values.put("hs_ayuda_abd", patient.getHs_ayuda_abd()?"SÍ":"NO");
		values.put("RELATIVEFULLNAME", relative!=null?relative.getFullname() + " (" + patient.getRelativerelation() + ")":"");

		String unidad_covivencia = "";
		if(patient.getHs_uc_solo()) unidad_covivencia += "<li>Solo/a</li>";
		if(patient.getHs_uc_conyuge()) unidad_covivencia += "<li>Cónyuge</li>";
		if(patient.getHs_uc_hijos()) unidad_covivencia += "<li>Hijos u otros parientes</li>";
		if(patient.getHs_uc_other()) unidad_covivencia += "<li>Otros: " + patient.getHs_uc_other_text() + "</li>";
		values.put("unidad_covivencia", unidad_covivencia);

		values.put("hs_nivel_formativo", patient.getHs_nivel_formativo());

		values.put("hs_interaccion_demas", patient.getHs_interaccion_demas());
		values.put("hs_interaccion_profesioneales", patient.getHs_interaccion_profesioneales());
		values.put("hs_participacion_actividades", patient.getHs_participacion_actividades());
		values.put("hs_integracion_dinamica", patient.getHs_integracion_dinamica());
		values.put("hs_grado_minusvalia_tipo", patient.getHs_grado_minusvalia_tipo());
		values.put("hs_grado_minusvalia_cuando", patient.getHs_grado_minusvalia_cuando());

		values.put("hs_ley_dependencia_solicitada", patient.getHs_ley_dependencia_solicitada()?"SÍ":"NO");
		values.put("hs_ley_dependencia_grado", patient.getHs_ley_dependencia_grado());

		values.put("hs_recibe_servicio_administracion", patient.getHs_recibe_servicio_administracion()?"SÍ":"NO");
		values.put("hs_patologias", patient.getHs_patologias());
		values.put("hs_diabetico", patient.getHs_diabetico()?"SÍ":"NO");
		values.put("hs_hipertenso", patient.getHs_hipertenso()?"SÍ":"NO");
		values.put("hs_alimenta_bien", patient.getHs_alimenta_bien()?"SÍ":"NO");
		values.put("hs_duerme_bien", patient.getHs_duerme_bien()?"SÍ":"NO");
		values.put("hs_fuma_bebe", patient.getHs_fuma_bebe()?"SÍ":"NO");
		values.put("hs_drogas", patient.getHs_drogas()?"SÍ":"NO");
		values.put("hs_drogas_text", patient.getHs_drogas_text());
		values.put("hs_valoracion_salud", patient.getHs_valoracion_salud());


		String user_familiar = "";
		if(patient.getHs_fam_dificultades_convivencia()) user_familiar += "<li>Dificultades para la convivencia</li>";
		if(patient.getHs_fam_dificultades_economicas()) user_familiar += "<li>Dificultades económicas</li>";
		if(patient.getHs_fam_dificultad_cuidados()) user_familiar += "<li>Dificultad de cuidados por problemas de salud del cuidador/a</li>";
		if(patient.getHs_fam_sin_apoyo()) user_familiar += "<li>Sin apoyo de otros familiares u otro apoyo social</li>";
		if(patient.getHs_fam_agotamiento_cuidador()) user_familiar += "<li>Agotamiento cuidador </li>";
		values.put("user_familiar", user_familiar);

		String user_vivienda = "";
		if(patient.getHs_viv_sin_domicilio()) user_vivienda += "<li>Sin domicilio</li>";
		if(patient.getHs_viv_ruinas()) user_vivienda += "<li>En ruinas o desahucio</li>";
		if(patient.getHs_viv_barreras()) user_vivienda += "<li>Barreras arquitectónicas</li>";
		if(patient.getHs_viv_inhabitabilidad()) user_vivienda += "<li>Inhabilitabilidad</li>";
		if(patient.getHs_alquiler_elevado()) user_vivienda += "<li>Alquiler elevado</li>";
		if(patient.getHs_escaleras_exteriores()) user_vivienda += "<li>Escaleras exteriores</li>";
		if(patient.getHs_escaleras_interiores()) user_vivienda += "<li>Escaleras interiores para acceso a patio</li>";
		if(patient.getHs_banera()) user_vivienda += "<li>Bañera</li>";
		if(patient.getHs_alfombras()) user_vivienda += "<li>Alfombras</li>";
		if(patient.getHs_otros()) user_vivienda += "<li>Otros: " + patient.getHs_otros_text() + "</li>";
		values.put("user_vivienda", user_vivienda);

		values.put("hs_nombre1", patient.getHs_nombre1());
		values.put("hs_nombre2", patient.getHs_nombre2());
		values.put("hs_nombre3", patient.getHs_nombre3());
		values.put("hs_nombre4", patient.getHs_nombre4());

		values.put("hs_parentesco1", patient.getHs_parentesco1());
		values.put("hs_parentesco2", patient.getHs_parentesco2());
		values.put("hs_parentesco3", patient.getHs_parentesco3());
		values.put("hs_parentesco4", patient.getHs_parentesco4());

		if(patient.getHs_edad1()>0) values.put("hs_edad1", patient.getHs_edad1()+" años"); else  values.put("hs_edad1", "");
		if(patient.getHs_edad1()>0) values.put("hs_edad2", patient.getHs_edad1()+" años"); else  values.put("hs_edad2", "");
		if(patient.getHs_edad1()>0) values.put("hs_edad3", patient.getHs_edad1()+" años"); else  values.put("hs_edad3", "");
		if(patient.getHs_edad1()>0) values.put("hs_edad4", patient.getHs_edad1()+" años"); else  values.put("hs_edad4", "");

		values.put("hs_profesion1", patient.getHs_profesion1());
		values.put("hs_profesion2", patient.getHs_profesion2());
		values.put("hs_profesion3", patient.getHs_profesion3());
		values.put("hs_profesion4", patient.getHs_profesion4());

		values.put("hs_tiene_pareja", patient.getHs_tiene_pareja()?"SÍ":"NO");
		values.put("fs_estado_civil", patient.getFs_estado_civil());
		values.put("hs_relacion_pareja", patient.getHs_relacion_pareja());
		values.put("hs_tiene_hijos", patient.getHs_tiene_hijos()?"SÍ":"NO");
		values.put("hs_relacion_hijos", patient.getHs_relacion_hijos());
		values.put("hs_tiene_hermanos", patient.getHs_tiene_hermanos()?"SÍ":"NO");
		values.put("hs_relacion_hermanos", patient.getHs_relacion_hermanos());

		values.put("hs_visitas_familiares", patient.getHs_visitas_familiares()?"SÍ":"NO");
		values.put("hs_visitas_cuanto", patient.getHs_visitas_cuanto());

		values.put("hs_apoyo_amigos", patient.getHs_apoyo_amigos()?"SÍ":"NO");
		values.put("hs_relacion_familia", patient.getHs_relacion_familia()?"SÍ":"NO");
		values.put("hs_acude_otras", patient.getHs_acude_otras()?"SÍ":"NO");

		values.put("hs_recibe_pension", patient.getHs_recibe_pension()?"SÍ":"NO");
		values.put("hs_cuantia_pension", patient.getHs_cuantia_pension()+"");

		values.put("hs_otros_ingresos", patient.getHs_otros_ingresos());
		values.put("hs_otra_prestacion", patient.getHs_otra_prestacion());
		values.put("hs_otros_recursos", patient.getHs_otros_recursos());
		values.put("hs_valoracion_profesional", patient.getHs_valoracion_profesional());
		values.put("hs_observaciones", patient.getHs_observaciones());

		//fs_url no va a ser un campo de la entidad si no una variable del DTO
		String hs_url = this.getCDNURL("historia_social", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);

		res.setHs_url(hs_url);
		return res;
	}

	@Override
	public PatientDTO savePatientInformeSocial(
			String id,
			String is_tiempo_conoce_usuario,
			String is_servicios_prestados,
			String is_como_adaptado,
			Boolean is_acudio_voluntad_propia,
			String is_quien_influyo_decision,
			String is_que_actividades,
			String is_como_relaciona,
			String is_como_pasa_dia,
			Boolean is_problemas_psico,
			String is_problemas_psico_text,
			Boolean is_recibe_tratamiento,
			String is_recibe_tratamiento_text,
			Boolean is_familia_estru,
			String is_familia_estru_text,
			Boolean is_recibe_ingresos_actividad_laboral,
			Boolean is_esta_buscando_empleo,
			String is_vive_en,
			Boolean is_cubiertas_necesidades_diarias,
			String is_valoracion_profesional,
			String is_propuesta
	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if(patient==null) return null;

		patient.setIs_tiempo_conoce_usuario(is_tiempo_conoce_usuario);
		patient.setIs_servicios_prestados(is_servicios_prestados);
		patient.setIs_como_adaptado(is_como_adaptado);
		patient.setIs_acudio_voluntad_propia(is_acudio_voluntad_propia == null ? false : is_acudio_voluntad_propia);
		patient.setIs_quien_influyo_decision(is_quien_influyo_decision);
		patient.setIs_que_actividades(is_que_actividades);
		patient.setIs_como_relaciona(is_como_relaciona);
		patient.setIs_como_pasa_dia(is_como_pasa_dia);
		patient.setIs_problemas_psico(is_problemas_psico == null ? false : is_problemas_psico);
		patient.setIs_problemas_psico_text(is_problemas_psico_text);
		patient.setIs_recibe_tratamiento(is_recibe_tratamiento == null ? false : is_recibe_tratamiento);
		patient.setIs_recibe_tratamiento_text(is_recibe_tratamiento_text);
		patient.setIs_familia_estru(is_familia_estru == null ? false : is_familia_estru);
		patient.setIs_familia_estru_text(is_familia_estru_text);
		patient.setIs_recibe_ingresos_actividad_laboral(is_recibe_ingresos_actividad_laboral == null ? false : is_recibe_ingresos_actividad_laboral);
		patient.setIs_esta_buscando_empleo(is_esta_buscando_empleo == null ? false : is_esta_buscando_empleo);
		patient.setIs_vive_en(is_vive_en);
		patient.setIs_cubiertas_necesidades_diarias(is_cubiertas_necesidades_diarias == null ? false : is_cubiertas_necesidades_diarias);
		patient.setIs_valoracion_profesional(is_valoracion_profesional);
		patient.setIs_propuesta(is_propuesta);

		patient = this.usersRepository.save(patient);
		User relative = this.usersRepository.findOne(patient.getIdrelative());
		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());

		HashMap<String, String> values = new HashMap<String, String>();

		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("AGE", Period.between(patient.getBirthdate(), LocalDate.now()).getYears()+"");
		values.put("DOCUMENTID", patient.getDocumentid());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());
		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city!=null?city.getName():"");
		values.put("STATENAME", state!=null?state.getName():"");
		values.put("NATIONALITY", patient.getNationality());

		values.put("is_tiempo_conoce_usuario", patient.getIs_tiempo_conoce_usuario());
		values.put("is_servicios_prestados", patient.getIs_servicios_prestados());
		values.put("is_como_adaptado", patient.getIs_como_adaptado());
		values.put("is_acudio_voluntad_propia", patient.getIs_acudio_voluntad_propia()?"SÍ":"NO");
		values.put("is_quien_influyo_decision", patient.getIs_quien_influyo_decision());
		values.put("is_que_actividades", patient.getIs_que_actividades());
		values.put("is_como_relaciona", patient.getIs_como_relaciona());
		values.put("is_como_pasa_dia", patient.getIs_como_pasa_dia());
		values.put("is_problemas_psico", patient.getIs_problemas_psico()?"SÍ":"NO");
		values.put("is_problemas_psico_text", patient.getIs_problemas_psico_text());
		values.put("is_familia_estru", patient.getIs_familia_estru()?"SÍ":"NO");
		values.put("is_familia_estru_text", patient.getIs_familia_estru_text());
		values.put("is_recibe_ingresos_actividad_laboral", patient.getIs_recibe_ingresos_actividad_laboral()?"SÍ":"NO");
		values.put("is_esta_buscando_empleo", patient.getIs_esta_buscando_empleo()?"SÍ":"NO");
		values.put("is_vive_en", patient.getIs_vive_en());
		values.put("is_cubiertas_necesidades_diarias", patient.getIs_cubiertas_necesidades_diarias()?"SÍ":"NO");
		values.put("is_valoracion_profesional", patient.getIs_valoracion_profesional());
		values.put("is_propuesta", patient.getIs_propuesta());

		values.put("fs_grado_minusvalia", patient.getFs_grado_minusvalia()?"SÍ":"NO");
		values.put("fs_grado_minusvalia_text", patient.getFs_grado_minusvalia_text());
		values.put("hs_grado_minusvalia_tipo", patient.getHs_grado_minusvalia_tipo());
		values.put("hs_grado_minusvalia_cuando", patient.getHs_grado_minusvalia_cuando());

		values.put("hs_patologias", patient.getHs_patologias());
		values.put("hs_ayuda_abd", patient.getHs_ayuda_abd()?"SÍ":"NO");
		values.put("hs_alimenta_bien", patient.getHs_alimenta_bien()?"SÍ":"NO");
		values.put("hs_duerme_bien", patient.getHs_duerme_bien()?"SÍ":"NO");
		values.put("hs_fuma_bebe", patient.getHs_fuma_bebe()?"SÍ":"NO");
		values.put("hs_drogas", patient.getHs_drogas()?"SÍ":"NO");
		values.put("hs_drogas_text", patient.getHs_drogas_text());
		values.put("hs_valoracion_salud", patient.getHs_valoracion_salud());


		values.put("hs_tiene_pareja", patient.getHs_tiene_pareja()?"SÍ":"NO");
		values.put("fs_estado_civil", patient.getFs_estado_civil());
		values.put("hs_relacion_pareja", patient.getHs_relacion_pareja());
		values.put("hs_tiene_hijos", patient.getHs_tiene_hijos()?"SÍ":"NO");
		values.put("hs_relacion_hijos", patient.getHs_relacion_hijos());
		values.put("hs_tiene_hermanos", patient.getHs_tiene_hermanos()?"SÍ":"NO");
		values.put("hs_relacion_hermanos", patient.getHs_relacion_hermanos());

		values.put("hs_visitas_familiares", patient.getHs_visitas_familiares()?"SÍ":"NO");
		values.put("hs_visitas_cuanto", patient.getHs_visitas_cuanto());

		values.put("hs_apoyo_amigos", patient.getHs_apoyo_amigos()?"SÍ":"NO");
		values.put("hs_relacion_familia", patient.getHs_relacion_familia()?"SÍ":"NO");
		values.put("hs_acude_otras", patient.getHs_acude_otras()?"SÍ":"NO");

		values.put("hs_nivel_formativo", patient.getHs_nivel_formativo());
		values.put("hs_otros_ingresos", patient.getHs_otros_ingresos());
		values.put("hs_otra_prestacion", patient.getHs_otra_prestacion());
		values.put("hs_otros_recursos", patient.getHs_otros_recursos());

		//is_url no va a ser un campo de la entidad si no una variable del DTO
		String is_url = this.getCDNURL("informe_social", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);

		res.setIs_url(is_url);
		return res;
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


	@Override
	public PatientDTO saveInformeNeuroPsicologico(
			String id,
			LocalDate ins_fecha_informe,
			String ins_motivo_consulta,
			String ins_antecedentes,
			String ins_diagnostico,
			String ins_texto_pre_puntuaciones,
			LocalDate ins_fecha1, LocalDate ins_fecha2, LocalDate ins_fecha3, LocalDate ins_fecha4,
			Double ins_orientacion1, Double ins_orientacion2, Double ins_orientacion3, Double ins_orientacion4,
			Double ins_lenguaje1, Double ins_lenguaje2, Double ins_lenguaje3, Double ins_lenguaje4,
			Double ins_memoria1, Double ins_memoria2, Double ins_memoria3, Double ins_memoria4,
			Double ins_atencalculo1, Double ins_atencalculo2, Double ins_atencalculo3, Double ins_atencalculo4,
			Double ins_praxis1, Double ins_praxis2, Double ins_praxis3, Double ins_praxis4,
			Double ins_pensabstracto1, Double ins_pensabstracto2, Double ins_pensabstracto3, Double ins_pensabstracto4,
			Double ins_percecpcion1, Double ins_percecpcion2, Double ins_percecpcion3, Double ins_percecpcion4,
			Double ins_total1, Double ins_total2, Double ins_total3, Double ins_total4,
			LocalDate ins_fecha_mms1, LocalDate ins_fecha_mms2, LocalDate ins_fecha_mms3, LocalDate ins_fecha_mms4,
			Double ins_mmse1, Double ins_mmse2, Double ins_mmse3, Double ins_mmse4,
			String ins_texto_post_puntuaciones,
			LocalDate ins_fecha_ind1, LocalDate ins_fecha_ind2, LocalDate ins_fecha_ind3,
			Double ins_indbathel1, Double ins_indbathel2, Double ins_indbathel3,
			Double ins_indlawton1, Double ins_indlawton2, Double ins_indlawton3,
			String ins_texto_eval_conductual,
			String ins_texto_conclusion

	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		patient.setIns_fecha_informe(ins_fecha_informe);
		patient.setIns_motivo_consulta(ins_motivo_consulta);
		patient.setIns_antecedentes(ins_antecedentes);
		patient.setIns_diagnostico(ins_diagnostico);
		patient.setIns_texto_pre_puntuaciones(ins_texto_pre_puntuaciones);

		patient.setIns_fecha1(ins_fecha1);
		patient.setIns_fecha2(ins_fecha2);
		patient.setIns_fecha3(ins_fecha3);
		patient.setIns_fecha4(ins_fecha4);

		patient.setIns_orientacion1(ins_orientacion1);
		patient.setIns_orientacion2(ins_orientacion2);
		patient.setIns_orientacion3(ins_orientacion3);
		patient.setIns_orientacion4(ins_orientacion4);

		patient.setIns_lenguaje1(ins_lenguaje1);
		patient.setIns_lenguaje2(ins_lenguaje2);
		patient.setIns_lenguaje3(ins_lenguaje3);
		patient.setIns_lenguaje4(ins_lenguaje4);

		patient.setIns_memoria1(ins_memoria1);
		patient.setIns_memoria2(ins_memoria2);
		patient.setIns_memoria3(ins_memoria3);
		patient.setIns_memoria4(ins_memoria4);

		patient.setIns_atencalculo1(ins_atencalculo1);
		patient.setIns_atencalculo2(ins_atencalculo2);
		patient.setIns_atencalculo3(ins_atencalculo3);
		patient.setIns_atencalculo4(ins_atencalculo4);

		patient.setIns_praxis1(ins_praxis1);
		patient.setIns_praxis2(ins_praxis2);
		patient.setIns_praxis3(ins_praxis3);
		patient.setIns_praxis4(ins_praxis4);

		patient.setIns_pensabstracto1(ins_pensabstracto1);
		patient.setIns_pensabstracto2(ins_pensabstracto2);
		patient.setIns_pensabstracto3(ins_pensabstracto3);
		patient.setIns_pensabstracto4(ins_pensabstracto4);

		patient.setIns_percecpcion1(ins_percecpcion1);
		patient.setIns_percecpcion2(ins_percecpcion2);
		patient.setIns_percecpcion3(ins_percecpcion3);
		patient.setIns_percecpcion4(ins_percecpcion4);

		patient.setIns_total1(ins_total1);
		patient.setIns_total2(ins_total2);
		patient.setIns_total3(ins_total3);
		patient.setIns_total4(ins_total4);

		patient.setIns_fecha_mms1(ins_fecha_mms1);
		patient.setIns_fecha_mms2(ins_fecha_mms2);
		patient.setIns_fecha_mms3(ins_fecha_mms3);
		patient.setIns_fecha_mms4(ins_fecha_mms4);

		patient.setIns_mmse1(ins_mmse1);
		patient.setIns_mmse2(ins_mmse2);
		patient.setIns_mmse3(ins_mmse3);
		patient.setIns_mmse4(ins_mmse4);

		patient.setIns_texto_post_puntuaciones(ins_texto_post_puntuaciones);

		patient.setIns_fecha_ind1(ins_fecha_ind1);
		patient.setIns_fecha_ind2(ins_fecha_ind2);
		patient.setIns_fecha_ind3(ins_fecha_ind3);

		patient.setIns_indbathel1(ins_indbathel1);
		patient.setIns_indbathel2(ins_indbathel2);
		patient.setIns_indbathel3(ins_indbathel3);

		patient.setIns_indlawton1(ins_indlawton1);
		patient.setIns_indlawton2(ins_indlawton2);
		patient.setIns_indlawton3(ins_indlawton3);

		patient.setIns_texto_eval_conductual(ins_texto_eval_conductual);
		patient.setIns_texto_conclusion(ins_texto_conclusion);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<String, String>();


		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city!=null?city.getName():"");
		values.put("STATENAME", state!=null?state.getName():"");

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("SEX", patient.getGender());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());

		values.put("OCUPACION_ANTERIOR", patient.getOcupacionAnterior());
		values.put("NIVEL_FORMATIVO", patient.getHs_nivel_formativo());


		values.put("ins_motivo_consulta", patient.getIns_motivo_consulta());
		values.put("ins_antecedentes", patient.getIns_antecedentes());
		values.put("ins_diagnostico", patient.getIns_diagnostico());
		values.put("ins_texto_pre_puntuaciones", patient.getIns_texto_pre_puntuaciones());
		values.put("ins_texto_post_puntuaciones", ""+ patient.getIns_texto_post_puntuaciones());
		values.put("ins_texto_eval_conductual", patient.getIns_texto_eval_conductual());
		values.put("ins_texto_conclusion", patient.getIns_texto_conclusion());

		try {  values.put("ins_fecha_informe", patient.getIns_fecha_informe().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_informe",""); }

		try { values.put("ins_fecha1", patient.getIns_fecha1().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha1",""); }
		try { values.put("ins_fecha2", patient.getIns_fecha2().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha2", ""); }
		try { values.put("ins_fecha3", patient.getIns_fecha3().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha3", ""); }
		try { values.put("ins_fecha4", patient.getIns_fecha4().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha4", ""); }

		try { values.put("ins_fecha_mms1", patient.getIns_fecha_mms1().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_mms1", ""); }
		try { values.put("ins_fecha_mms2", patient.getIns_fecha_mms2().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_mms2", ""); }
		try { values.put("ins_fecha_mms3", patient.getIns_fecha_mms3().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_mms3", ""); }
		try { values.put("ins_fecha_mms4", patient.getIns_fecha_mms4().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_mms4", ""); }

		try { values.put("ins_fecha_ind1", patient.getIns_fecha_ind1().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_ind1", ""); }
		try { values.put("ins_fecha_ind2", patient.getIns_fecha_ind2().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_ind2", ""); }
		try { values.put("ins_fecha_ind3", patient.getIns_fecha_ind3().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ins_fecha_ind3", ""); }

		if(patient.getIns_orientacion1()==null) values.put("ins_orientacion1", "");
		else values.put("ins_orientacion1", ""+ patient.getIns_orientacion1());

		if(patient.getIns_orientacion2() == null) values.put("ins_orientacion2", "");
		else values.put("ins_orientacion2", ""+ patient.getIns_orientacion2());

		if(patient.getIns_orientacion3() == null) values.put("ins_orientacion3", "");
		else values.put("ins_orientacion3", ""+ patient.getIns_orientacion3());

		if(patient.getIns_orientacion4() == null) values.put("ins_orientacion4", "");
		else values.put("ins_orientacion4", ""+ patient.getIns_orientacion4());

		if(patient.getIns_lenguaje1() == null) values.put("ins_lenguaje1", "");
		else values.put("ins_lenguaje1", ""+ patient.getIns_lenguaje1());

		if(patient.getIns_lenguaje2() == null) values.put("ins_lenguaje2", "");
		else values.put("ins_lenguaje2", ""+ patient.getIns_lenguaje2());

		if(patient.getIns_lenguaje3() == null) values.put("ins_lenguaje3", "");
		else values.put("ins_lenguaje3", ""+ patient.getIns_lenguaje3());

		if(patient.getIns_lenguaje4() == null) values.put("ins_lenguaje4", "");
		else values.put("ins_lenguaje4", ""+ patient.getIns_lenguaje4());

		if(patient.getIns_memoria1() == null) values.put("ins_memoria1", "");
		else values.put("ins_memoria1", ""+ patient.getIns_memoria1());

		if(patient.getIns_memoria2() == null) values.put("ins_memoria2", "");
		else values.put("ins_memoria2", ""+ patient.getIns_memoria2());

		if(patient.getIns_memoria3() == null) values.put("ins_memoria3", "");
		else values.put("ins_memoria3", ""+ patient.getIns_memoria3());

		if(patient.getIns_memoria4() == null) values.put("ins_memoria4", "");
		else values.put("ins_memoria4", ""+ patient.getIns_memoria4());

		if(patient.getIns_atencalculo1() == null) values.put("ins_atencalculo1", "");
		else values.put("ins_atencalculo1", ""+ patient.getIns_atencalculo1());

		if(patient.getIns_atencalculo2() == null) values.put("ins_atencalculo2", "");
		else values.put("ins_atencalculo2", ""+ patient.getIns_atencalculo2());

		if(patient.getIns_atencalculo3() == null) values.put("ins_atencalculo3", "");
		else values.put("ins_atencalculo3", ""+ patient.getIns_atencalculo3());

		if(patient.getIns_atencalculo4() == null) values.put("ins_atencalculo4", "");
		else values.put("ins_atencalculo4", ""+ patient.getIns_atencalculo4());

		if(patient.getIns_praxis1() == null) values.put("ins_praxis1", "");
		else values.put("ins_praxis1", ""+ patient.getIns_praxis1());

		if(patient.getIns_praxis2() == null) values.put("ins_praxis2", "");
		else values.put("ins_praxis2", ""+ patient.getIns_praxis2());

		if(patient.getIns_praxis3() == null) values.put("ins_praxis3", "");
		else values.put("ins_praxis3", ""+ patient.getIns_praxis3());

		if(patient.getIns_praxis4() == null) values.put("ins_praxis4", "");
		else values.put("ins_praxis4", ""+ patient.getIns_praxis4());

		if(patient.getIns_pensabstracto1() == null) values.put("ins_pensabstracto1", "");
		else values.put("ins_pensabstracto1", ""+ patient.getIns_pensabstracto1());

		if(patient.getIns_pensabstracto2() == null) values.put("ins_pensabstracto2", "");
		else values.put("ins_pensabstracto2", ""+ patient.getIns_pensabstracto2());

		if(patient.getIns_pensabstracto3() == null) values.put("ins_pensabstracto3", "");
		else values.put("ins_pensabstracto3", ""+ patient.getIns_pensabstracto3());

		if(patient.getIns_pensabstracto4() == null) values.put("ins_pensabstracto4", "");
		else values.put("ins_pensabstracto4", ""+ patient.getIns_pensabstracto4());

		if(patient.getIns_percecpcion1() == null) values.put("ins_percecpcion1", "");
		else values.put("ins_percecpcion1", ""+ patient.getIns_percecpcion1());

		if(patient.getIns_percecpcion2() == null) values.put("ins_percecpcion2", "");
		else values.put("ins_percecpcion2", ""+ patient.getIns_percecpcion2());

		if(patient.getIns_percecpcion3() == null) values.put("ins_percecpcion3", "");
		else values.put("ins_percecpcion3", ""+ patient.getIns_percecpcion3());

		if(patient.getIns_percecpcion4() == null) values.put("ins_percecpcion4", "");
		else values.put("ins_percecpcion4", ""+ patient.getIns_percecpcion4());

		if(patient.getIns_total1() == null) values.put("ins_total1", "");
		else values.put("ins_total1", ""+ patient.getIns_total1());

		if(patient.getIns_total2() == null) values.put("ins_total2", "");
		else values.put("ins_total2", ""+ patient.getIns_total2());

		if(patient.getIns_total3() == null) values.put("ins_total3", "");
		else values.put("ins_total3", ""+ patient.getIns_total3());

		if(patient.getIns_total4() == null) values.put("ins_total4", "");
		else values.put("ins_total4", ""+ patient.getIns_total4());

		if(patient.getIns_mmse1() == null) values.put("ins_mmse1", "");
		else values.put("ins_mmse1", ""+ patient.getIns_mmse1());

		if(patient.getIns_mmse2() == null) values.put("ins_mmse2", "");
		else values.put("ins_mmse2", ""+ patient.getIns_mmse2());

		if(patient.getIns_mmse3() == null) values.put("ins_mmse3", "");
		else values.put("ins_mmse3", ""+ patient.getIns_mmse3());

		if(patient.getIns_mmse4() == null) values.put("ins_mmse4", "");
		else values.put("ins_mmse4", ""+ patient.getIns_mmse4());

		if(patient.getIns_indbathel1() == null) values.put("ins_indbathel1", "");
		else values.put("ins_indbathel1", ""+ patient.getIns_indbathel1());

		if(patient.getIns_indbathel2() == null) values.put("ins_indbathel2", "");
		else values.put("ins_indbathel2", ""+ patient.getIns_indbathel2());

		if(patient.getIns_indbathel3() == null) values.put("ins_indbathel3", "");
		else values.put("ins_indbathel3", ""+ patient.getIns_indbathel3());

		if(patient.getIns_indlawton1() == null) values.put("ins_indlawton1", "");
		else values.put("ins_indlawton1", ""+ patient.getIns_indlawton1());

		if(patient.getIns_indlawton2() == null) values.put("ins_indlawton2", "");
		else values.put("ins_indlawton2", ""+ patient.getIns_indlawton2());

		if(patient.getIns_indlawton3() == null) values.put("ins_indlawton3", "");
		else values.put("ins_indlawton3", ""+ patient.getIns_indlawton3());

		//ins_url no va a ser un campo de la entidad si no una variable del DTO
		String ins_url = this.getCDNURL("informe_neuro", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setIns_url(ins_url);
		return res;
	}


	public PatientDTO saveInformePsicoSocial(String id, LocalDate ipsFechaInforme, String ipsSanitarios, String ipsSociofamiliar, String ipsEvalcognitiva,
											 String ipsEvalconductual, String ipsEvalfuncional, String ipsSituacioneconomica, String ipsObservaciones) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		patient.setIps_fecha_informe(ipsFechaInforme);
		patient.setIps_sanitarios(ipsSanitarios);
		patient.setIps_sociofamiliar(ipsSociofamiliar);
		patient.setIps_evalcognitiva(ipsEvalcognitiva);
		patient.setIps_evalconductual(ipsEvalconductual);
		patient.setIps_evalfuncional(ipsEvalfuncional);
		patient.setIps_situacioneconomica(ipsSituacioneconomica);
		patient.setIps_observaciones(ipsObservaciones);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<String, String>();

		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city!=null?city.getName():"");
		values.put("STATENAME", state!=null?state.getName():"");

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("SEX", patient.getGender());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());

		values.put("OCUPACION_ANTERIOR", patient.getOcupacionAnterior());
		values.put("NIVEL_FORMATIVO", patient.getHs_nivel_formativo());


		values.put("ips_sanitarios", patient.getIps_sanitarios());
		values.put("ips_sociofamiliar", patient.getIps_sociofamiliar());
		values.put("ips_evalcognitiva", patient.getIps_evalcognitiva());
		values.put("ips_evalconductual", patient.getIps_evalconductual());
		values.put("ips_evalfuncional", ""+ patient.getIps_evalfuncional());
		values.put("ips_situacioneconomica", patient.getIps_situacioneconomica());
		values.put("ips_observaciones", patient.getIps_observaciones());


		try {  values.put("ips_fecha_informe", patient.getIps_fecha_informe().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); } catch (Exception e) { values.put("ips_fecha_informe",""); }


		//ins_url no va a ser un campo de la entidad si no una variable del DTO
		String ips_url = this.getCDNURL("informe_psicosocial", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setIps_url(ips_url);
		return res;
	}




	public PatientDTO savePAIFisio(
			String id,
			LocalDate paiFisioFechaValoracion,
			String paiFisioProbSalud,
			String paiFisioDolores,
			String paiFisioDuerme,
			String paiFisioNecAliment,
			String paiFisioHabSaludables,
			String paiFisioAtencionPreven,
			String paiFisioAccesoAtencion,
			String paiFisioMedicacionRequerida,
			String paiFisioAlergias,
			String paiFisioUpp,
			String paiFisioAutonomo,
			String paiFisioAyudasTecnicas,
			String paiFisioMovilidadMmss,
			String paiFisioMovilidadMmii,
			String paiFisioMovilidadCuello,
			String paiFisioMovilidaTronco,
			String paiFisioEquilibrio,
			String paiFisioBipedestacion,
			String paiFisioMarcha,
			String paiFisioRiesgoCaidas,
			String paiFisioDeformidades,
			String paiFisioDisfrutaOcio,
			String paiFisioEspaciosOcio,
			String paiFisioRelacionesEntorno,
			String paiFisioObjetivos,
			String paiFisioTratamiento,
			String paiFisioValoraciones,
			String paiFisioActuaciones,
			String paiFisioIncidencias
	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		patient.setPai_fisio_fecha_valoracion(paiFisioFechaValoracion);
		patient.setPai_fisio_prob_salud(paiFisioProbSalud);
		patient.setPai_fisio_dolres(paiFisioDolores);
		patient.setPai_fisio_duerme(paiFisioDuerme);
		patient.setPai_fisio_nec_aliment(paiFisioNecAliment);
		patient.setPai_fisio_hab_saludables(paiFisioHabSaludables);
		patient.setPai_fisio_atencion_preven(paiFisioAtencionPreven);
		patient.setPai_fisio_acceso_atencion(paiFisioAccesoAtencion);
		patient.setPai_fisio_medicacion_requerida(paiFisioMedicacionRequerida);
		patient.setPai_fisio_alergias(paiFisioAlergias);
		patient.setPai_fisio_upp(paiFisioUpp);
		patient.setPai_fisio_autonomo(paiFisioAutonomo);
		patient.setPai_fisio_ayudas_tecnicas(paiFisioAyudasTecnicas);
		patient.setPai_fisio_movilidad_mmss(paiFisioMovilidadMmss);
		patient.setPai_fisio_movilidad_mmii(paiFisioMovilidadMmii);
		patient.setPai_fisio_movilidad_cuello(paiFisioMovilidadCuello);
		patient.setPai_fisio_movilida_tronco(paiFisioMovilidaTronco);
		patient.setPai_fisio_equilibrio(paiFisioEquilibrio);
		patient.setPai_fisio_bipedestacion(paiFisioBipedestacion);
		patient.setPai_fisio_marcha(paiFisioMarcha);
		patient.setPai_fisio_riesgo_caidas(paiFisioRiesgoCaidas);
		patient.setPai_fisio_deformidades(paiFisioDeformidades);
		patient.setPai_fisio_disfruta_ocio(paiFisioDisfrutaOcio);
		patient.setPai_fisio_espacios_ocio(paiFisioEspaciosOcio);
		patient.setPai_fisio_relaciones_entorno(paiFisioRelacionesEntorno);
		patient.setPai_fisio_objetivos(paiFisioObjetivos);
		patient.setPai_fisio_tratamiento(paiFisioTratamiento);
		patient.setPai_fisio_valoraciones(paiFisioValoraciones);
		patient.setPai_fisio_actuaciones(paiFisioActuaciones);
		patient.setPai_fisio_incidencias(paiFisioIncidencias);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<>();

		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());
		/*
		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city != null ? city.getName() : "");
		values.put("STATENAME", state != null ? state.getName() : "");
		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("SEX", patient.getGender());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());
		values.put("OCUPACION_ANTERIOR", patient.getOcupacionAnterior());
		values.put("NIVEL_FORMATIVO", patient.getHs_nivel_formativo());
		 */

		values.put("pai_fisio_prob_salud", patient.getPai_fisio_prob_salud());
		values.put("pai_fisio_dolres", patient.getPai_fisio_dolres());
		values.put("pai_fisio_duerme", patient.getPai_fisio_duerme());
		values.put("pai_fisio_nec_aliment", patient.getPai_fisio_nec_aliment());
		values.put("pai_fisio_hab_saludables", patient.getPai_fisio_hab_saludables());
		values.put("pai_fisio_atencion_preven", patient.getPai_fisio_atencion_preven());
		values.put("pai_fisio_acceso_atencion", patient.getPai_fisio_acceso_atencion());
		values.put("pai_fisio_medicacion_requerida", patient.getPai_fisio_medicacion_requerida());
		values.put("pai_fisio_alergias", patient.getPai_fisio_alergias());
		values.put("pai_fisio_upp", patient.getPai_fisio_upp());
		values.put("pai_fisio_autonomo", patient.getPai_fisio_autonomo());
		values.put("pai_fisio_ayudas_tecnicas", patient.getPai_fisio_ayudas_tecnicas());
		values.put("pai_fisio_movilidad_mmss", patient.getPai_fisio_movilidad_mmss());
		values.put("pai_fisio_movilidad_mmii", patient.getPai_fisio_movilidad_mmii());
		values.put("pai_fisio_movilidad_cuello", patient.getPai_fisio_movilidad_cuello());
		values.put("pai_fisio_movilida_tronco", patient.getPai_fisio_movilida_tronco());
		values.put("pai_fisio_equilibrio", patient.getPai_fisio_equilibrio());
		values.put("pai_fisio_bipedestacion", patient.getPai_fisio_bipedestacion());
		values.put("pai_fisio_marcha", patient.getPai_fisio_marcha());
		values.put("pai_fisio_riesgo_caidas", patient.getPai_fisio_riesgo_caidas());
		values.put("pai_fisio_deformidades", patient.getPai_fisio_deformidades());
		values.put("pai_fisio_disfruta_ocio", patient.getPai_fisio_disfruta_ocio());
		values.put("pai_fisio_espacios_ocio", patient.getPai_fisio_espacios_ocio());
		values.put("pai_fisio_relaciones_entorno", patient.getPai_fisio_relaciones_entorno());
		values.put("pai_fisio_objetivos", patient.getPai_fisio_objetivos());
		values.put("pai_fisio_tratamiento", patient.getPai_fisio_tratamiento());
		values.put("pai_fisio_valoraciones", patient.getPai_fisio_valoraciones());
		values.put("pai_fisio_actuaciones", patient.getPai_fisio_actuaciones());
		values.put("pai_fisio_incidencias", patient.getPai_fisio_incidencias());

		try {
			values.put("pai_fisio_fecha_valoracion", patient.getPai_fisio_fecha_valoracion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} catch (Exception e) {
			values.put("pai_fisio_fecha_valoracion", "");
		}

		String paiFisioUrl = this.getCDNURL("pai_fisio", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setPai_fisio_url(paiFisioUrl);
		return res;
	}



	public PatientDTO savePAIPsico(
			String id,
			String paiPsicoAcude,
			String paiPsicoSintomas,
			String paiPsicoDiagnostico,
			String paiPsicoQuienDiagnostica,
			LocalDate paiPsicoFechaDiagnostico,
			String paiPsicoFormaEvaluacion,
			String paiPsicoSintomatologiaActual,
			String paiPsicoAntecedentes,
			String paiPsicoBreveHistorial,
			String paiPsicoOrientacion,
			String paiPsicoLenguaje,
			String paiPsicoMemoria,
			String paiPsicoAtencion,
			String paiPsicoPraxi,
			String paiPsicoPensamientoAbstracto,
			String paiPsicoPercepcion,
			String paiPsicoFuncionEjecutiva,
			String paiPsicoEscalaFolstein,
			String paiPsicoEvaluacionConductual,
			String paiPsicoPlanActValoracionS1,
			String paiPsicoPlanActValoracionS2,
			String paiPsicoPlanActInstrumentosS1,
			String paiPsicoPlanActInstrumentosS2,
			String paiPsicoPlanActObjetivosS1,
			String paiPsicoPlanActObjetivosS2,
			String paiPsicoPlanActActividadesS1,
			String paiPsicoPlanActActividadesS2,
			String paiPsicoPlanActIncidenciasS1,
			String paiPsicoPlanActIncidenciasS2,
			String paiPsicoValoraciones,
			String paiPsicoActuaciones,
			String paiPsicoIncidencias
	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		patient.setPai_psico_acude(paiPsicoAcude);
		patient.setPai_psico_sintomas(paiPsicoSintomas);
		patient.setPai_psico_diagnostico(paiPsicoDiagnostico);
		patient.setPai_psico_quien_diagnostica(paiPsicoQuienDiagnostica);
		patient.setPai_psico_fecha_diagnostico(paiPsicoFechaDiagnostico);
		patient.setPai_psico_forma_evalucion(paiPsicoFormaEvaluacion);
		patient.setPai_psico_sintomatologia_actual(paiPsicoSintomatologiaActual);
		patient.setPai_psico_antecedentes(paiPsicoAntecedentes);
		patient.setPai_psico_breve_historial(paiPsicoBreveHistorial);
		patient.setPai_psico_orientacion(paiPsicoOrientacion);
		patient.setPai_psico_lenguaje(paiPsicoLenguaje);
		patient.setPai_psico_memoria(paiPsicoMemoria);
		patient.setPai_psico_atencion(paiPsicoAtencion);
		patient.setPai_psico_praxi(paiPsicoPraxi);
		patient.setPai_psico_pensamiento_abstracto(paiPsicoPensamientoAbstracto);
		patient.setPai_psico_percepcion(paiPsicoPercepcion);
		patient.setPai_psico_funcion_ejecutiva(paiPsicoFuncionEjecutiva);
		patient.setPai_psico_escala_folstein(paiPsicoEscalaFolstein);
		patient.setPai_psico_evaluacion_conductual(paiPsicoEvaluacionConductual);
		patient.setPai_psico_plan_act_valoracion_s1(paiPsicoPlanActValoracionS1);
		patient.setPai_psico_plan_act_valoracion_s2(paiPsicoPlanActValoracionS2);
		patient.setPai_psico_plan_act_instrumentos_s1(paiPsicoPlanActInstrumentosS1);
		patient.setPai_psico_plan_act_instrumentos_s2(paiPsicoPlanActInstrumentosS2);
		patient.setPai_psico_plan_act_objetivos_s1(paiPsicoPlanActObjetivosS1);
		patient.setPai_psico_plan_act_objetivos_s2(paiPsicoPlanActObjetivosS2);
		patient.setPai_psico_plan_act_actividades_s1(paiPsicoPlanActActividadesS1);
		patient.setPai_psico_plan_act_actividades_s2(paiPsicoPlanActActividadesS2);
		patient.setPai_psico_plan_act_incidencias_s1(paiPsicoPlanActIncidenciasS1);
		patient.setPai_psico_plan_act_incidencias_s2(paiPsicoPlanActIncidenciasS2);
		patient.setPai_psico_valoraciones(paiPsicoValoraciones);
		patient.setPai_psico_actuaciones(paiPsicoActuaciones);
		patient.setPai_psico_incidencias(paiPsicoIncidencias);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<>();

		/*
		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());
		 */

		values.put("pai_psico_acude", paiPsicoAcude);
		values.put("pai_psico_sintomas", paiPsicoSintomas);
		values.put("pai_psico_diagnostico", paiPsicoDiagnostico);
		values.put("pai_psico_quien_diagnostica", paiPsicoQuienDiagnostica);
		values.put("pai_psico_forma_evalucion", paiPsicoFormaEvaluacion);
		values.put("pai_psico_sintomatologia_actual", paiPsicoSintomatologiaActual);
		values.put("pai_psico_antecedentes", paiPsicoAntecedentes);
		values.put("pai_psico_breve_historial", paiPsicoBreveHistorial);
		values.put("pai_psico_orientacion", paiPsicoOrientacion);
		values.put("pai_psico_lenguaje", paiPsicoLenguaje);
		values.put("pai_psico_memoria", paiPsicoMemoria);
		values.put("pai_psico_atencion", paiPsicoAtencion);
		values.put("pai_psico_praxi", paiPsicoPraxi);
		values.put("pai_psico_pensamiento_abstracto", paiPsicoPensamientoAbstracto);
		values.put("pai_psico_percepcion", paiPsicoPercepcion);
		values.put("pai_psico_funcion_ejecutiva", paiPsicoFuncionEjecutiva);
		values.put("pai_psico_escala_folstein", paiPsicoEscalaFolstein);
		values.put("pai_psico_evaluacion_conductual", paiPsicoEvaluacionConductual);
		values.put("pai_psico_plan_act_valoracion_s1", paiPsicoPlanActValoracionS1);
		values.put("pai_psico_plan_act_valoracion_s2", paiPsicoPlanActValoracionS2);
		values.put("pai_psico_plan_act_instrumentos_s1", paiPsicoPlanActInstrumentosS1);
		values.put("pai_psico_plan_act_instrumentos_s2", paiPsicoPlanActInstrumentosS2);
		values.put("pai_psico_plan_act_objetivos_s1", paiPsicoPlanActObjetivosS1);
		values.put("pai_psico_plan_act_objetivos_s2", paiPsicoPlanActObjetivosS2);
		values.put("pai_psico_plan_act_actividades_s1", paiPsicoPlanActActividadesS1);
		values.put("pai_psico_plan_act_actividades_s2", paiPsicoPlanActActividadesS2);
		values.put("pai_psico_plan_act_incidencias_s1", paiPsicoPlanActIncidenciasS1);
		values.put("pai_psico_plan_act_incidencias_s2", paiPsicoPlanActIncidenciasS2);
		values.put("pai_psico_valoraciones", paiPsicoValoraciones);
		values.put("pai_psico_actuaciones", paiPsicoActuaciones);
		values.put("pai_psico_incidencias", paiPsicoIncidencias);

		try {
			values.put("pai_psico_fecha_diagnostico", patient.getPai_psico_fecha_diagnostico().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} catch (Exception e) {
			values.put("pai_psico_fecha_diagnostico", "");
		}

		String paiPsicoUrl = this.getCDNURL("pai_psico", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setPai_psico_url(paiPsicoUrl);
		return res;
	}

	public PatientDTO savePAITocupa(
			String id,
			String paiTocupaNivelIndependencia,
			String paiTocupaPlanMotriz,
			String paiTocupaAlimentacion,
			String paiTocupaWc,
			String paiTocupaAseo,
			String paiTocupaDeambular,
			String paiTocupaTransferencias,
			String paiTocupaVestido,
			String paiTocupaBano,
			String paiTocupaEscalones,
			String paiTocupaEsfinteres,
			String paiTocupaDinero,
			String paiTocupaCompras,
			String paiTocupaTelefono,
			String paiTocupaCasa,
			String paiTocupaCalle,
			String paiTocupaMedicacion,
			String paiTocupaIndiceBarthel,
			String paiTocupaEscalaActividad,
			String paiTocupaDisfrutaTiempo,
			String paiTocupaEspaciosOcio,
			String paiTocupaDisfrutaOcio,
			String paiTocupaRelacionOtros,
			String paiTocupaPropiosObjetivos,
			String paiTocupaParticipaActividades,
			String paiTocupaActividadesIniciativaPropia,
			String pai_tocupa_valoraciones,
			String pai_tocupa_actuaciones,
			String pai_tocupa_incidencias



	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		patient.setPai_tocupa_nivel_independencia(paiTocupaNivelIndependencia);
		patient.setPai_tocupa_plan_motriz(paiTocupaPlanMotriz);
		patient.setPai_tocupa_alimentacion(paiTocupaAlimentacion);
		patient.setPai_tocupa_wc(paiTocupaWc);
		patient.setPai_tocupa_aseo(paiTocupaAseo);
		patient.setPai_tocupa_deambular(paiTocupaDeambular);
		patient.setPai_tocupa_transferencias(paiTocupaTransferencias);
		patient.setPai_tocupa_vestido(paiTocupaVestido);
		patient.setPai_tocupa_bano(paiTocupaBano);
		patient.setPai_tocupa_escaolones(paiTocupaEscalones);
		patient.setPai_tocupa_esfinteres(paiTocupaEsfinteres);
		patient.setPai_tocupa_dinero(paiTocupaDinero);
		patient.setPai_tocupa_compras(paiTocupaCompras);
		patient.setPai_tocupa_telefono(paiTocupaTelefono);
		patient.setPai_tocupa_casa(paiTocupaCasa);
		patient.setPai_tocupa_calle(paiTocupaCalle);
		patient.setPai_tocupa_medicacion(paiTocupaMedicacion);
		patient.setPai_tocupa_indice_barthel(paiTocupaIndiceBarthel);
		patient.setPai_tocupa_escala_actividad(paiTocupaEscalaActividad);
		patient.setPai_tocupa_disfruta_tiempo(paiTocupaDisfrutaTiempo);
		patient.setPai_tocupa_espacios_ocio(paiTocupaEspaciosOcio);
		patient.setPai_tocupa_disfruta_ocio(paiTocupaDisfrutaOcio);
		patient.setPai_tocupa_relacion_otros(paiTocupaRelacionOtros);
		patient.setPai_tocupa_propios_objetivos(paiTocupaPropiosObjetivos);
		patient.setPai_tocupa_participa_actividades(paiTocupaParticipaActividades);
		patient.setPai_tocupa_actividades_iniciativa_propia(paiTocupaActividadesIniciativaPropia);
		patient.setPai_tocupa_valoraciones(pai_tocupa_valoraciones);
		patient.setPai_tocupa_actuaciones(pai_tocupa_actuaciones);
		patient.setPai_tocupa_incidencias(pai_tocupa_incidencias);


		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<>();

		values.put("FS_FECHA_INSCRIPCION", patient.getFs_fecha_inscripcion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("AGE", Period.between(patient.getBirthdate(), LocalDate.now()).getYears()+"");

		values.put("RELATIVEFULLNAME", relative!=null?relative.getFullname() + " / " + patient.getRelativerelation():"");


		values.put("pai_tocupa_nivel_independencia", paiTocupaNivelIndependencia);
		values.put("pai_tocupa_plan_motriz", paiTocupaPlanMotriz);
		values.put("pai_tocupa_alimentacion", paiTocupaAlimentacion);
		values.put("pai_tocupa_wc", paiTocupaWc);
		values.put("pai_tocupa_aseo", paiTocupaAseo);
		values.put("pai_tocupa_deambular", paiTocupaDeambular);
		values.put("pai_tocupa_transferencias", paiTocupaTransferencias);
		values.put("pai_tocupa_vestido", paiTocupaVestido);
		values.put("pai_tocupa_bano", paiTocupaBano);
		values.put("pai_tocupa_escaolones", paiTocupaEscalones);
		values.put("pai_tocupa_esfinteres", paiTocupaEsfinteres);
		values.put("pai_tocupa_dinero", paiTocupaDinero);
		values.put("pai_tocupa_compras", paiTocupaCompras);
		values.put("pai_tocupa_telefono", paiTocupaTelefono);
		values.put("pai_tocupa_casa", paiTocupaCasa);
		values.put("pai_tocupa_calle", paiTocupaCalle);
		values.put("pai_tocupa_medicacion", paiTocupaMedicacion);
		values.put("pai_tocupa_indice_barthel", paiTocupaIndiceBarthel);
		values.put("pai_tocupa_escala_actividad", paiTocupaEscalaActividad);
		values.put("pai_tocupa_disfruta_tiempo", paiTocupaDisfrutaTiempo);
		values.put("pai_tocupa_espacios_ocio", paiTocupaEspaciosOcio);
		values.put("pai_tocupa_disfruta_ocio", paiTocupaDisfrutaOcio);
		values.put("pai_tocupa_relacion_otros", paiTocupaRelacionOtros);
		values.put("pai_tocupa_propios_objetivos", paiTocupaPropiosObjetivos);
		values.put("pai_tocupa_participa_actividades", paiTocupaParticipaActividades);
		values.put("pai_tocupa_actividades_iniciativa_propia", paiTocupaActividadesIniciativaPropia);
		values.put("pai_tocupa_valoraciones", pai_tocupa_valoraciones);
		values.put("pai_tocupa_actuaciones", pai_tocupa_actuaciones);
		values.put("pai_tocupa_incidencias", pai_tocupa_incidencias);

		String paiTocupaUrl = this.getCDNURL("pai_tocupa", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setPai_tocupa_url(paiTocupaUrl);
		return res;
	}




	public PatientDTO savePAIEnfer(
			String id,
			String pai_enfer_diagnostico,
			String pai_enfer_problemas_audio,
			String pai_enfer_problemas_audio_text,
			String pai_enfer_uso_audifono,
			String pai_enfer_problemas_vision,
			String pai_enfer_problemas_vision_text,
			String pai_enfer_uso_gafas,
			String pai_enfer_tension,
			String pai_enfer_uso_medicacion,
			String pai_enfer_diabetes,
			String pai_enfer_diabetes_text,
			String pai_enfer_alergias,
			String pai_enfer_otras_enfermedades,
			String pai_enfer_tratamiento_medicamento_1,
			String pai_enfer_tratamiento_medicamento_2,
			String pai_enfer_tratamiento_medicamento_3,
			String pai_enfer_tratamiento_medicamento_4,
			String pai_enfer_tratamiento_dosis_1,
			String pai_enfer_tratamiento_dosis_2,
			String pai_enfer_tratamiento_dosis_3,
			String pai_enfer_tratamiento_dosis_4,
			String pai_enfer_tratamiento_fecha_1,
			String pai_enfer_tratamiento_fecha_2,
			String pai_enfer_tratamiento_fecha_3,
			String pai_enfer_tratamiento_fecha_4,
			String pai_enfer_tratamiento_para_1,
			String pai_enfer_tratamiento_para_2,
			String pai_enfer_tratamiento_para_3,
			String pai_enfer_tratamiento_para_4,
			String pai_enfer_medicacion_centro,
			String pai_enfer_medicacion_centro_text,
			String pai_enfer_medicacion_puntual,
			String pai_enfer_wc_esfinteres,
			String pai_enfer_wc_retencion,
			String pai_enfer_wc_estrenimiento,
			String pai_enfer_wc_acompanam,
			String pai_enfer_alim_alergias,
			String pai_enfer_alim_alergias_text,
			String pai_enfer_alim_dieta,
			String pai_enfer_alim_dieta_text,
			String pai_enfer_alim_problemas_deglucion,
			String pai_enfer_alim_espesantes,
			String pai_enfer_alim_ayuda,
			String pai_enfer_alim_observaciones,
			String pai_enfer_valoraciones,
			String pai_enfer_actuaciones,
			String pai_enfer_incidencias
	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		// Actualiza los campos del paciente con los valores proporcionados
		patient.setPai_enfer_diagnostico(pai_enfer_diagnostico);
		patient.setPai_enfer_problemas_audio(pai_enfer_problemas_audio);
		patient.setPai_enfer_problemas_audio_text(pai_enfer_problemas_audio_text);
		patient.setPai_enfer_uso_audifono(pai_enfer_uso_audifono);

		patient.setPai_enfer_problemas_vision(pai_enfer_problemas_vision);
		patient.setPai_enfer_problemas_vision_text(pai_enfer_problemas_vision_text);
		patient.setPai_enfer_uso_gafas(pai_enfer_uso_gafas);
		patient.setPai_enfer_tension(pai_enfer_tension);
		patient.setPai_enfer_uso_medicacion(pai_enfer_uso_medicacion);
		patient.setPai_enfer_diabetes(pai_enfer_diabetes);
		patient.setPai_enfer_diabetes_text(pai_enfer_diabetes_text);
		patient.setPai_enfer_alergias(pai_enfer_alergias);
		patient.setPai_enfer_otras_enfermedades(pai_enfer_otras_enfermedades);
		patient.setPai_enfer_tratamiento_medicamento_1(pai_enfer_tratamiento_medicamento_1);
		patient.setPai_enfer_tratamiento_medicamento_2(pai_enfer_tratamiento_medicamento_2);
		patient.setPai_enfer_tratamiento_medicamento_3(pai_enfer_tratamiento_medicamento_3);
		patient.setPai_enfer_tratamiento_medicamento_4(pai_enfer_tratamiento_medicamento_4);
		patient.setPai_enfer_tratamiento_dosis_1(pai_enfer_tratamiento_dosis_1);
		patient.setPai_enfer_tratamiento_dosis_2(pai_enfer_tratamiento_dosis_2);
		patient.setPai_enfer_tratamiento_dosis_3(pai_enfer_tratamiento_dosis_3);
		patient.setPai_enfer_tratamiento_dosis_4(pai_enfer_tratamiento_dosis_4);
		patient.setPai_enfer_tratamiento_fecha_1(pai_enfer_tratamiento_fecha_1);
		patient.setPai_enfer_tratamiento_fecha_2(pai_enfer_tratamiento_fecha_2);
		patient.setPai_enfer_tratamiento_fecha_3(pai_enfer_tratamiento_fecha_3);
		patient.setPai_enfer_tratamiento_fecha_4(pai_enfer_tratamiento_fecha_4);
		patient.setPai_enfer_tratamiento_para_1(pai_enfer_tratamiento_para_1);
		patient.setPai_enfer_tratamiento_para_2(pai_enfer_tratamiento_para_2);
		patient.setPai_enfer_tratamiento_para_3(pai_enfer_tratamiento_para_3);
		patient.setPai_enfer_tratamiento_para_4(pai_enfer_tratamiento_para_4);
		patient.setPai_enfer_medicacion_centro(pai_enfer_medicacion_centro);
		patient.setPai_enfer_medicacion_centro_text(pai_enfer_medicacion_centro_text);
		patient.setPai_enfer_medicacion_puntual(pai_enfer_medicacion_puntual);
		patient.setPai_enfer_wc_esfinteres(pai_enfer_wc_esfinteres);
		patient.setPai_enfer_wc_retencion(pai_enfer_wc_retencion);
		patient.setPai_enfer_wc_estrenimiento(pai_enfer_wc_estrenimiento);
		patient.setPai_enfer_wc_acompanam(pai_enfer_wc_acompanam);
		patient.setPai_enfer_alim_alergias(pai_enfer_alim_alergias);
		patient.setPai_enfer_alim_alergias_text(pai_enfer_alim_alergias_text);
		patient.setPai_enfer_alim_dieta(pai_enfer_alim_dieta);
		patient.setPai_enfer_alim_dieta_text(pai_enfer_alim_dieta_text);
		patient.setPai_enfer_alim_problemas_deglucion(pai_enfer_alim_problemas_deglucion);
		patient.setPai_enfer_alim_espesantes(pai_enfer_alim_espesantes);
		patient.setPai_enfer_alim_ayuda(pai_enfer_alim_ayuda);
		patient.setPai_enfer_alim_observaciones(pai_enfer_alim_observaciones);
		patient.setPai_enfer_valoraciones(pai_enfer_valoraciones);
		patient.setPai_enfer_actuaciones(pai_enfer_actuaciones);
		patient.setPai_enfer_incidencias(pai_enfer_incidencias);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<>();
		// Aquí deberías formatear las fechas según tus necesidades
		if (patient.getFs_fecha_inscripcion() != null) {
			values.put("FS_FECHA_INSCRIPCION", patient.getFs_fecha_inscripcion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());
		values.put("FS_NUM_EXPEDIENTE", patient.getFs_num_expediente());


		// Agrega todos los campos de pai_enfer al HashMap
		values.put("pai_enfer_diagnostico", pai_enfer_diagnostico);
		values.put("pai_enfer_problemas_audio", pai_enfer_problemas_audio);
		values.put("pai_enfer_problemas_audio_text", pai_enfer_problemas_audio_text);
		values.put("pai_enfer_uso_audifono", pai_enfer_uso_audifono);
		values.put("pai_enfer_problemas_vision", pai_enfer_problemas_vision);
		values.put("pai_enfer_problemas_vision_text", pai_enfer_problemas_vision_text);
		values.put("pai_enfer_uso_gafas", pai_enfer_uso_gafas);
		values.put("pai_enfer_tension", pai_enfer_tension);
		values.put("pai_enfer_uso_medicacion", pai_enfer_uso_medicacion);
		values.put("pai_enfer_diabetes", pai_enfer_diabetes);
		values.put("pai_enfer_diabetes_text", pai_enfer_diabetes_text);
		values.put("pai_enfer_alergias", pai_enfer_alergias);
		values.put("pai_enfer_otras_enfermedades", pai_enfer_otras_enfermedades);

		String med = "";
		if(pai_enfer_tratamiento_medicamento_1!=null && !pai_enfer_tratamiento_medicamento_1.trim().equals(""))
		{
			med += "<li><b>MEDICAMENTO: </b> " + pai_enfer_tratamiento_medicamento_1;
			med += " / <b>DOSIS: </b> " + pai_enfer_tratamiento_dosis_1;
			med += " / <b>FECHA INICIO: </b> " + pai_enfer_tratamiento_fecha_1;
			med += " / <b>LA TOMA PARA: </b> " + pai_enfer_tratamiento_para_1 + "</li>";
		}
		if (pai_enfer_tratamiento_medicamento_2 != null && !pai_enfer_tratamiento_medicamento_2.trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + pai_enfer_tratamiento_medicamento_2;
			med += " / <b>DOSIS: </b> " + pai_enfer_tratamiento_dosis_2;
			med += " / <b>FECHA INICIO: </b> " + pai_enfer_tratamiento_fecha_2;
			med += " / <b>LA TOMA PARA: </b> " + pai_enfer_tratamiento_para_2 + "</li>";
		}

		if (pai_enfer_tratamiento_medicamento_3 != null && !pai_enfer_tratamiento_medicamento_3.trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + pai_enfer_tratamiento_medicamento_3;
			med += " / <b>DOSIS: </b> " + pai_enfer_tratamiento_dosis_3;
			med += " / <b>FECHA INICIO: </b> " + pai_enfer_tratamiento_fecha_3;
			med += " / <b>LA TOMA PARA: </b> " + pai_enfer_tratamiento_para_3 + "</li>";
		}

		if (pai_enfer_tratamiento_medicamento_4 != null && !pai_enfer_tratamiento_medicamento_4.trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + pai_enfer_tratamiento_medicamento_4;
			med += " / <b>DOSIS: </b> " + pai_enfer_tratamiento_dosis_4;
			med += " / <b>FECHA INICIO: </b> " + pai_enfer_tratamiento_fecha_4;
			med += " / <b>LA TOMA PARA: </b> " + pai_enfer_tratamiento_para_4 + "</li>";
		}
		values.put("med", med);

		values.put("pai_enfer_tratamiento_medicamento_2", pai_enfer_tratamiento_medicamento_2);
		values.put("pai_enfer_tratamiento_medicamento_3", pai_enfer_tratamiento_medicamento_3);
		values.put("pai_enfer_tratamiento_medicamento_4", pai_enfer_tratamiento_medicamento_4);
		values.put("pai_enfer_tratamiento_dosis_1", pai_enfer_tratamiento_dosis_1);
		values.put("pai_enfer_tratamiento_dosis_2", pai_enfer_tratamiento_dosis_2);
		values.put("pai_enfer_tratamiento_dosis_3", pai_enfer_tratamiento_dosis_3);
		values.put("pai_enfer_tratamiento_dosis_4", pai_enfer_tratamiento_dosis_4);
		values.put("pai_enfer_tratamiento_fecha_1", pai_enfer_tratamiento_fecha_1);
		values.put("pai_enfer_tratamiento_fecha_2", pai_enfer_tratamiento_fecha_2);
		values.put("pai_enfer_tratamiento_fecha_3", pai_enfer_tratamiento_fecha_3);
		values.put("pai_enfer_tratamiento_fecha_4", pai_enfer_tratamiento_fecha_4);
		values.put("pai_enfer_tratamiento_para_1", pai_enfer_tratamiento_para_1);
		values.put("pai_enfer_tratamiento_para_2", pai_enfer_tratamiento_para_2);
		values.put("pai_enfer_tratamiento_para_3", pai_enfer_tratamiento_para_3);
		values.put("pai_enfer_tratamiento_para_4", pai_enfer_tratamiento_para_4);


		values.put("pai_enfer_medicacion_centro", pai_enfer_medicacion_centro);
		values.put("pai_enfer_medicacion_centro_text", pai_enfer_medicacion_centro_text);
		values.put("pai_enfer_medicacion_puntual", pai_enfer_medicacion_puntual);
		values.put("pai_enfer_wc_esfinteres", pai_enfer_wc_esfinteres);
		values.put("pai_enfer_wc_retencion", pai_enfer_wc_retencion);
		values.put("pai_enfer_wc_estrenimiento", pai_enfer_wc_estrenimiento);
		values.put("pai_enfer_wc_acompanam", pai_enfer_wc_acompanam);
		values.put("pai_enfer_alim_alergias", pai_enfer_alim_alergias);
		values.put("pai_enfer_alim_alergias_text", pai_enfer_alim_alergias_text);
		values.put("pai_enfer_alim_dieta", pai_enfer_alim_dieta);
		values.put("pai_enfer_alim_dieta_text", pai_enfer_alim_dieta_text);
		values.put("pai_enfer_alim_problemas_deglucion", pai_enfer_alim_problemas_deglucion);
		values.put("pai_enfer_alim_espesantes", pai_enfer_alim_espesantes);
		values.put("pai_enfer_alim_ayuda", pai_enfer_alim_ayuda);
		values.put("pai_enfer_alim_observaciones", pai_enfer_alim_observaciones);
		values.put("pai_enfer_valoraciones", pai_enfer_valoraciones);
		values.put("pai_enfer_actuaciones", pai_enfer_actuaciones);
		values.put("pai_enfer_incidencias", pai_enfer_incidencias);

		String paiEnferUrl = this.getCDNURL("pai_enfer", patient.get_id(), values); //AQUÍ ESTÁ EL CAMBIO
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setPai_enfer_url(paiEnferUrl); //Y AQUÍ
		return res;
	}

	public PatientDTO savePAISocial(
			String id,
			String pai_social_historia,
			String pai_social_informes,
			String pai_social_informes_text,
			String pai_social_valoracion_disca,
			String pai_social_valoracion_disca_fecha,
			String pai_social_valoracion_disca_ca,
			String pai_social_valoracion_disca_grado,
			String pai_social_3_persona,
			String pai_social_ayudas_tecnicas,
			String pai_social_ayudas_tecnicas_text,
			String pai_social_movilidad,
			String pai_social_ley_dependencai,
			String pai_social_grado_y_nivel,
			String pai_social_cuidador,
			String pai_social_relacion_cuidador,
			String pai_social_indicadores,
			String pai_social_apoyos,
			String pai_social_vive,
			String pai_social_domicilio_obstaculos,
			String pai_social_domicilio_ayudas_tecnicas,
			String pai_social_domicilio_confort,
			String pai_social_domicilio_actual_quiere,
			String pai_social_domicilio_actual_otro,
			String pai_social_domicilio_actula_residencia,
			String pai_social_apoyo_tipo1,
			String pai_social_apoyo_tipo2,
			String pai_social_apoyo_tipo3,
			String pai_social_apoyo_tipo4,
			String pai_social_apoyo_titularidad1,
			String pai_social_apoyo_titularidad2,
			String pai_social_apoyo_titularidad3,
			String pai_social_apoyo_titularidad4,
			String pai_social_apoyo_coste1,
			String pai_social_apoyo_coste2,
			String pai_social_apoyo_coste3,
			String pai_social_apoyo_coste4,
			String pai_social_apoyo_aportacion1,
			String pai_social_apoyo_aportacion2,
			String pai_social_apoyo_aportacion3,
			String pai_social_apoyo_aportacion4,
			String pai_social_apoyo_dom_prestacion1,
			String pai_social_apoyo_dom_prestacion2,
			String pai_social_apoyo_dom_prestacion3,
			String pai_social_apoyo_dom_prestacion4,
			String pai_social_apoyo_dom_intensidad1,
			String pai_social_apoyo_dom_intensidad2,
			String pai_social_apoyo_dom_intensidad3,
			String pai_social_apoyo_dom_intensidad4,
			String pai_social_apoyo_dom_coste1,
			String pai_social_apoyo_dom_coste2,
			String pai_social_apoyo_dom_coste3,
			String pai_social_apoyo_dom_coste4,
			String pai_social_apoyo_dom_aportacion1,
			String pai_social_apoyo_dom_aportacion2,
			String pai_social_apoyo_dom_aportacion3,
			String pai_social_apoyo_dom_aportacion4,
			String pai_social_apoyo_otras_prestacion1,
			String pai_social_apoyo_otras_prestacion2,
			String pai_social_apoyo_otras_prestacion3,
			String pai_social_apoyo_otras_prestacion4,
			String pai_social_apoyo_otras_titularidad1,
			String pai_social_apoyo_otras_titularidad2,
			String pai_social_apoyo_otras_titularidad3,
			String pai_social_apoyo_otras_titularidad4,
			String pai_social_apoyo_otras_intensidad1,
			String pai_social_apoyo_otras_intensidad2,
			String pai_social_apoyo_otras_intensidad3,
			String pai_social_apoyo_otras_intensidad4,
			String pai_social_ingresos,
			String pai_social_ingresos_familia,
			String pai_social_ingresos_cubre,
			String pai_social_nivel_estudios,
			String pai_social_relaciones,
			String pai_social_necesidades,
			String pai_social_objetivos,
			String pai_social_valoraciones,
			String pai_social_actuaciones,
			String pai_social_incidencias
	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		// Actualiza los campos del paciente con los valores proporcionados
		patient.setPai_social_historia(pai_social_historia);
		patient.setPai_social_informes(pai_social_informes);
		patient.setPai_social_informes_text(pai_social_informes_text);
		patient.setPai_social_valoracion_disca(pai_social_valoracion_disca);
		patient.setPai_social_valoracion_disca_fecha(pai_social_valoracion_disca_fecha);
		patient.setPai_social_valoracion_disca_ca(pai_social_valoracion_disca_ca);
		patient.setPai_social_valoracion_disca_grado(pai_social_valoracion_disca_grado);
		patient.setPai_social_3_persona(pai_social_3_persona);
		patient.setPai_social_ayudas_tecnicas(pai_social_ayudas_tecnicas);
		patient.setPai_social_ayudas_tecnicas_text(pai_social_ayudas_tecnicas_text);
		patient.setPai_social_movilidad(pai_social_movilidad);
		patient.setPai_social_ley_dependencai(pai_social_ley_dependencai);
		patient.setPai_social_grado_y_nivel(pai_social_grado_y_nivel);
		patient.setPai_social_cuidador(pai_social_cuidador);
		patient.setPai_social_relacion_cuidador(pai_social_relacion_cuidador);
		patient.setPai_social_indicadores(pai_social_indicadores);
		patient.setPai_social_apoyos(pai_social_apoyos);
		patient.setPai_social_vive(pai_social_vive);
		patient.setPai_social_domicilio_obstaculos(pai_social_domicilio_obstaculos);
		patient.setPai_social_domicilio_ayudas_tecnicas(pai_social_domicilio_ayudas_tecnicas);
		patient.setPai_social_domicilio_confort(pai_social_domicilio_confort);
		patient.setPai_social_domicilio_actual_quiere(pai_social_domicilio_actual_quiere);
		patient.setPai_social_domicilio_actual_otro(pai_social_domicilio_actual_otro);
		patient.setPai_social_domicilio_actula_residencia(pai_social_domicilio_actula_residencia);
		patient.setPai_social_apoyo_tipo1(pai_social_apoyo_tipo1);
		patient.setPai_social_apoyo_tipo2(pai_social_apoyo_tipo2);
		patient.setPai_social_apoyo_tipo3(pai_social_apoyo_tipo3);
		patient.setPai_social_apoyo_tipo4(pai_social_apoyo_tipo4);
		patient.setPai_social_apoyo_titularidad1(pai_social_apoyo_titularidad1);
		patient.setPai_social_apoyo_titularidad2(pai_social_apoyo_titularidad2);
		patient.setPai_social_apoyo_titularidad3(pai_social_apoyo_titularidad3);
		patient.setPai_social_apoyo_titularidad4(pai_social_apoyo_titularidad4);
		patient.setPai_social_apoyo_coste1(pai_social_apoyo_coste1);
		patient.setPai_social_apoyo_coste2(pai_social_apoyo_coste2);
		patient.setPai_social_apoyo_coste3(pai_social_apoyo_coste3);
		patient.setPai_social_apoyo_coste4(pai_social_apoyo_coste4);
		patient.setPai_social_apoyo_aportacion1(pai_social_apoyo_aportacion1);
		patient.setPai_social_apoyo_aportacion2(pai_social_apoyo_aportacion2);
		patient.setPai_social_apoyo_aportacion3(pai_social_apoyo_aportacion3);
		patient.setPai_social_apoyo_aportacion4(pai_social_apoyo_aportacion4);
		patient.setPai_social_apoyo_dom_prestacion1(pai_social_apoyo_dom_prestacion1);
		patient.setPai_social_apoyo_dom_prestacion2(pai_social_apoyo_dom_prestacion2);
		patient.setPai_social_apoyo_dom_prestacion3(pai_social_apoyo_dom_prestacion3);
		patient.setPai_social_apoyo_dom_prestacion4(pai_social_apoyo_dom_prestacion4);
		patient.setPai_social_apoyo_dom_intensidad1(pai_social_apoyo_dom_intensidad1);
		patient.setPai_social_apoyo_dom_intensidad2(pai_social_apoyo_dom_intensidad2);
		patient.setPai_social_apoyo_dom_intensidad3(pai_social_apoyo_dom_intensidad3);
		patient.setPai_social_apoyo_dom_intensidad4(pai_social_apoyo_dom_intensidad4);
		patient.setPai_social_apoyo_dom_coste1(pai_social_apoyo_dom_coste1);
		patient.setPai_social_apoyo_dom_coste2(pai_social_apoyo_dom_coste2);
		patient.setPai_social_apoyo_dom_coste3(pai_social_apoyo_dom_coste3);
		patient.setPai_social_apoyo_dom_coste4(pai_social_apoyo_dom_coste4);
		patient.setPai_social_apoyo_dom_aportacion1(pai_social_apoyo_dom_aportacion1);
		patient.setPai_social_apoyo_dom_aportacion2(pai_social_apoyo_dom_aportacion2);
		patient.setPai_social_apoyo_dom_aportacion3(pai_social_apoyo_dom_aportacion3);
		patient.setPai_social_apoyo_dom_aportacion4(pai_social_apoyo_dom_aportacion4);
		patient.setPai_social_apoyo_otras_prestacion1(pai_social_apoyo_otras_prestacion1);
		patient.setPai_social_apoyo_otras_prestacion2(pai_social_apoyo_otras_prestacion2);
		patient.setPai_social_apoyo_otras_prestacion3(pai_social_apoyo_otras_prestacion3);
		patient.setPai_social_apoyo_otras_prestacion4(pai_social_apoyo_otras_prestacion4);
		patient.setPai_social_apoyo_otras_titularidad1(pai_social_apoyo_otras_titularidad1);
		patient.setPai_social_apoyo_otras_titularidad2(pai_social_apoyo_otras_titularidad2);
		patient.setPai_social_apoyo_otras_titularidad3(pai_social_apoyo_otras_titularidad3);
		patient.setPai_social_apoyo_otras_titularidad4(pai_social_apoyo_otras_titularidad4);
		patient.setPai_social_apoyo_otras_intensidad1(pai_social_apoyo_otras_intensidad1);
		patient.setPai_social_apoyo_otras_intensidad2(pai_social_apoyo_otras_intensidad2);
		patient.setPai_social_apoyo_otras_intensidad3(pai_social_apoyo_otras_intensidad3);
		patient.setPai_social_apoyo_otras_intensidad4(pai_social_apoyo_otras_intensidad4);
		patient.setPai_social_ingresos(pai_social_ingresos);
		patient.setPai_social_ingresos_familia(pai_social_ingresos_familia);
		patient.setPai_social_ingresos_cubre(pai_social_ingresos_cubre);
		patient.setPai_social_nivel_estudios(pai_social_nivel_estudios);
		patient.setPai_social_relaciones(pai_social_relaciones);
		patient.setPai_social_necesidades(pai_social_necesidades);
		patient.setPai_social_objetivos(pai_social_objetivos);
		patient.setPai_social_valoraciones(pai_social_valoraciones);
		patient.setPai_social_actuaciones(pai_social_actuaciones);
		patient.setPai_social_incidencias(pai_social_incidencias);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<>();

		// Agrega todos los campos de pai_social al HashMap
		values.put("pai_social_historia", pai_social_historia);
		values.put("pai_social_informes", pai_social_informes);
		values.put("pai_social_informes_text", pai_social_informes_text);
		values.put("pai_social_valoracion_disca", pai_social_valoracion_disca);
		values.put("pai_social_valoracion_disca_fecha", pai_social_valoracion_disca_fecha);
		values.put("pai_social_valoracion_disca_ca", pai_social_valoracion_disca_ca);
		values.put("pai_social_valoracion_disca_grado", pai_social_valoracion_disca_grado);
		values.put("pai_social_3_persona", pai_social_3_persona);
		values.put("pai_social_ayudas_tecnicas", pai_social_ayudas_tecnicas);
		values.put("pai_social_ayudas_tecnicas_text", pai_social_ayudas_tecnicas_text);
		values.put("pai_social_movilidad", pai_social_movilidad);
		values.put("pai_social_ley_dependencai", pai_social_ley_dependencai);
		values.put("pai_social_grado_y_nivel", pai_social_grado_y_nivel);
		values.put("pai_social_cuidador", pai_social_cuidador);
		values.put("pai_social_relacion_cuidador", pai_social_relacion_cuidador);
		values.put("pai_social_indicadores", pai_social_indicadores);
		values.put("pai_social_apoyos", pai_social_apoyos);
		values.put("pai_social_vive", pai_social_vive);
		values.put("pai_social_domicilio_obstaculos", pai_social_domicilio_obstaculos);
		values.put("pai_social_domicilio_ayudas_tecnicas", pai_social_domicilio_ayudas_tecnicas);
		values.put("pai_social_domicilio_confort", pai_social_domicilio_confort);
		values.put("pai_social_domicilio_actual_quiere", pai_social_domicilio_actual_quiere);
		values.put("pai_social_domicilio_actual_otro", pai_social_domicilio_actual_otro);
		values.put("pai_social_domicilio_actula_residencia", pai_social_domicilio_actula_residencia);

		String apoyo = "";
		if(pai_social_apoyo_tipo1!=null && !pai_social_apoyo_tipo1.trim().equals(""))
		{
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + pai_social_apoyo_tipo1;
			apoyo += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_titularidad1;
			apoyo += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_coste1;
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_aportacion1 + "</li>";
		}
		if(pai_social_apoyo_tipo2!=null && !pai_social_apoyo_tipo2.trim().equals(""))
		{
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + pai_social_apoyo_tipo2;
			apoyo += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_titularidad2;
			apoyo += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_coste2;
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_aportacion2 + "</li>";
		}
		if(pai_social_apoyo_tipo3!=null && !pai_social_apoyo_tipo3.trim().equals(""))
		{
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + pai_social_apoyo_tipo3;
			apoyo += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_titularidad3;
			apoyo += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_coste3;
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_aportacion3 + "</li>";
		}
		if(pai_social_apoyo_tipo4!=null && !pai_social_apoyo_tipo4.trim().equals(""))
		{
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + pai_social_apoyo_tipo4;
			apoyo += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_titularidad4;
			apoyo += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_coste4;
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_aportacion4 + "</li>";
		}
		values.put("apoyo", apoyo);


		String apoyo_dom = "";
		if(pai_social_apoyo_dom_prestacion1!=null && !pai_social_apoyo_dom_prestacion1.trim().equals(""))
		{
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_dom_prestacion1;
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + pai_social_apoyo_dom_intensidad1;
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_dom_coste1;
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_dom_aportacion1 + "</li>";
		}
		if(pai_social_apoyo_dom_prestacion2!=null && !pai_social_apoyo_dom_prestacion2.trim().equals(""))
		{
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_dom_prestacion2;
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + pai_social_apoyo_dom_intensidad2;
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_dom_coste2;
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_dom_aportacion2 + "</li>";
		}
		if(pai_social_apoyo_dom_prestacion3!=null && !pai_social_apoyo_dom_prestacion3.trim().equals(""))
		{
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_dom_prestacion3;
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + pai_social_apoyo_dom_intensidad3;
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_dom_coste3;
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_dom_aportacion3 + "</li>";
		}
		if(pai_social_apoyo_dom_prestacion4!=null && !pai_social_apoyo_dom_prestacion4.trim().equals(""))
		{
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_dom_prestacion4;
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + pai_social_apoyo_dom_intensidad4;
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + pai_social_apoyo_dom_coste4;
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + pai_social_apoyo_dom_aportacion4 + "</li>";
		}
		values.put("apoyo_dom", apoyo_dom);




		String apoyo_otras = "";
		if(pai_social_apoyo_otras_prestacion1!=null && !pai_social_apoyo_otras_prestacion1.trim().equals(""))
		{
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_otras_prestacion1;
			apoyo_otras += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_otras_titularidad1;
			apoyo_otras += " / <b>INTENSIDAD: </b> " + pai_social_apoyo_otras_intensidad1;
		}
		if(pai_social_apoyo_otras_prestacion2!=null && !pai_social_apoyo_otras_prestacion2.trim().equals(""))
		{
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_otras_prestacion2;
			apoyo_otras += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_otras_titularidad2;
			apoyo_otras += " / <b>INTENSIDAD: </b> " + pai_social_apoyo_otras_intensidad2;
		}
		if(pai_social_apoyo_otras_prestacion3!=null && !pai_social_apoyo_otras_prestacion3.trim().equals(""))
		{
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_otras_prestacion3;
			apoyo_otras += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_otras_titularidad3;
			apoyo_otras += " / <b>INTENSIDAD: </b> " + pai_social_apoyo_otras_intensidad3;
		}
		if(pai_social_apoyo_otras_prestacion4!=null && !pai_social_apoyo_otras_prestacion4.trim().equals(""))
		{
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + pai_social_apoyo_otras_prestacion4;
			apoyo_otras += " / <b>TITULARIDAD: </b> " + pai_social_apoyo_otras_titularidad4;
			apoyo_otras += " / <b>INTENSIDAD: </b> " + pai_social_apoyo_otras_intensidad4;
		}

		values.put("apoyo_otras", apoyo_otras);


		values.put("pai_social_ingresos", pai_social_ingresos);
		values.put("pai_social_ingresos_familia", pai_social_ingresos_familia);
		values.put("pai_social_ingresos_cubre", pai_social_ingresos_cubre);
		values.put("pai_social_nivel_estudios", pai_social_nivel_estudios);
		values.put("pai_social_relaciones", pai_social_relaciones);
		values.put("pai_social_necesidades", pai_social_necesidades);
		values.put("pai_social_objetivos", pai_social_objetivos);
		values.put("pai_social_valoraciones", pai_social_valoraciones);
		values.put("pai_social_actuaciones", pai_social_actuaciones);
		values.put("pai_social_incidencias", pai_social_incidencias);

		String paiSocialUrl = this.getCDNURL("pai_social", patient.get_id(), values); // AQUÍ ESTÁ EL CAMBIO
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setPai_social_url(paiSocialUrl); // Y AQUÍ
		return res;
	}


	public PatientDTO savePaiPortada(
			String id,
			String pai_portada_fecha,
			String pai_portada_representante_guardador,
			String pai_portada_cuidador_nombre,
			String pai_portada_cuidador_edad,
			String pai_portada_cuidador_dni,
			String pai_portada_cuidador_domicilio,
			String pai_portada_cuidador_estado_civil,
			String pai_portada_cuidador_profesion,
			String pai_portada_cuidador_relacion,
			String pai_portada_cuidador_convive_otros,
			String pai_portada_nss,
			String pai_portada_seguro_medico,
			String pai_portada_datos_medicos_enfermedades,
			String pai_portada_datos_medicos_grado_minusvalida,
			String pai_portada_datos_medicos_grado_dependencia,
			String pai_portada_profesional_1,
			String pai_portada_categoria_1,
			String pai_portada_profesional_2,
			String pai_portada_categoria_2,
			String pai_portada_profesional_3,
			String pai_portada_categoria_3,
			String pai_portada_profesional_4,
			String pai_portada_categoria_4,
			String pai_portada_profesional_5,
			String pai_portada_categoria_5
	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		// Actualiza los campos del paciente con los valores proporcionados
		patient.setPai_portada_fecha(pai_portada_fecha);
		patient.setPai_portada_representante_guardador(pai_portada_representante_guardador);
		patient.setPai_portada_cuidador_nombre(pai_portada_cuidador_nombre);
		patient.setPai_portada_cuidador_edad(pai_portada_cuidador_edad);
		patient.setPai_portada_cuidador_dni(pai_portada_cuidador_dni);
		patient.setPai_portada_cuidador_domicilio(pai_portada_cuidador_domicilio);
		patient.setPai_portada_cuidador_estado_civil(pai_portada_cuidador_estado_civil);
		patient.setPai_portada_cuidador_profesion(pai_portada_cuidador_profesion);
		patient.setPai_portada_cuidador_relacion(pai_portada_cuidador_relacion);
		patient.setPai_portada_cuidador_convive_otros(pai_portada_cuidador_convive_otros);
		patient.setPai_portada_nss(pai_portada_nss);
		patient.setPai_portada_seguro_medico(pai_portada_seguro_medico);
		patient.setPai_portada_datos_medicos_enfermedades(pai_portada_datos_medicos_enfermedades);
		patient.setPai_portada_datos_medicos_grado_minusvalida(pai_portada_datos_medicos_grado_minusvalida);
		patient.setPai_portada_datos_medicos_grado_dependencia(pai_portada_datos_medicos_grado_dependencia);
		patient.setPai_portada_profesional_1(pai_portada_profesional_1);
		patient.setPai_portada_categoria_1(pai_portada_categoria_1);
		patient.setPai_portada_profesional_2(pai_portada_profesional_2);
		patient.setPai_portada_categoria_2(pai_portada_categoria_2);
		patient.setPai_portada_profesional_3(pai_portada_profesional_3);
		patient.setPai_portada_categoria_3(pai_portada_categoria_3);
		patient.setPai_portada_profesional_4(pai_portada_profesional_4);
		patient.setPai_portada_categoria_4(pai_portada_categoria_4);
		patient.setPai_portada_profesional_5(pai_portada_profesional_5);
		patient.setPai_portada_categoria_5(pai_portada_categoria_5);

		patient = this.usersRepository.save(patient);

		City city = this.citiesRepository.findOne(patient.getIdcity());
		State state = this.statesRepository.findOne(patient.getIdstate());
		Country country = this.countriesRepository.findOne(patient.getIdcountry());
		User relative = this.usersRepository.findOne(patient.getIdrelative());

		HashMap<String, String> values = new HashMap<>();
		// Aquí puedes formatear la fecha según tus necesidades
		if (patient.getFs_fecha_inscripcion() != null) {
			values.put("FS_FECHA_INSCRIPCION", patient.getFs_fecha_inscripcion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());
		values.put("FS_NUM_EXPEDIENTE", patient.getFs_num_expediente());

		values.put("BIRTHDATE", patient.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		values.put("AGE", Period.between(patient.getBirthdate(), LocalDate.now()).getYears()+"");
		values.put("DOCUMENTID", patient.getDocumentid());
		values.put("NATIONALITY", patient.getNationality());
		values.put("FS_NUM_SS", patient.getFs_num_ss());
		values.put("FS_ESTADO_CIVIL", patient.getFs_estado_civil());
		values.put("RELATIVEFULLNAME", relative!=null?relative.getFullname() + " (" + patient.getRelativerelation() + ")":"");
		values.put("POSTALADDRESS", patient.getPostaladdress());
		values.put("POSTALCODE", patient.getPostalcode());
		values.put("CITYNAME", city!=null?city.getName():"");
		values.put("STATENAME", state!=null?state.getName():"");
		values.put("PHONE", patient.getPhone());

		if(patient.getFs_incapacitacion_judicial()!=null && patient.getFs_incapacitacion_judicial()==true)
			values.put("fs_incapacitacion_judicial", "SÍ");
		else if(patient.getFs_incapacitacion_judicial()!=null && patient.getFs_incapacitacion_judicial()==false)
			values.put("fs_incapacitacion_judicial", "NO");
		else
			values.put("fs_incapacitacion_judicial", "");


		// Agrega todos los campos de pai_portada al HashMap
		values.put("pai_portada_fecha", pai_portada_fecha);
		values.put("pai_portada_representante_guardador", pai_portada_representante_guardador);
		values.put("pai_portada_cuidador_nombre", pai_portada_cuidador_nombre);
		values.put("pai_portada_cuidador_edad", pai_portada_cuidador_edad);
		values.put("pai_portada_cuidador_dni", pai_portada_cuidador_dni);
		values.put("pai_portada_cuidador_domicilio", pai_portada_cuidador_domicilio);
		values.put("pai_portada_cuidador_estado_civil", pai_portada_cuidador_estado_civil);
		values.put("pai_portada_cuidador_profesion", pai_portada_cuidador_profesion);
		values.put("pai_portada_cuidador_relacion", pai_portada_cuidador_relacion);
		values.put("pai_portada_cuidador_convive_otros", pai_portada_cuidador_convive_otros);
		values.put("pai_portada_nss", pai_portada_nss);
		values.put("pai_portada_seguro_medico", pai_portada_seguro_medico);
		values.put("pai_portada_datos_medicos_enfermedades", pai_portada_datos_medicos_enfermedades);
		values.put("pai_portada_datos_medicos_grado_minusvalida", pai_portada_datos_medicos_grado_minusvalida);
		values.put("pai_portada_datos_medicos_grado_dependencia", pai_portada_datos_medicos_grado_dependencia);

		String pai_profesional_1 = "";
		if(pai_portada_profesional_1!=null && !pai_portada_profesional_1.trim().equals(""))
		{
			pai_profesional_1 += "<li><b>Nombre y apellidos:</b>" + pai_portada_profesional_1 + "</li>";
			pai_profesional_1 += "<li><b>Categoría profesional:</b>" + pai_portada_categoria_1 + "</li>";
			pai_profesional_1 += "<li><b>Firma:</b>"  + "</li>";
		}
		values.put("pai_profesional_1", pai_profesional_1);

		String pai_profesional_2 = "";
		if (pai_portada_profesional_2 != null && !pai_portada_profesional_2.trim().equals("")) {
			pai_profesional_2 += "<li><b>Nombre y apellidos:</b>" + pai_portada_profesional_2 + "</li>";
			pai_profesional_2 += "<li><b>Categoría profesional:</b>" + pai_portada_categoria_2 + "</li>";
			pai_profesional_2 += "<li><b>Firma:</b>"  + "</li>";
		}
		values.put("pai_profesional_2", pai_profesional_2);

		String pai_profesional_3 = "";
		if (pai_portada_profesional_3 != null && !pai_portada_profesional_3.trim().equals("")) {
			pai_profesional_3 += "<li><b>Nombre y apellidos:</b>" + pai_portada_profesional_3 + "</li>";
			pai_profesional_3 += "<li><b>Categoría profesional:</b>" + pai_portada_categoria_3 + "</li>";
			pai_profesional_3 += "<li><b>Firma:</b>"  + "</li>";
		}
		values.put("pai_profesional_3", pai_profesional_3);

		String pai_profesional_4 = "";
		if (pai_portada_profesional_4 != null && !pai_portada_profesional_4.trim().equals("")) {
			pai_profesional_4 += "<li><b>Nombre y apellidos:</b>" + pai_portada_profesional_4 + "</li>";
			pai_profesional_4 += "<li><b>Categoría profesional:</b>" + pai_portada_categoria_4 + "</li>";
			pai_profesional_4 += "<li><b>Firma:</b>"  + "</li>";
		}
		values.put("pai_profesional_4", pai_profesional_4);

		String pai_profesional_5 = "";
		if (pai_portada_profesional_5 != null && !pai_portada_profesional_5.trim().equals("")) {
			pai_profesional_5 += "<li><b>Nombre y apellidos:</b>" + pai_portada_profesional_5 + "</li>";
			pai_profesional_5 += "<li><b>Categoría profesional:</b>" + pai_portada_categoria_5 + "</li>";
			pai_profesional_5 += "<li><b>Firma:</b>"  + "</li>";
		}
		values.put("pai_profesional_5", pai_profesional_5);


		//PAI SOCIAL

		values.put("pai_social_historia", patient.getPai_social_historia());
		values.put("pai_social_informes", patient.getPai_social_informes());
		values.put("pai_social_informes_text", patient.getPai_social_informes_text());
		values.put("pai_social_valoracion_disca", patient.getPai_social_valoracion_disca());
		values.put("pai_social_valoracion_disca_fecha", patient.getPai_social_valoracion_disca_fecha());
		values.put("pai_social_valoracion_disca_ca", patient.getPai_social_valoracion_disca_ca());
		values.put("pai_social_valoracion_disca_grado", patient.getPai_social_valoracion_disca_grado());
		values.put("pai_social_3_persona", patient.getPai_social_3_persona());
		values.put("pai_social_ayudas_tecnicas", patient.getPai_social_ayudas_tecnicas());
		values.put("pai_social_ayudas_tecnicas_text", patient.getPai_social_ayudas_tecnicas_text());
		values.put("pai_social_movilidad", patient.getPai_social_movilidad());
		values.put("pai_social_ley_dependencai", patient.getPai_social_ley_dependencai());
		values.put("pai_social_grado_y_nivel", patient.getPai_social_grado_y_nivel());
		values.put("pai_social_cuidador", patient.getPai_social_cuidador());
		values.put("pai_social_relacion_cuidador", patient.getPai_social_relacion_cuidador());
		values.put("pai_social_indicadores", patient.getPai_social_indicadores());
		values.put("pai_social_apoyos", patient.getPai_social_apoyos());
		values.put("pai_social_vive", patient.getPai_social_vive());
		values.put("pai_social_domicilio_obstaculos", patient.getPai_social_domicilio_obstaculos());
		values.put("pai_social_domicilio_ayudas_tecnicas", patient.getPai_social_domicilio_ayudas_tecnicas());
		values.put("pai_social_domicilio_confort", patient.getPai_social_domicilio_confort());
		values.put("pai_social_domicilio_actual_quiere", patient.getPai_social_domicilio_actual_quiere());
		values.put("pai_social_domicilio_actual_otro", patient.getPai_social_domicilio_actual_otro());
		values.put("pai_social_domicilio_actula_residencia", patient.getPai_social_domicilio_actula_residencia());

		String apoyo = "";
		if (patient.getPai_social_apoyo_tipo1() != null && !patient.getPai_social_apoyo_tipo1().trim().equals("")) {
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + patient.getPai_social_apoyo_tipo1();
			apoyo += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_titularidad1();
			apoyo += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_coste1();
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_aportacion1() + "</li>";
		}
		if (patient.getPai_social_apoyo_tipo2() != null && !patient.getPai_social_apoyo_tipo2().trim().equals("")) {
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + patient.getPai_social_apoyo_tipo2();
			apoyo += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_titularidad2();
			apoyo += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_coste2();
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_aportacion2() + "</li>";
		}
		if (patient.getPai_social_apoyo_tipo3() != null && !patient.getPai_social_apoyo_tipo3().trim().equals("")) {
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + patient.getPai_social_apoyo_tipo3();
			apoyo += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_titularidad3();
			apoyo += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_coste3();
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_aportacion3() + "</li>";
		}
		if (patient.getPai_social_apoyo_tipo4() != null && !patient.getPai_social_apoyo_tipo4().trim().equals("")) {
			apoyo += "<li><b>TIPO DE CENTRO Y PLAZA: </b> " + patient.getPai_social_apoyo_tipo4();
			apoyo += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_titularidad4();
			apoyo += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_coste4();
			apoyo += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_aportacion4() + "</li>";
		}
		values.put("apoyo", apoyo);

		String apoyo_dom = "";
		if (patient.getPai_social_apoyo_dom_prestacion1() != null && !patient.getPai_social_apoyo_dom_prestacion1().trim().equals("")) {
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_dom_prestacion1();
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + patient.getPai_social_apoyo_dom_intensidad1();
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_dom_coste1();
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_dom_aportacion1() + "</li>";
		}
		if (patient.getPai_social_apoyo_dom_prestacion2() != null && !patient.getPai_social_apoyo_dom_prestacion2().trim().equals("")) {
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_dom_prestacion2();
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + patient.getPai_social_apoyo_dom_intensidad2();
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_dom_coste2();
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_dom_aportacion2() + "</li>";
		}
		if (patient.getPai_social_apoyo_dom_prestacion3() != null && !patient.getPai_social_apoyo_dom_prestacion3().trim().equals("")) {
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_dom_prestacion3();
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + patient.getPai_social_apoyo_dom_intensidad3();
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_dom_coste3();
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_dom_aportacion3() + "</li>";
		}
		if (patient.getPai_social_apoyo_dom_prestacion4() != null && !patient.getPai_social_apoyo_dom_prestacion4().trim().equals("")) {
			apoyo_dom += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_dom_prestacion4();
			apoyo_dom += " / <b>INTENSIDAD (horas/semana): </b> " + patient.getPai_social_apoyo_dom_intensidad4();
			apoyo_dom += " / <b>COSTE MENSUAL: </b> " + patient.getPai_social_apoyo_dom_coste4();
			apoyo_dom += " / <b>APORTACIÓN DEL USUARIO/A: </b> " + patient.getPai_social_apoyo_dom_aportacion4() + "</li>";
		}
		values.put("apoyo_dom", apoyo_dom);

		String apoyo_otras = "";
		if (patient.getPai_social_apoyo_otras_prestacion1() != null && !patient.getPai_social_apoyo_otras_prestacion1().trim().equals("")) {
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_otras_prestacion1();
			apoyo_otras += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_otras_titularidad1();
			apoyo_otras += " / <b>INTENSIDAD: </b> " + patient.getPai_social_apoyo_otras_intensidad1();
		}
		if (patient.getPai_social_apoyo_otras_prestacion2() != null && !patient.getPai_social_apoyo_otras_prestacion2().trim().equals("")) {
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_otras_prestacion2();
			apoyo_otras += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_otras_titularidad2();
			apoyo_otras += " / <b>INTENSIDAD: </b> " + patient.getPai_social_apoyo_otras_intensidad2();
		}
		if (patient.getPai_social_apoyo_otras_prestacion3() != null && !patient.getPai_social_apoyo_otras_prestacion3().trim().equals("")) {
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_otras_prestacion3();
			apoyo_otras += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_otras_titularidad3();
			apoyo_otras += " / <b>INTENSIDAD: </b> " + patient.getPai_social_apoyo_otras_intensidad3();
		}
		if (patient.getPai_social_apoyo_otras_prestacion4() != null && !patient.getPai_social_apoyo_otras_prestacion4().trim().equals("")) {
			apoyo_otras += "<li><b>PRESTACIÓN: </b> " + patient.getPai_social_apoyo_otras_prestacion4();
			apoyo_otras += " / <b>TITULARIDAD: </b> " + patient.getPai_social_apoyo_otras_titularidad4();
			apoyo_otras += " / <b>INTENSIDAD: </b> " + patient.getPai_social_apoyo_otras_intensidad4();
		}
		values.put("apoyo_otras", apoyo_otras);

		values.put("pai_social_ingresos", patient.getPai_social_ingresos());
		values.put("pai_social_ingresos_familia", patient.getPai_social_ingresos_familia());
		values.put("pai_social_ingresos_cubre", patient.getPai_social_ingresos_cubre());
		values.put("pai_social_nivel_estudios", patient.getPai_social_nivel_estudios());
		values.put("pai_social_relaciones", patient.getPai_social_relaciones());
		values.put("pai_social_necesidades", patient.getPai_social_necesidades());
		values.put("pai_social_objetivos", patient.getPai_social_objetivos());
		values.put("pai_social_valoraciones", patient.getPai_social_valoraciones());
		values.put("pai_social_actuaciones", patient.getPai_social_actuaciones());
		values.put("pai_social_incidencias", patient.getPai_social_incidencias());


		//PAI PSICO

		values.put("pai_psico_acude", patient.getPai_psico_acude());
		values.put("pai_psico_sintomas", patient.getPai_psico_sintomas());
		values.put("pai_psico_diagnostico", patient.getPai_psico_diagnostico());
		values.put("pai_psico_quien_diagnostica", patient.getPai_psico_quien_diagnostica());
		values.put("pai_psico_forma_evalucion", patient.getPai_psico_forma_evalucion());
		values.put("pai_psico_sintomatologia_actual", patient.getPai_psico_sintomatologia_actual());
		values.put("pai_psico_antecedentes", patient.getPai_psico_antecedentes());
		values.put("pai_psico_breve_historial", patient.getPai_psico_breve_historial());
		values.put("pai_psico_orientacion", patient.getPai_psico_orientacion());
		values.put("pai_psico_lenguaje", patient.getPai_psico_lenguaje());
		values.put("pai_psico_memoria", patient.getPai_psico_memoria());
		values.put("pai_psico_atencion", patient.getPai_psico_atencion());
		values.put("pai_psico_praxi", patient.getPai_psico_praxi());
		values.put("pai_psico_pensamiento_abstracto", patient.getPai_psico_pensamiento_abstracto());
		values.put("pai_psico_percepcion", patient.getPai_psico_percepcion());
		values.put("pai_psico_funcion_ejecutiva", patient.getPai_psico_funcion_ejecutiva());
		values.put("pai_psico_escala_folstein", patient.getPai_psico_escala_folstein());
		values.put("pai_psico_evaluacion_conductual", patient.getPai_psico_evaluacion_conductual());
		values.put("pai_psico_plan_act_valoracion_s1", patient.getPai_psico_plan_act_valoracion_s1());
		values.put("pai_psico_plan_act_valoracion_s2", patient.getPai_psico_plan_act_valoracion_s2());
		values.put("pai_psico_plan_act_instrumentos_s1", patient.getPai_psico_plan_act_instrumentos_s1());
		values.put("pai_psico_plan_act_instrumentos_s2", patient.getPai_psico_plan_act_instrumentos_s2());
		values.put("pai_psico_plan_act_objetivos_s1", patient.getPai_psico_plan_act_objetivos_s1());
		values.put("pai_psico_plan_act_objetivos_s2", patient.getPai_psico_plan_act_objetivos_s2());
		values.put("pai_psico_plan_act_actividades_s1", patient.getPai_psico_plan_act_actividades_s1());
		values.put("pai_psico_plan_act_actividades_s2", patient.getPai_psico_plan_act_actividades_s2());
		values.put("pai_psico_plan_act_incidencias_s1", patient.getPai_psico_plan_act_incidencias_s1());
		values.put("pai_psico_plan_act_incidencias_s2", patient.getPai_psico_plan_act_incidencias_s2());
		values.put("pai_psico_valoraciones", patient.getPai_psico_valoraciones());
		values.put("pai_psico_actuaciones", patient.getPai_psico_actuaciones());
		values.put("pai_psico_incidencias", patient.getPai_psico_incidencias());

		//PAI ENFER

// Agrega todos los campos de pai_enfer al HashMap
		values.put("pai_enfer_diagnostico", patient.getPai_enfer_diagnostico());
		values.put("pai_enfer_problemas_audio", patient.getPai_enfer_problemas_audio());
		values.put("pai_enfer_problemas_audio_text", patient.getPai_enfer_problemas_audio_text());
		values.put("pai_enfer_uso_audifono", patient.getPai_enfer_uso_audifono());
		values.put("pai_enfer_problemas_vision", patient.getPai_enfer_problemas_vision());
		values.put("pai_enfer_problemas_vision_text", patient.getPai_enfer_problemas_vision_text());
		values.put("pai_enfer_uso_gafas", patient.getPai_enfer_uso_gafas());
		values.put("pai_enfer_tension", patient.getPai_enfer_tension());
		values.put("pai_enfer_uso_medicacion", patient.getPai_enfer_uso_medicacion());
		values.put("pai_enfer_diabetes", patient.getPai_enfer_diabetes());
		values.put("pai_enfer_diabetes_text", patient.getPai_enfer_diabetes_text());
		values.put("pai_enfer_alergias", patient.getPai_enfer_alergias());
		values.put("pai_enfer_otras_enfermedades", patient.getPai_enfer_otras_enfermedades());

		String med = "";
		if (patient.getPai_enfer_tratamiento_medicamento_1() != null && !patient.getPai_enfer_tratamiento_medicamento_1().trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + patient.getPai_enfer_tratamiento_medicamento_1();
			med += " / <b>DOSIS: </b> " + patient.getPai_enfer_tratamiento_dosis_1();
			med += " / <b>FECHA INICIO: </b> " + patient.getPai_enfer_tratamiento_fecha_1();
			med += " / <b>LA TOMA PARA: </b> " + patient.getPai_enfer_tratamiento_para_1() + "</li>";
		}
		if (patient.getPai_enfer_tratamiento_medicamento_2() != null && !patient.getPai_enfer_tratamiento_medicamento_2().trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + patient.getPai_enfer_tratamiento_medicamento_2();
			med += " / <b>DOSIS: </b> " + patient.getPai_enfer_tratamiento_dosis_2();
			med += " / <b>FECHA INICIO: </b> " + patient.getPai_enfer_tratamiento_fecha_2();
			med += " / <b>LA TOMA PARA: </b> " + patient.getPai_enfer_tratamiento_para_2() + "</li>";
		}
		if (patient.getPai_enfer_tratamiento_medicamento_3() != null && !patient.getPai_enfer_tratamiento_medicamento_3().trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + patient.getPai_enfer_tratamiento_medicamento_3();
			med += " / <b>DOSIS: </b> " + patient.getPai_enfer_tratamiento_dosis_3();
			med += " / <b>FECHA INICIO: </b> " + patient.getPai_enfer_tratamiento_fecha_3();
			med += " / <b>LA TOMA PARA: </b> " + patient.getPai_enfer_tratamiento_para_3() + "</li>";
		}
		if (patient.getPai_enfer_tratamiento_medicamento_4() != null && !patient.getPai_enfer_tratamiento_medicamento_4().trim().equals("")) {
			med += "<li><b>MEDICAMENTO: </b> " + patient.getPai_enfer_tratamiento_medicamento_4();
			med += " / <b>DOSIS: </b> " + patient.getPai_enfer_tratamiento_dosis_4();
			med += " / <b>FECHA INICIO: </b> " + patient.getPai_enfer_tratamiento_fecha_4();
			med += " / <b>LA TOMA PARA: </b> " + patient.getPai_enfer_tratamiento_para_4() + "</li>";
		}
		values.put("med", med);

		values.put("pai_enfer_tratamiento_medicamento_2", patient.getPai_enfer_tratamiento_medicamento_2());
		values.put("pai_enfer_tratamiento_medicamento_3", patient.getPai_enfer_tratamiento_medicamento_3());
		values.put("pai_enfer_tratamiento_medicamento_4", patient.getPai_enfer_tratamiento_medicamento_4());
		values.put("pai_enfer_tratamiento_dosis_1", patient.getPai_enfer_tratamiento_dosis_1());
		values.put("pai_enfer_tratamiento_dosis_2", patient.getPai_enfer_tratamiento_dosis_2());
		values.put("pai_enfer_tratamiento_dosis_3", patient.getPai_enfer_tratamiento_dosis_3());
		values.put("pai_enfer_tratamiento_dosis_4", patient.getPai_enfer_tratamiento_dosis_4());
		values.put("pai_enfer_tratamiento_fecha_1", patient.getPai_enfer_tratamiento_fecha_1());
		values.put("pai_enfer_tratamiento_fecha_2", patient.getPai_enfer_tratamiento_fecha_2());
		values.put("pai_enfer_tratamiento_fecha_3", patient.getPai_enfer_tratamiento_fecha_3());
		values.put("pai_enfer_tratamiento_fecha_4", patient.getPai_enfer_tratamiento_fecha_4());
		values.put("pai_enfer_tratamiento_para_1", patient.getPai_enfer_tratamiento_para_1());
		values.put("pai_enfer_tratamiento_para_2", patient.getPai_enfer_tratamiento_para_2());
		values.put("pai_enfer_tratamiento_para_3", patient.getPai_enfer_tratamiento_para_3());
		values.put("pai_enfer_tratamiento_para_4", patient.getPai_enfer_tratamiento_para_4());

		values.put("pai_enfer_medicacion_centro", patient.getPai_enfer_medicacion_centro());
		values.put("pai_enfer_medicacion_centro_text", patient.getPai_enfer_medicacion_centro_text());
		values.put("pai_enfer_medicacion_puntual", patient.getPai_enfer_medicacion_puntual());
		values.put("pai_enfer_wc_esfinteres", patient.getPai_enfer_wc_esfinteres());
		values.put("pai_enfer_wc_retencion", patient.getPai_enfer_wc_retencion());
		values.put("pai_enfer_wc_estrenimiento", patient.getPai_enfer_wc_estrenimiento());
		values.put("pai_enfer_wc_acompanam", patient.getPai_enfer_wc_acompanam());
		values.put("pai_enfer_alim_alergias", patient.getPai_enfer_alim_alergias());
		values.put("pai_enfer_alim_alergias_text", patient.getPai_enfer_alim_alergias_text());
		values.put("pai_enfer_alim_dieta", patient.getPai_enfer_alim_dieta());
		values.put("pai_enfer_alim_dieta_text", patient.getPai_enfer_alim_dieta_text());
		values.put("pai_enfer_alim_problemas_deglucion", patient.getPai_enfer_alim_problemas_deglucion());
		values.put("pai_enfer_alim_espesantes", patient.getPai_enfer_alim_espesantes());
		values.put("pai_enfer_alim_ayuda", patient.getPai_enfer_alim_ayuda());
		values.put("pai_enfer_alim_observaciones", patient.getPai_enfer_alim_observaciones());
		values.put("pai_enfer_valoraciones", patient.getPai_enfer_valoraciones());
		values.put("pai_enfer_actuaciones", patient.getPai_enfer_actuaciones());
		values.put("pai_enfer_incidencias", patient.getPai_enfer_incidencias());

		//PAI TOCUPA

		values.put("RELATIVEFULLNAME", relative != null ? relative.getFullname() + " / " + patient.getRelativerelation() : "");

		values.put("pai_tocupa_nivel_independencia", patient.getPai_tocupa_nivel_independencia());
		values.put("pai_tocupa_plan_motriz", patient.getPai_tocupa_plan_motriz());
		values.put("pai_tocupa_alimentacion", patient.getPai_tocupa_alimentacion());
		values.put("pai_tocupa_wc", patient.getPai_tocupa_wc());
		values.put("pai_tocupa_aseo", patient.getPai_tocupa_aseo());
		values.put("pai_tocupa_deambular", patient.getPai_tocupa_deambular());
		values.put("pai_tocupa_transferencias", patient.getPai_tocupa_transferencias());
		values.put("pai_tocupa_vestido", patient.getPai_tocupa_vestido());
		values.put("pai_tocupa_bano", patient.getPai_tocupa_bano());
		values.put("pai_tocupa_escaolones", patient.getPai_tocupa_escaolones());
		values.put("pai_tocupa_esfinteres", patient.getPai_tocupa_esfinteres());
		values.put("pai_tocupa_dinero", patient.getPai_tocupa_dinero());
		values.put("pai_tocupa_compras", patient.getPai_tocupa_compras());
		values.put("pai_tocupa_telefono", patient.getPai_tocupa_telefono());
		values.put("pai_tocupa_casa", patient.getPai_tocupa_casa());
		values.put("pai_tocupa_calle", patient.getPai_tocupa_calle());
		values.put("pai_tocupa_medicacion", patient.getPai_tocupa_medicacion());
		values.put("pai_tocupa_indice_barthel", patient.getPai_tocupa_indice_barthel());
		values.put("pai_tocupa_escala_actividad", patient.getPai_tocupa_escala_actividad());
		values.put("pai_tocupa_disfruta_tiempo", patient.getPai_tocupa_disfruta_tiempo());
		values.put("pai_tocupa_espacios_ocio", patient.getPai_tocupa_espacios_ocio());
		values.put("pai_tocupa_disfruta_ocio", patient.getPai_tocupa_disfruta_ocio());
		values.put("pai_tocupa_relacion_otros", patient.getPai_tocupa_relacion_otros());
		values.put("pai_tocupa_propios_objetivos", patient.getPai_tocupa_propios_objetivos());
		values.put("pai_tocupa_participa_actividades", patient.getPai_tocupa_participa_actividades());
		values.put("pai_tocupa_actividades_iniciativa_propia", patient.getPai_tocupa_actividades_iniciativa_propia());
		values.put("pai_tocupa_valoraciones", patient.getPai_tocupa_valoraciones());
		values.put("pai_tocupa_actuaciones", patient.getPai_tocupa_actuaciones());
		values.put("pai_tocupa_incidencias", patient.getPai_tocupa_incidencias());


		//PAI FISIO

		values.put("pai_fisio_prob_salud", patient.getPai_fisio_prob_salud());
		values.put("pai_fisio_dolres", patient.getPai_fisio_dolres());
		values.put("pai_fisio_duerme", patient.getPai_fisio_duerme());
		values.put("pai_fisio_nec_aliment", patient.getPai_fisio_nec_aliment());
		values.put("pai_fisio_hab_saludables", patient.getPai_fisio_hab_saludables());
		values.put("pai_fisio_atencion_preven", patient.getPai_fisio_atencion_preven());
		values.put("pai_fisio_acceso_atencion", patient.getPai_fisio_acceso_atencion());
		values.put("pai_fisio_medicacion_requerida", patient.getPai_fisio_medicacion_requerida());
		values.put("pai_fisio_alergias", patient.getPai_fisio_alergias());
		values.put("pai_fisio_upp", patient.getPai_fisio_upp());
		values.put("pai_fisio_autonomo", patient.getPai_fisio_autonomo());
		values.put("pai_fisio_ayudas_tecnicas", patient.getPai_fisio_ayudas_tecnicas());
		values.put("pai_fisio_movilidad_mmss", patient.getPai_fisio_movilidad_mmss());
		values.put("pai_fisio_movilidad_mmii", patient.getPai_fisio_movilidad_mmii());
		values.put("pai_fisio_movilidad_cuello", patient.getPai_fisio_movilidad_cuello());
		values.put("pai_fisio_movilida_tronco", patient.getPai_fisio_movilida_tronco());
		values.put("pai_fisio_equilibrio", patient.getPai_fisio_equilibrio());
		values.put("pai_fisio_bipedestacion", patient.getPai_fisio_bipedestacion());
		values.put("pai_fisio_marcha", patient.getPai_fisio_marcha());
		values.put("pai_fisio_riesgo_caidas", patient.getPai_fisio_riesgo_caidas());
		values.put("pai_fisio_deformidades", patient.getPai_fisio_deformidades());
		values.put("pai_fisio_disfruta_ocio", patient.getPai_fisio_disfruta_ocio());
		values.put("pai_fisio_espacios_ocio", patient.getPai_fisio_espacios_ocio());
		values.put("pai_fisio_relaciones_entorno", patient.getPai_fisio_relaciones_entorno());
		values.put("pai_fisio_objetivos", patient.getPai_fisio_objetivos());
		values.put("pai_fisio_tratamiento", patient.getPai_fisio_tratamiento());
		values.put("pai_fisio_valoraciones", patient.getPai_fisio_valoraciones());
		values.put("pai_fisio_actuaciones", patient.getPai_fisio_actuaciones());
		values.put("pai_fisio_incidencias", patient.getPai_fisio_incidencias());



		String paiPortadaUrl = this.getCDNURL("pai_portada", patient.get_id(), values); //AQUÍ ESTÁ EL CAMBIO
		PatientDTO res = new PatientDTO(patient, city, state, country, relative, null);
		res.setPai_portada_url(paiPortadaUrl); //Y AQUÍ


		return res;
	}






	@Override
	public PatientDTO generatePAIResumen(
			String id

	) throws Exception {

		User patient = this.usersRepository.findOne(id);
		if (patient == null) return null;

		HashMap<String, String> values = new HashMap<String, String>();

		values.put("NAME", patient.getName());
		values.put("SURNAME1", patient.getSurname1());
		values.put("SURNAME2", patient.getSurname2());

		values.put("pai_social_valoraciones", patient.getPai_social_valoraciones());
		values.put("pai_social_actuaciones", patient.getPai_social_actuaciones());
		values.put("pai_social_incidencias", patient.getPai_social_incidencias());

		values.put("pai_enfer_valoraciones", patient.getPai_enfer_valoraciones());
		values.put("pai_enfer_actuaciones", patient.getPai_enfer_actuaciones());
		values.put("pai_enfer_incidencias", patient.getPai_enfer_incidencias());

		values.put("pai_psico_valoraciones", patient.getPai_psico_valoraciones());
		values.put("pai_psico_actuaciones", patient.getPai_psico_actuaciones());
		values.put("pai_psico_incidencias", patient.getPai_psico_incidencias());

		values.put("pai_tocupa_valoraciones", patient.getPai_tocupa_valoraciones());
		values.put("pai_tocupa_actuaciones", patient.getPai_tocupa_actuaciones());
		values.put("pai_tocupa_incidencias", patient.getPai_tocupa_incidencias());

		values.put("pai_fisio_valoraciones", patient.getPai_fisio_valoraciones());
		values.put("pai_fisio_actuaciones", patient.getPai_fisio_actuaciones());
		values.put("pai_fisio_incidencias", patient.getPai_fisio_incidencias());

		String url = this.getCDNURL("pai_resumen", patient.get_id(), values);
		PatientDTO res = new PatientDTO(patient, null, null, null, null, null);
		res.setPai_portada_url(url); //se reutiliza pq da igual
		return res;
	}

}
