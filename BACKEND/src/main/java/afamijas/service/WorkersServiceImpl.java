package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.*;
import afamijas.queuemail.model.dto.SendingResultDTO;
import afamijas.queuemail.services.QueuemailHardyService;
import afamijas.utils.PasswordPolicy;
import afamijas.utils.SendMail;
import afamijas.utils.StringUtils;
import afamijas.utils.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkersServiceImpl implements WorkersService
{

	@Value("${debug.queries}")
	Boolean debug_queries;

	@Value("${media.path}")
	String mediapath;


	@Value("${queuemail.use}")
	private String use_queuemail;


	final MongoTemplate mongoTemplate;

    final UsersRepository usersRepository;

	final FeedingRepository feedingRepository;

	final TempFridgeRepository tempFridgeRepository;

	final TempServicesRepository tempServicesRepository;

	final MealSamplesRepository mealSamplesRepository;

	final LegionellaLogRepository legionellaLogRepository;

	final WCLogRepository wcLogRepository;

	final RouteStopsRepository routeStopsRepository;

	final WorkersAbsencesRepository workersAbsencesRepository;
	
	final CalendarEventsRepository calendarEventsRepository;

	final MenusRepository menusRepository;

	final HealthLogRepository healthLogRepository;

	final DocsRepository docsRepository;

	final ReceiptsRepository receiptsRepository;

	final InvoicesRepository invoicesRepository;
	final MediaService mediaService;

	final NotificationsService notificationsService;

	final DocsPsicoRepository docsPsicoRepository;

	final CitiesRepository citiesRepository;

	final StatesRepository statesRepository;

	final CountriesRepository countriesRepository;
	final AtencionesRepository atencionesRepository;

	final NominasRepository nominasRepository;



	final UsersService usersService;
	final SendMail sendMail;
	final QueuemailHardyService queuemailHardyService;
	final ConfigurationService configurationService;
	final Template template;
	final ErrorsService errorsService;


	final AddressBookRepository addressBookRepository;


	@Autowired
	public WorkersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, FeedingRepository feedingRepository, TempFridgeRepository tempFridgeRepository, TempServicesRepository tempServicesRepository, MealSamplesRepository mealSamplesRepository, LegionellaLogRepository legionellaLogRepository, WCLogRepository wcLogRepository, RouteStopsRepository routeStopsRepository, WorkersAbsencesRepository workersAbsencesRepository, CalendarEventsRepository calendarEventsRepository, MenusRepository menusRepository, HealthLogRepository healthLogRepository, DocsRepository docsRepository, ReceiptsRepository receiptsRepository, InvoicesRepository invoicesRepository, MediaService mediaService, NotificationsService notificationsService, DocsPsicoRepository docsPsicoRepository, CitiesRepository citiesRepository, StatesRepository statesRepository, CountriesRepository countriesRepository, AtencionesRepository atencionesRepository, NominasRepository nominasRepository, UsersService usersService, SendMail sendMail, QueuemailHardyService queuemailHardyService, ConfigurationService configurationService, Template template, ErrorsService errorsService, AddressBookRepository addressBookRepository)
	{
		this.mongoTemplate = mongoTemplate;
		this.usersRepository = usersRepository;
		this.feedingRepository = feedingRepository;
		this.tempFridgeRepository = tempFridgeRepository;
		this.tempServicesRepository = tempServicesRepository;
		this.mealSamplesRepository = mealSamplesRepository;
		this.legionellaLogRepository = legionellaLogRepository;
		this.wcLogRepository = wcLogRepository;
		this.routeStopsRepository = routeStopsRepository;
		this.workersAbsencesRepository = workersAbsencesRepository;
		this.calendarEventsRepository = calendarEventsRepository;
		this.menusRepository = menusRepository;
		this.healthLogRepository = healthLogRepository;
		this.docsRepository = docsRepository;
		this.receiptsRepository = receiptsRepository;
		this.invoicesRepository = invoicesRepository;
		this.mediaService = mediaService;
		this.notificationsService = notificationsService;
		this.docsPsicoRepository = docsPsicoRepository;
		this.citiesRepository = citiesRepository;
		this.statesRepository = statesRepository;
		this.countriesRepository = countriesRepository;
		this.atencionesRepository = atencionesRepository;
        this.nominasRepository = nominasRepository;
        this.usersService = usersService;
		this.sendMail = sendMail;
		this.queuemailHardyService = queuemailHardyService;
		this.configurationService = configurationService;
		this.template = template;
		this.errorsService = errorsService;
        this.addressBookRepository = addressBookRepository;
    }

	//TODO: REVISAR SI LA PAGINACIÓN ESTÁ BIEN HECHA YA QUE NO ESTÁ COMO EN LAS OTRAS
	@Override
	public Page<PatientDTO> getActivePatients(String name_surnames, String documentid, String groupcode, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = new Query().addCriteria(Criteria.where("status").is("A")).with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));

		if(name_surnames!=null)
		{
			Criteria names_or_criteria = new Criteria();
			names_or_criteria.orOperator(Criteria.where("name").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname1").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname2").regex(".*"+name_surnames+".*", "i"));

			query.addCriteria(names_or_criteria);
		}
		if(documentid!=null) query.addCriteria(Criteria.where("documentid").is(documentid));
		if(groupcode!=null) query.addCriteria(Criteria.where("groupcode").is(groupcode));

		List<PatientDTO> list = this.mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, null, null)).toList();

		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> this.mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
	}


	@Override
	public List<PatientDTO> getAllPatients(String groupcode)
	{
		Query query = new Query().with(Sort.by(Sort.Direction.ASC, "name"));
		Criteria criteria = new Criteria().where("roles").in(Arrays.asList("PATIENT")).and("status").is("A");
		if(groupcode!=null) criteria.and("groupcode").is(groupcode);
		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getAllPatients: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		return this.mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, null, null)).toList();
	}

	@Override
	public PatientDTO getPatientById(String id)
	{
		Query query = new Query();
		Criteria criteria = new Criteria().where("_id").is(id);
		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getPatientById: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		User user = this.mongoTemplate.findOne(query, User.class);
		return new PatientDTO(user,
				this.citiesRepository.findOne(user.getIdcity()),
				this.statesRepository.findOne(user.getIdstate()),
				this.countriesRepository.findOne(user.getIdcountry()),
				this.usersRepository.findOne(user.getIdrelative()),
				null);
	}



	@Override
	public List<MemberDTO> getAllMembers()
	{
		Query query = new Query().with(Sort.by(Sort.Direction.ASC, "name"));
		Criteria criteria = new Criteria().where("roles").in(Arrays.asList("MEMBER")).and("status").is("A");
		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getAllMembers: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		return this.mongoTemplate.find(query, User.class).stream().map(x -> new MemberDTO(x, null, null, null)).toList();
	}






	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public String uploadTimetable(MultipartFile file) throws Exception
	{
		Media media = this.mediaService.create(UUID.randomUUID().toString(), "timetable", "file", file);



		return media.getUrl();

		//TODO ¿Eliminar anterior?
		//TODO: ENVIAR NOTIFICACIÓN ??
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public String uploadActivities(MultipartFile file) throws Exception
	{
		Media media = this.mediaService.create(UUID.randomUUID().toString(), "activities", "file", file);

		return media.getUrl();

		//TODO ¿Eliminar anterior?
		//TODO: ENVIAR NOTIFICACIÓN ??
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public WorkerAbsenceDTO addAbsenceByWorker(String idpatient, String idworker, String idroutestop, String comment)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		User worker = this.usersRepository.findOne(idworker);
		if(worker==null) return null;

		RouteStop routeStop = null;
		if(idroutestop!=null) routeStop = this.routeStopsRepository.findOne(idroutestop);

		WorkerAbsence workerAbsence = new WorkerAbsence();
		workerAbsence.setIdpatient(idpatient);
		workerAbsence.setIdworker(idworker);
		workerAbsence.setIdroutestop(idroutestop);
		workerAbsence.setWhen(LocalDateTime.now());
		workerAbsence.setComment(comment);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new WorkerAbsenceDTO(this.workersAbsencesRepository.save(workerAbsence), patient, worker, routeStop);	}

	@Override
	public void deleteAbsence(String idpatient, String idabsence) {

	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveCalendarEvent(String idworker, String idcalendarevent, LocalDateTime start, LocalDateTime end, Boolean allDay, String title, Boolean dayoff, String description, List<String> roles, List<String> idsusers, LocalDateTime publishdate, String url)
	{
		CalendarEvent calendarEvent = null;

		if(roles!=null && roles.contains("WORKER"))
		{
			List all_worker_roles = Arrays.asList(Roles.WORKER_ROLES);
			roles.addAll(all_worker_roles);
			roles = roles.stream().distinct().collect(Collectors.toList());
			roles.remove("WORKER");
		}
		
		if(idcalendarevent!=null)
		{
			calendarEvent= this.calendarEventsRepository.findOne(idcalendarevent);
			if(calendarEvent==null) return;
		}
		else 
			calendarEvent = new CalendarEvent();

		if(publishdate==null) publishdate = LocalDateTime.now();
		
		calendarEvent.setStart(start);
		calendarEvent.setEnd(end);
		calendarEvent.setAllDay(allDay);
		calendarEvent.setTitle(title);
		calendarEvent.setDayoff(dayoff);
		calendarEvent.setDescription(description);
		calendarEvent.setRoles(roles);
		calendarEvent.setIdsusers(idsusers);
		calendarEvent.setPublishdate(publishdate);
		calendarEvent.setUrl(url);

		this.calendarEventsRepository.save(calendarEvent);

		// TODO: GESTIONAR QUÉ HACER CON idworker (log, etc.)

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO NUEVO EVENTO O MODIFICACIÓN DEL MISMO
	}



	@Override
	public List<CalendarEventDTO> getCalendarEvents(String idworker, List<String> roles, Boolean admin)
	{
		Query query = new Query();

		Criteria criteria = new Criteria();

		if(!admin)
		{
			criteria.orOperator(Criteria.where("roles").in(roles),
					Criteria.where("idsusers").in(Arrays.asList(idworker)),
					new Criteria().andOperator(
							new Criteria().orOperator(Criteria.where("roles").exists(false), Criteria.where("roles").size(0)),
							new Criteria().orOperator(Criteria.where("idsusers").exists(false), Criteria.where("idsusers").size(0))
					)

			).and("publishdate").lte(LocalDateTime.now());
		}


		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getCalendarEvents: " +query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		List<CalendarEvent> calendarEventList = this.mongoTemplate.find(query, CalendarEvent.class);

		return calendarEventList.stream().map(x -> new CalendarEventDTO(x)).toList();
	}


	@Override
	public List<UserDTO> getAllUsers(List<String> roles)
	{
		if(roles==null || roles.size()==0)
		{
			roles = new ArrayList<>();
			roles.add("RELATIVE");
			roles.add("WORKER");
		}

		if(roles.contains("WORKER"))
		{
			if(!roles.contains("TRANSPORT")) roles.add("TRANSPORT");
			if(!roles.contains("ADMIN")) roles.add("ADMIN");
			if(!roles.contains("CLEANING")) roles.add("CLEANING");
			if(!roles.contains("NURSING")) roles.add("NURSING");
			if(!roles.contains("NURSING_ASSISTANT")) roles.add("NURSING_ASSISTANT");
			if(!roles.contains("LEGIONELLA_CONTROL")) roles.add("LEGIONELLA_CONTROL");
			if(!roles.contains("KITCHEN")) roles.add("KITCHEN");
			if(!roles.contains("MONITOR")) roles.add("MONITOR");
			if(!roles.contains("SOCIAL_WORKER")) roles.add("SOCIAL_WORKER");
			if(!roles.contains("PSYCHOLOGIST")) roles.add("PSYCHOLOGIST");
			if(!roles.contains("MANAGER")) roles.add("MANAGER");
			if(!roles.contains("PHYSIOTHERAPIST")) roles.add("PHYSIOTHERAPIST");
			if(!roles.contains("OCCUPATIONAL_THERAPIST")) roles.add("OCCUPATIONAL_THERAPIST");
			if(!roles.contains("OPERATOR_EXTRA_1")) roles.add("OPERATOR_EXTRA_1");
		}

		Query query = new Query();

		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("roles").in(roles),
				Criteria.where("roles").nin(Arrays.asList("ROOT"))
		).and("status").is("A");


		query.addCriteria(criteria);

		try { if(debug_queries) System.out.println("getAllUsers: " +query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		List<User> userList = this.mongoTemplate.find(query, User.class);

		return userList.stream().map(x -> new UserDTO(x)).toList();
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteCalendarEvent(String idcalendarevent)
	{
		this.calendarEventsRepository.deleteById(idcalendarevent);
		//TODO: ¿avisos?
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveMenu(String idadmin, String id, String type, String description, LocalDate from, LocalDate to, MultipartFile file) throws Exception
	{
		if(id==null && file==null) return; //si es nuevo y no lleva fichero bye

		Menu menu = id!=null?this.menusRepository.findOne(id, "A"):new Menu();
		if(menu==null) return;

		menu.setType(type);
		menu.setDescription(description);
		menu.setFrom(from);
		menu.setTo(to);

		if(file!=null)
		{
			Media media = this.mediaService.create(UUID.randomUUID().toString(), "menus", "file", file);
			menu.setMenu_url(media.getUrl());
		}


		this.menusRepository.save(menu);
	}



	@Override
	public Page<MedicationDTO> getMedications(String idpatient, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().where("roles").is(Arrays.asList("PATIENT"));
		if(idpatient!=null) criteria.and("_id").is(idpatient);
		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, User.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getMedications: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		List<MedicationDTO>  list = this.mongoTemplate.find(query, User.class).stream().map(x -> new MedicationDTO(x)).toList();

		return new PageImpl<>(list, pageable, total);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void modifyMedication(String idpatient, String medicationDescriptionMorning, String medicationDescriptionEvening)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return;

		patient.setMedication_description_morning(medicationDescriptionMorning);
		patient.setMedication_description_evening(medicationDescriptionEvening);

		this.usersRepository.save(patient);
	}

	@Override
	public Page<FoodDTO> getFoods(String idpatient, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().where("roles").is(Arrays.asList("PATIENT"));
		if(idpatient!=null) criteria.and("_id").is(idpatient);
		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, User.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getFoods: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		List<FoodDTO>  list = this.mongoTemplate.find(query, User.class).stream().map(x -> new FoodDTO(x)).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void modifyFood(String idpatient, String menu_type, String breakfast_description)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return;

		patient.setMenu_type(menu_type);
		patient.setBreakfast_description(breakfast_description);

		this.usersRepository.save(patient);
	}



	@Override
	public Page<FeedingDTO> getFeedings(User worker, String groupcode, String idpatient, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));
		if(idpatient!=null)
			criteria.and("idpatient").is(idpatient);
		else if(groupcode != null)
		{
			Query query2 = new Query();
			query2.addCriteria(new Criteria().where("roles").is(Arrays.asList("PATIENT")).and("groupcode").is(groupcode).and("status").is("A"));
			List<String> idspatients =  this.mongoTemplate.find(query2, User.class).stream().map(x -> x.get_id()).toList();
			criteria.and("idpatient").in(idspatients);
		}

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, Feeding.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getFeedings: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS PATIENT Y WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<FeedingDTO>  list = this.mongoTemplate.find(query, Feeding.class).stream().map(x -> new FeedingDTO(x, this.usersRepository.findOne(x.getIdpatient()), this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerFeeding(String id, String idpatient, String idworker, String dish, String result, String daymeal, String indications, String incidences)
	{
		if(!daymeal.equals("DESAYUNO") && !daymeal.equals("ALMUERZO")) return;
		if(!result.equals("COMPLETO") && !result.equals("PARCIAL") && !result.equals("NADA")) return;
		if(daymeal.equals("ALMUERZO") && (!dish.equals("PRIMERO") && !dish.equals("SEGUNDO") && !dish.equals("POSTRE"))) return;

		Feeding feeding;
		if(id!=null)
		{
			feeding = this.feedingRepository.findOne(id);
			if(feeding==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			if(daymeal.equals("ALMUERZO"))
				criteria.and("idpatient").is(idpatient).and("day").is(LocalDate.now()).and("daymeal").is(daymeal).and("dish").is(dish);
			else
				criteria.and("idpatient").is(idpatient).and("day").is(LocalDate.now()).and("daymeal").is(daymeal);

			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerFeeding: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			feeding = this.mongoTemplate.findOne(query, Feeding.class);

			if(feeding==null)  feeding = new Feeding();
		}

		if(id==null) //rellenamos campos-clave
		{
			feeding.setIdpatient(idpatient);
			feeding.setDay(LocalDate.now()); //SOLO SE ESTABLECE EL DÍA EN NUEVOS REGISTROS. LUEGO NO VARÍA.
			feeding.setDaymeal(daymeal);
			if(daymeal.equals("ALMUERZO")) feeding.setDish(dish); else feeding.setDish(null);
		}
		feeding.setIdworker(idworker);
		feeding.setResult(result);
		feeding.setIndications(indications);
		feeding.setIncidences(incidences);

		this.feedingRepository.save(feeding);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteFeeding(String id)
	{
		Feeding feeding = this.feedingRepository.findOne(id);
		if(feeding!=null) this.feedingRepository.delete(feeding);
	}



	@Override
	public Page<TempFridgeDTO> getTempFridges(User worker, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, TempFridge.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getTempFridges: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<TempFridgeDTO> list = this.mongoTemplate.find(query, TempFridge.class).stream().map(x -> new TempFridgeDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}





	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerTempFridge(String id, String idworker, Double temperature)
	{
		TempFridge tempFridge;
		if(id!=null)
		{
			tempFridge = this.tempFridgeRepository.findOne(id);
			if(tempFridge==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("day").is(LocalDate.now());
			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerTempFridge: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			tempFridge = this.mongoTemplate.findOne(query, TempFridge.class);

			if(tempFridge==null)  tempFridge = new TempFridge();
		}

		if(id==null) //rellenamos campos-clave
		{
			tempFridge.setDay(LocalDate.now()); //SOLO SE ESTABLECE EL DÍA EN NUEVOS REGISTROS. LUEGO NO VARÍA.
		}
		tempFridge.setIdworker(idworker);
		tempFridge.setTemperature(temperature);
		tempFridge.setOk(temperature<=4.0?true:false);

		//TODO: ¿NOTIFICAR SI UNA TEMPERATURA ESTÁ MAL?

		this.tempFridgeRepository.save(tempFridge);
	}




	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteTempFridge(String id)
	{
		TempFridge tempFridge = this.tempFridgeRepository.findOne(id);
		if(tempFridge!=null) this.tempFridgeRepository.delete(tempFridge);
	}








	@Override
	public Page<TempServiceDTO> getTempServices(User worker, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc) {
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, TempService.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getTempServices: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<TempServiceDTO> list = this.mongoTemplate.find(query, TempService.class).stream().map(x -> new TempServiceDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);

	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerTempService(String id, String idworker, String dish, String dishtype, Double tempreception, Double tempservice)
	{
		TempService tempService;
		if(id!=null)
		{
			tempService = this.tempServicesRepository.findOne(id);
			if(tempService==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("day").is(LocalDate.now());
			criteria.and("dish").is(dish);
			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerTempService: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			tempService = this.mongoTemplate.findOne(query, TempService.class);

			if(tempService==null)  tempService = new TempService();
		}

		if(id==null)
		{
			tempService.setDay(LocalDate.now());
		}

		tempService.setDish(dish);
		tempService.setTemperature_reception(tempreception);
		tempService.setDish_type(dishtype);
		tempService.setIdworker(idworker);
		tempService.setOk(true);

		if(tempreception>4.0) tempService.setOk(false);

		if(tempservice!=null)
		{
			tempService.setTemperature_service(tempservice);
			if(dishtype.equals("FRÍO") && tempservice>8.0) tempService.setOk(false);
			if(dishtype.equals("CALIENTE") && tempservice<65.0) tempService.setOk(false);
		}

		//TODO: ¿NOTIFICAR SI UNA TEMPERATURA ESTÁ MAL?

		this.tempServicesRepository.save(tempService);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteTempService(String id)
	{
		TempService tempService = this.tempServicesRepository.findOne(id);
		if(tempService!=null) this.tempServicesRepository.delete(tempService);
	}




	@Override
	public Page<MealSampleDTO> getMealSamples(User user, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, MealSample.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getMealSamples: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<MealSampleDTO> list = this.mongoTemplate.find(query, MealSample.class).stream().map(x -> new MealSampleDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerMealSample(String id, String idworker, String dish, Boolean orgenolepticoOk, Boolean cuerposExtraOk, String comments)
	{
		MealSample mealSample;
		if(id!=null)
		{
			mealSample = this.mealSamplesRepository.findOne(id);
			if(mealSample==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("day").is(LocalDate.now());
			criteria.and("dish").is(dish);
			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerMealSample: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			mealSample = this.mongoTemplate.findOne(query, MealSample.class);

			if(mealSample==null)  mealSample = new MealSample();
		}

		if(id==null)
		{
			mealSample.setDay(LocalDate.now());
		}

		mealSample.setDish(dish);
		if(orgenolepticoOk!=null)
		{
			mealSample.setOrgenolepticoOk(orgenolepticoOk);
			//TODO: ¿NOTIFICAR SI ESTÁ MAL?
		}
		if(cuerposExtraOk!=null)
		{
			mealSample.setCuerposExtraOk(cuerposExtraOk);
			//TODO: ¿NOTIFICAR SI ESTÁ MAL?
		}

		if(comments!=null) mealSample.setComments(comments);

		mealSample.setIdworker(idworker);

		this.mealSamplesRepository.save(mealSample);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteMealSample(String id)
	{
		MealSample mealSample = this.mealSamplesRepository.findOne(id);
		if(mealSample!=null) this.mealSamplesRepository.delete(mealSample);
	}





	@Override
	public Page<LegionellaLogDTO> getLegionellaLogs(User user, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, LegionellaLog.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getLegionellaLogs: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<LegionellaLogDTO> list = this.mongoTemplate.find(query, LegionellaLog.class).stream().map(x -> new LegionellaLogDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerLegionellaLog(String id, String idworker, String point, Double value, Double temperature)
	{
		LegionellaLog legionellaLog;
		if(id!=null)
		{
			legionellaLog = this.legionellaLogRepository.findOne(id);
			if(legionellaLog==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("day").is(LocalDate.now());
			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerLegionellaLog: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			legionellaLog = this.mongoTemplate.findOne(query, LegionellaLog.class);

			if(legionellaLog==null)  legionellaLog = new LegionellaLog();
		}

		if(id==null)
		{
			legionellaLog.setDay(LocalDate.now());
		}

		legionellaLog.setOk(true);
		legionellaLog.setPoint(point);
		if(value!=null)
		{
			legionellaLog.setValue(value);
			if(value<0.2 || value>1) legionellaLog.setOk(false);
			//TODO: ¿NOTIFICAR SI ESTÁ MAL?
		}
		if(temperature!=null)
		{
			legionellaLog.setTemperature(temperature);
			if(temperature<60) legionellaLog.setOk(false);
			//TODO: ¿NOTIFICAR SI ESTÁ MAL?
		}

		legionellaLog.setIdworker(idworker);

		this.legionellaLogRepository.save(legionellaLog);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteLegionellaLog(String id)
	{
		LegionellaLog legionellaLog = this.legionellaLogRepository.findOne(id);
		if(legionellaLog!=null) this.legionellaLogRepository.delete(legionellaLog);
	}

	@Override
	public Page<WCLogDTO> getWCLogs(User user, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, WCLog.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getWCLogs: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<WCLogDTO> list = this.mongoTemplate.find(query, WCLog.class).stream().map(x -> new WCLogDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);

	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerWCLog(String id, String idworker, String point, String hour)
	{
		WCLog wcLog;
		if(id!=null)
		{
			wcLog = this.wcLogRepository.findOne(id);
			if(wcLog==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("day").is(LocalDate.now());
			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerWCLog: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			wcLog = this.mongoTemplate.findOne(query, WCLog.class);

			if(wcLog==null) wcLog = new WCLog();
		}

		if(id==null)
		{
			wcLog.setDay(LocalDate.now());
		}

		wcLog.setHour(hour);
		wcLog.setPoint(point);


		wcLog.setIdworker(idworker);

		this.wcLogRepository.save(wcLog);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteWCLog(String id)
	{
		WCLog wcLog = this.wcLogRepository.findOne(id);
		if(wcLog!=null) this.wcLogRepository.delete(wcLog);
	}





	@Override
	public Page<HealthLogDTO> getHealthLogs(User user, String groupcode, String idpatient, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().andOperator(new Criteria().where("day").gte(dayfrom), new Criteria().where("day").lte(dayto));
		if(idpatient!=null)
			criteria.and("idpatient").is(idpatient);
		else if(groupcode != null)
		{
			Query query2 = new Query();
			query2.addCriteria(new Criteria().where("roles").is(Arrays.asList("PATIENT")).and("groupcode").is(groupcode).and("status").is("A"));
			List<String> idspatients =  this.mongoTemplate.find(query2, User.class).stream().map(x -> x.get_id()).toList();
			criteria.and("idpatient").in(idspatients);
		}

		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, HealthLog.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getHealthLogs: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<HealthLogDTO> list = this.mongoTemplate.find(query, HealthLog.class).stream().map(x -> new HealthLogDTO(x, this.usersRepository.findOne(x.getIdpatient()), this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerHealthLog(String id, String idpatient, String idworker, Double lowPressure, Double highPressure, Double sugar)
	{
		if(lowPressure!=null && lowPressure==0) lowPressure = null;
		if(highPressure!=null && highPressure==0) highPressure = null;
		if(sugar!=null && sugar==0) sugar = null;

		HealthLog healthLog;
		if(id!=null)
		{
			healthLog = this.healthLogRepository.findOne(id);
			if(healthLog==null) return;
		}
		else
		{
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria.and("day").is(LocalDate.now());
			criteria.and("idpatient").is(idpatient);
			query.addCriteria(criteria);
			try { if(debug_queries) System.out.println("registerHealthLog: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
			healthLog = this.mongoTemplate.findOne(query, HealthLog.class);

			if(healthLog==null) healthLog = new HealthLog();
		}

		if(id==null)
		{
			healthLog.setIdpatient(idpatient);
			healthLog.setDay(LocalDate.now());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String hour = formatter.format(LocalTime.now());

		if(lowPressure!=null)
		{
			healthLog.setLow_pressure(lowPressure);
			if(healthLog.getHour_presure()==null) healthLog.setHour_presure(hour);
		}
		if(highPressure!=null)
		{
			healthLog.setHigh_pressure(highPressure);
			if(healthLog.getHour_presure()==null) healthLog.setHour_presure(hour);
		}
		if(sugar!=null)
		{
			healthLog.setSugar(sugar);
			if(healthLog.getHour_sugar()==null) healthLog.setHour_sugar(hour);
		}
		healthLog.setIdworker(idworker);

		this.healthLogRepository.save(healthLog);

	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteHealthLog(String id)
	{
		HealthLog healthLog = this.healthLogRepository.findOne(id);
		if(healthLog!=null) this.healthLogRepository.delete(healthLog);
	}








	@Override
	public Page<DocDTO> getDocs(User user, String text, LocalDate availablefrom, LocalDate availableto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		if(user.getRoles().contains("ADMIN") || user.getRoles().contains("MANAGER"))
		{

			if(text!=null && !text.trim().equals(""))
			{
				Criteria criteria1 = new Criteria().orOperator(
						Criteria.where("title").regex(".*"+text+".*", "i"),
						Criteria.where("description").regex(".*"+text+".*", "i")
				);
				query.addCriteria(criteria1);
			}

			if(availablefrom!=null)
			{
				//no va a ser null nunca por saveDoc
				query.addCriteria(Criteria.where("dayfrom").gte(availablefrom));
			}

			if(availableto!=null)
			{
				//no va a ser null nunca por saveDoc
				query.addCriteria(Criteria.where("dayto").lte(availableto));
			}
		}
		else
		{
			List<Criteria> andCriteria = new ArrayList<>();

			if (text != null && !text.trim().equals("")) {
				Criteria textCriteria = new Criteria().orOperator(
						Criteria.where("title").regex(".*" + text + ".*", "i"),
						Criteria.where("description").regex(".*" + text + ".*", "i")
				);
				andCriteria.add(textCriteria);
			}

			andCriteria.add(Criteria.where("dayfrom").lte(LocalDate.now()));
			andCriteria.add(Criteria.where("dayto").gte(LocalDate.now()));

			Criteria rolesCriteria = new Criteria().orOperator(
					Criteria.where("roles").in(user.getRoles()),
					Criteria.where("roles").size(0),
					Criteria.where("roles").exists(false),
					Criteria.where("idworker").is(user.get_id())
			);
			andCriteria.add(rolesCriteria);

			Criteria finalCriteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));
			query.addCriteria(finalCriteria);
		}


		if(!(text!=null && !text.trim().equals("")) && availablefrom==null && availableto==null && !(!user.getRoles().contains("ADMIN") && !user.getRoles().contains("MANAGER")))
			query.addCriteria(new Criteria());

		try { if(debug_queries) System.out.println("getDocs: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		long total = this.mongoTemplate.count(query, Doc.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<DocDTO> list = this.mongoTemplate.find(query, Doc.class).stream().map(x -> new DocDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveDoc(String id, String idworker, String title, String description, String url, LocalDate dayfrom, LocalDate dayto, List<String> roles, boolean isAdmin, boolean createEvent) throws Exception
	{
		if(dayfrom==null)
		{
			if(dayto==null)
				dayfrom = LocalDate.now();
			else
				dayfrom = dayto;
		}
		if(dayto==null) dayto = LocalDate.of(2100, 1, 1);
		if(roles!=null && roles.size()==0) roles = null;

		if(roles!=null && roles.contains("WORKER"))
		{
			List all_worker_roles = Arrays.asList(Roles.WORKER_ROLES);
			roles.addAll(all_worker_roles);
			roles = roles.stream().distinct().collect(Collectors.toList());
			roles.remove("WORKER");
		}


		Doc doc;
		if(id!=null)
		{
			doc = this.docsRepository.findOne(id);
			if(doc==null) return;

			if(!isAdmin && !doc.getIdworker().equals(idworker)) throw new Exception("NO");
		}
		else
		{
			doc = new Doc();
			doc.setUrl(url);
		}

		if(isAdmin)
		{
			doc.setIdworker(idworker);
			doc.setTitle(title);
			doc.setDescription(description);
			doc.setDayfrom(dayfrom);
			doc.setDayto(dayto);
			doc.setRoles(roles);

			doc = this.docsRepository.save(doc);

			this.notificationsService.create(null, roles, doc.getTitle(), "NORMAL", doc.getDescription(), url);
		}
		else
		{
			doc.setIdworker(idworker);
			doc.setTitle(title);
			doc.setDescription(description);

			doc.setDayfrom(dayfrom);
			doc.setDayto(dayto);

			roles = new ArrayList<>();
			roles.add("ADMIN");
			roles.add("MANAGER");
			doc.setRoles(roles);

			doc = this.docsRepository.save(doc);

			this.notificationsService.create(null, roles, doc.getTitle(), "NORMAL", doc.getDescription(), url);
		}

		if(isAdmin && createEvent)
			this.saveCalendarEvent(idworker, null, doc.getDayfrom().atStartOfDay(), doc.getDayto().equals(LocalDate.of(2100, 1, 1))?null:doc.getDayto().atTime(23, 59, 59), false, doc.getTitle(), false, doc.getDescription() , doc.getRoles(), null, doc.getDayfrom().atStartOfDay(), doc.getUrl());

	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteAdminDoc(String id)
	{
		Doc doc = this.docsRepository.findOne(id);
		if(doc!=null) this.docsRepository.delete(doc);
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteWorkerDoc(String id, String idworker) throws Exception
	{
		Doc doc = this.docsRepository.findOne(id);
		if(doc!=null && doc.getIdworker().equals(idworker)) this.docsRepository.delete(doc);
		else throw new Exception("NO");
	}




	// SOLO PARA ADMIN y MANAGER
	public Page<ReceiptDTO> getReceipts(User user, String idmember, LocalDate dayfrom, LocalDate dayto, String status, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		if(idmember!=null)
			query.addCriteria(Criteria.where("idmember").is(idmember));

		if(dayfrom!=null && dayto!=null)
			query.addCriteria(new Criteria().andOperator(new Criteria().where("duedate").gte(dayfrom), new Criteria().where("duedate").lte(dayto)));
		else if(dayfrom!=null && dayto==null)
			query.addCriteria(Criteria.where("duedate").gte(dayfrom));
		else if(dayto!=null && dayfrom==null)
			query.addCriteria(Criteria.where("duedate").lte(dayto));

		if(status!=null)
			query.addCriteria(Criteria.where("status").is(status));

		long total = this.mongoTemplate.count(query, Receipt.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getReceipts: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<ReceiptDTO> list = this.mongoTemplate.find(query, Receipt.class).stream().map(x -> new ReceiptDTO(x, this.usersRepository.findOne(x.getIdmember()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveReceipt(String id, String idmember, String url, Double total, LocalDate duedate, String status, LocalDate paiddate)
	{
		Receipt receipt;
		String oldurl = null;
		if(id!=null)
		{
			receipt = this.receiptsRepository.findOne(id);
			if(receipt==null) return;
			oldurl = receipt.getUrl();
		}
		else
		{
			receipt = new Receipt();
			receipt.setUrl(url);
			receipt.setIdmember(idmember);
		}

		receipt.setTotal(total);
		receipt.setDuedate(duedate);
		receipt.setStatus(status);

		if(status.equals("PAID") && paiddate==null) paiddate = LocalDate.now();
		receipt.setPaiddate(paiddate);

		receipt = this.receiptsRepository.save(receipt);

		//this.notificationsService.create(idmember, null, status.equals("PAID")?"Recibo pagado":"Recibo pendiente", "NORMAL", duedate.format(DateTimeFormatter.ofPattern("yyyy-MM-d")) + ": " + total + " euros.", receipt.getUrl());
	}




	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteReceipt(String id)
	{
		Receipt receipt = this.receiptsRepository.findOne(id);
		if(receipt!=null) this.receiptsRepository.delete(receipt);
	}






	// SOLO PARA ADMIN y MANAGER
	public Page<InvoiceDTO> getInvoices(User user, String idpatient, LocalDate dayfrom, LocalDate dayto, String status, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		if(idpatient!=null)
			query.addCriteria(Criteria.where("idpatient").is(idpatient));

		if(dayfrom!=null && dayto!=null)
			query.addCriteria(new Criteria().andOperator(new Criteria().where("duedate").gte(dayfrom), new Criteria().where("duedate").lte(dayto)));
		else if(dayfrom!=null && dayto==null)
			query.addCriteria(Criteria.where("duedate").gte(dayfrom));
		else if(dayto!=null && dayfrom==null)
			query.addCriteria(Criteria.where("duedate").lte(dayto));

		if(status!=null)
			query.addCriteria(Criteria.where("status").is(status));

		long total = this.mongoTemplate.count(query, Invoice.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getInvoices: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<InvoiceDTO> list = this.mongoTemplate.find(query, Invoice.class).stream().map(x -> new InvoiceDTO(x, this.usersRepository.findOne(x.getIdpatient()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveInvoice(String id, String idpatient, String url, Double total, LocalDate duedate, String status, LocalDate paiddate)
	{
		Invoice invoice;
		String oldurl = null;
		if(id!=null)
		{
			invoice = this.invoicesRepository.findOne(id);
			if(invoice==null) return;
			oldurl = invoice.getUrl();
		}
		else
		{
			invoice = new Invoice();
			invoice.setUrl(url);
			invoice.setIdpatient(idpatient);
		}

		invoice.setTotal(total);
		invoice.setDuedate(duedate);
		invoice.setStatus(status);

		if(status.equals("PAID") && paiddate==null) paiddate = LocalDate.now();
		invoice.setPaiddate(paiddate);

		invoice = this.invoicesRepository.save(invoice);

		//this.notificationsService.create(idpatient, null, status.equals("PAID")?"Factura pagada":"Factura pendiente", "NORMAL", duedate.format(DateTimeFormatter.ofPattern("yyyy-MM-d")) + ": " + total + " euros.", invoice.getUrl());
	}




	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteInvoice(String id)
	{
		Invoice invoice = this.invoicesRepository.findOne(id);
		if(invoice!=null) this.invoicesRepository.delete(invoice);
	}





	@Override
	public Page<DocPsicoDTO> getDocsPsico(User user, String idpatient, String type, String text, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		if(idpatient!=null)	query.addCriteria(Criteria.where("idpatient").is(idpatient));
		if(type!=null)	query.addCriteria(Criteria.where("type").is(type));
		if(text!=null && !text.trim().equals("")) query.addCriteria(Criteria.where("description").regex(".*"+text+".*", "i"));
		long total = this.mongoTemplate.count(query, DocPsico.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getDocsPsico: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<DocPsicoDTO> list = this.mongoTemplate.find(query, DocPsico.class).stream().map(x -> new DocPsicoDTO(x, this.usersRepository.findOne(x.getIdworker()), this.usersRepository.findOne(x.getIdpatient()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveDocPsico(String id, String idworker, String idpatient, String type, String description, String url)
	{
		DocPsico doc;
		if(id!=null)
		{
			doc = this.docsPsicoRepository.findOne(id);
			if(doc==null) return;
		}
		else
		{
			doc = new DocPsico();
			doc.setUrl(url);
		}

		doc.setIdworker(idworker);
		doc.setIdpatient(idpatient);
		doc.setType(type);
		doc.setDescription(description);

		doc = this.docsPsicoRepository.save(doc);

		/* TODO ¿ACTIVAR?
		if(id==null)
			this.notificationsService.create(null, Arrays.asList("ADMIN"), doc.getType().equals("PSICO_REPORT")?"Nuevo informe psicológico":"Nueva evaluación psicológica", "NORMAL", doc.getDescription(), url);
		else
			this.notificationsService.create(null, Arrays.asList("ADMIN"), doc.getType().equals("PSICO_REPORT")?"Modificado informe psicológico":"Modificada evaluación psicológica", "NORMAL", doc.getDescription(), url);
		*/
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteDocPsico(String id)
	{
		DocPsico doc = this.docsPsicoRepository.findOne(id);
		if(doc!=null) this.docsPsicoRepository.delete(doc);
	}

	/*
	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void savePermission(String id, String type, String description, LocalDate from, LocalDate to, MultipartFile file) throws Exception
	{
		if(id==null && file==null) return; //si es nuevo y no lleva fichero bye

		Menu menu = id!=null?this.menusRepository.findOne(id, "A"):new Menu();
		if(menu==null) return;

		menu.setType(type);
		menu.setDescription(description);
		menu.setFrom(from);
		menu.setTo(to);

		if(file!=null)
		{
			Media media = this.mediaService.create(UUID.randomUUID().toString(), "menus", "file", file);
			menu.setMenu_url(media.getUrl());
		}

		this.menusRepository.save(menu);
	}*/





	// SOLO PARA ADMIN y MANAGER
	public Page<AtencionDTO> getAtenciones(LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		if(dayfrom!=null && dayto!=null)
			query.addCriteria(new Criteria().andOperator(new Criteria().where("datedone").gte(dayfrom), new Criteria().where("datedone").lte(dayto)));
		else if(dayfrom!=null && dayto==null)
			query.addCriteria(Criteria.where("datedone").gte(dayfrom));
		else if(dayto!=null && dayfrom==null)
			query.addCriteria(Criteria.where("datedone").lte(dayto));

		long total = this.mongoTemplate.count(query, Invoice.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));
		try { if(debug_queries) System.out.println("getAtenciones: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<AtencionDTO> list = this.mongoTemplate.find(query, Atencion.class).stream().map(x -> new AtencionDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}




	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerAtencion(String id, String idworker, String number, LocalDate datedone, String clientfullname, String sex, String nationality, String relationship, String why, String via, String professional, String observations)
	{
		Atencion atencion;
		if(id!=null)
		{
			atencion = this.atencionesRepository.findOne(id);
			if(atencion==null) return;
			atencion.setNumber(number);
		}
		else
		{
			atencion = new Atencion();
			atencion.setIdworker(idworker);

			String last_nunmber = this.atencionesRepository.findHighestNumber();
			if(last_nunmber==null || last_nunmber.trim().equals("")) last_nunmber = "0/" + getInitials(clientfullname);
			last_nunmber = StringUtils.incrementLastNumber(last_nunmber) +  "/" + getInitials(clientfullname);
			atencion.setNumber(last_nunmber);
		}

		atencion.setDatedone(datedone);
		atencion.setClientfullname(clientfullname);
		atencion.setSex(sex);
		atencion.setNationality(nationality);
		atencion.setRelationship(relationship);
		atencion.setWhy(why);
		atencion.setVia(via);
		atencion.setProfessional(professional);
		atencion.setObservations(observations);

		atencion = this.atencionesRepository.save(atencion);

		//this.notificationsService.create(idpatient, null, status.equals("PAID")?"Factura pagada":"Factura pendiente", "NORMAL", duedate.format(DateTimeFormatter.ofPattern("yyyy-MM-d")) + ": " + total + " euros.", invoice.getUrl());
	}


	private String getInitials(String nombresYApellidos)
	{
		if(nombresYApellidos==null || nombresYApellidos.trim().equals(""))  return "";
		String[] partes = nombresYApellidos.split(" ");
		StringBuilder iniciales = new StringBuilder();

		for (String parte : partes) {
			if (!parte.isEmpty()) {
				iniciales.append(parte.charAt(0));  // Agregar la primera letra de cada palabra
			}
		}

		return iniciales.toString().toUpperCase();
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteAtencion(String id)
	{
		Atencion atencion = this.atencionesRepository.findOne(id);
		if(atencion!=null) this.atencionesRepository.delete(atencion);
	}



	//TODO: REVISAR SI LA PAGINACIÓN ESTÁ BIEN HECHA YA QUE NO ESTÁ COMO EN LAS OTRAS
	@Override
	public Page<WorkerDTO> getWorkers(String role, String name_surnames, String documentid, String status, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = null;
		if(role!=null)
			query = new Query().with(pageable).addCriteria(Criteria.where("roles").in(role)).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));
		else
			query = new Query().with(pageable).addCriteria(Criteria.where("roles").in(Roles.WORKER_ROLES)).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));

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


		if(debug_queries) System.out.println("getWokers: " + query.getQueryObject().toJson());
		List<WorkerDTO> list = this.mongoTemplate.find(query, User.class).stream().map(x -> new WorkerDTO(x, null, null, null, this.calendarEventsRepository.findByIdsusersContainingOrderByStartDesc(x.get_id()))).toList();


		Query finalQuery = query;
		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> this.mongoTemplate.count(Query.of(finalQuery).limit(-1).skip(-1), User.class));
	}



	@Override
	public WorkerDTO saveWorker(String id, List<String> roles, String name, String surname1, String surname2, String email, String password, String phone, String documentid, String documenttype,
								String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
								String nss, String categoria_profesional, String tipo_contrato, String jornada_laboral, String horario)
	{
		boolean sendnewpass = id==null;

		User worker = null;
		if(id!=null)
		{
			worker = this.usersRepository.findOne(id);
			if(worker==null) return null;
		}
		else
			worker = new User();

		worker.setRoles(roles);
		worker.setStatus("A");

		worker.setName(name);
		worker.setSurname1(surname1);
		worker.setSurname2(surname2);
		worker.setEmail(email);
		worker.setPhone(phone);
		worker.setDocumentid(documentid);
		worker.setDocumenttype(documenttype);
		worker.setUsername(email);

		if(password!=null && !password.trim().equals(""))
		{
			sendnewpass = false;
			worker.setPassword(new BCryptPasswordEncoder().encode(password));
		}

		worker.setIdcity(idcity);
		worker.setIdstate(idstate);
		worker.setIdcountry(idcountry);
		worker.setPostaladdress(postaladdress);
		worker.setPostalcode(postalcode);

		worker.setNss(nss);
		worker.setCategoria_profesional(categoria_profesional);
		worker.setTipo_contrato(tipo_contrato);
		worker.setJornada_laboral(jornada_laboral);
		worker.setHorario(horario);

		worker = this.usersRepository.save(worker);

		if(sendnewpass) try { this.sendNewPassword(worker); } catch (Exception e) {}

		//ACTUALIZAMOS AGENDA
		try
		{
			List<AddressBook> addressBooks = this.addressBookRepository.findByIduser(worker.get_id());
			if(addressBooks!=null && addressBooks.size()>0)
				for(AddressBook addressBook:addressBooks)
				{
					addressBook.setFullname(worker.getFullname());
					addressBook.setPhone(worker.getPhone());
					addressBook.setEmail(worker.getEmail());
					this.addressBookRepository.save(addressBook);
				}
			else
			{
				AddressBook addressBook = new AddressBook(worker);
				this.addressBookRepository.save(addressBook);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		return new WorkerDTO(worker, this.citiesRepository.findOne(idcity), this.statesRepository.findOne(idstate), this.countriesRepository.findOne(idcountry), this.calendarEventsRepository.findByIdsusersContainingOrderByStartDesc(worker.get_id()));
	}

	@Override
	public void unregisterWorker (String id)
	{
		User member = this.usersRepository.findOne(id);
		if(member!=null)
		{
			if(member.getStatus().equals("D"))
			{
				this.usersRepository.delete(member);
			}
			else
			{
				member.setStatus("D");
				this.usersRepository.save(member);
			}
		}

	}



	private void sendNewPassword(User user) throws Exception
	{
		//String fromemail = this.configurationService.value("fromemail");
		String fromemail = "info@afamijas.org";

		String newpassword = PasswordPolicy.generate();
		user.setPassword(new BCryptPasswordEncoder().encode(newpassword));
		user.setToken(UUID.randomUUID().toString()); //renovamos token
		user = this.usersService.save(user);

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("newpassword", newpassword);
		values.put("username", user.getUsername());
		String body = template.parse("mail_sendnewpassword.html", values);

		String newpasswordsubject = "Sus nuevos datos de acceso";

		if(use_queuemail==null) use_queuemail = this.configurationService.value("queuemail.use");
		if(use_queuemail.equals("true"))
		{
			//ENVIAMOS POR QUEUEMAIL CON SERVICIO REBUSTO QUE IMPLEMENTA COLA LOCAL DE EMAILS FALLIDOS
			SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, fromemail, "AFA Mijas",  user.getEmail(), user.getEmail(), null, null, fromemail,
					newpasswordsubject, "UTF-8", body, "UTF-8", "text/html",
					null, null, null,
					true, false,
					true, null);

			if(sendingResultDTO==null || (sendingResultDTO.getLocalqueued()!=null && sendingResultDTO.getLocalqueued()==false) )
			{
				this.errorsService.sendWarning("Email local-queuing failed", sendingResultDTO==null?"NULL":sendingResultDTO.getError());
				this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, newpasswordsubject, "UTF-8", body, "UTF-8", null, "html");
			}
		}
		else
		{
			this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, newpasswordsubject, "UTF-8", body, "UTF-8", null, "html");
		}

	}



	// SOLO PARA ADMIN y MANAGER
	public Page<NominaDTO> getNominas(User user, String idworker, LocalDate dayfrom, LocalDate dayto, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		if(idworker!=null)
			query.addCriteria(Criteria.where("idworker").is(idworker));

		if(dayfrom!=null && dayto!=null)
			query.addCriteria(new Criteria().andOperator(new Criteria().where("duedate").gte(dayfrom), new Criteria().where("duedate").lte(dayto)));
		else if(dayfrom!=null && dayto==null)
			query.addCriteria(Criteria.where("duedate").gte(dayfrom));
		else if(dayto!=null && dayfrom==null)
			query.addCriteria(Criteria.where("duedate").lte(dayto));

		query.addCriteria(Criteria.where("status").is("A"));

		long total = this.mongoTemplate.count(query, Nomina.class);

		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getNominas: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		//EN LOS USUARIOS WORKER INCLUIMOS TAMBIÉN LOS QUE PUDIERAN ESTAR BORRADOS POR ESO FINDONE NO FILTRA POR STATUS
		List<NominaDTO> list = this.mongoTemplate.find(query, Nomina.class).stream().map(x -> new NominaDTO(x, this.usersRepository.findOne(x.getIdworker()))).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveNomina(String id, String idworker, String url, LocalDate duedate)
	{
		Nomina nomina;
		String oldurl = null;
		if(id!=null)
		{
			nomina = this.nominasRepository.findOne(id);
			if(nomina==null) return;
			oldurl = nomina.getUrl();
		}
		else
		{
			nomina = new Nomina();
			nomina.setUrl(url);
			nomina.setIdworker(idworker);
		}

		nomina.setDuedate(duedate);
		nomina.setStatus("A");

		nomina = this.nominasRepository.save(nomina);

		//this.notificationsService.create(idmember, null, status.equals("PAID")?"Recibo pagado":"Recibo pendiente", "NORMAL", duedate.format(DateTimeFormatter.ofPattern("yyyy-MM-d")) + ": " + total + " euros.", receipt.getUrl());
	}




	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteNomina(String id)
	{
		Nomina nomina = this.nominasRepository.findOne(id);
		if(nomina!=null) this.nominasRepository.delete(nomina);
	}




	@Override
	public List<WorkerDTO> getAllWorkers()
	{
		Query query = new Query().with(Sort.by(Sort.Direction.ASC, "name"));
		Criteria criteria = new Criteria().where("roles").in(Roles.WORKER_ROLES).and("status").is("A");
		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getAllWorkers: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		return this.mongoTemplate.find(query, User.class).stream().map(x -> new WorkerDTO(x, null, null, null, null)).toList();
	}


	@Override
	public Page<WorkerAbsenceDTO> getWorkerAbsences(String idpatient, LocalDateTime from, LocalDateTime to, int page, int size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria1 = new Criteria().where("idpatient").is(idpatient);
		if(from!=null) query.addCriteria(Criteria.where("when").gte(from));
		if(to!=null) query.addCriteria(Criteria.where("when").lte(to));

		//Criteria criteria2 = new Criteria().where("to").gte(LocalDateTime.now());

		//Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
		query.addCriteria(criteria1);

		long total = this.mongoTemplate.count(query, WorkerAbsence.class);
		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getWorkerAbsences: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		List<WorkerAbsenceDTO> list = this.mongoTemplate.find(query, WorkerAbsence.class).stream().map(x -> new WorkerAbsenceDTO(x, this.usersRepository.findOne(x.getIdpatient()), this.usersRepository.findOne(x.getIdworker()), this.routeStopsRepository.findOne(x.getIdroutestop())  )).toList();

		return new PageImpl<>(list, pageable, total);
	}




	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public WorkerAbsenceDTO saveWorkerAbsence(String id, String idpatient, String idworker, String idroutestop, String comment, LocalDateTime when)
	{
		if(when==null) return null;

		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		User worker = this.usersRepository.findOne(idworker);
		if(worker==null) return null;

		WorkerAbsence workerAbsence = id!=null?this.workersAbsencesRepository.findOne(id):new WorkerAbsence();
		if(workerAbsence==null) return null;

		workerAbsence.setIdpatient(idpatient);
		workerAbsence.setIdworker(idworker);
		workerAbsence.setIdroutestop(idroutestop);
		workerAbsence.setComment(comment);
		workerAbsence.setWhen(when);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new WorkerAbsenceDTO(this.workersAbsencesRepository.save(workerAbsence), patient, worker, routeStopsRepository.findOne(idroutestop));
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteWorkerAbsence(String idabsence)
	{
		WorkerAbsence workerAbsence = idabsence!=null?this.workersAbsencesRepository.findOne(idabsence):new WorkerAbsence();
		if(workerAbsence==null) return;

		this.workersAbsencesRepository.deleteById(idabsence);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO ELIMINACIÓN DE AUSENCIA PARA ESTE PACIENTE
	}

}
