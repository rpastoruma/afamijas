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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkersServiceImpl implements WorkersService
{

	@Value("${debug.queries}")
	Boolean debug_queries;

	@Value("${media.path}")
	String mediapath;

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
	final MediaService mediaService;


	@Autowired
	public WorkersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, FeedingRepository feedingRepository, TempFridgeRepository tempFridgeRepository, TempServicesRepository tempServicesRepository, MealSamplesRepository mealSamplesRepository, LegionellaLogRepository legionellaLogRepository, WCLogRepository wcLogRepository, RouteStopsRepository routeStopsRepository, WorkersAbsencesRepository workersAbsencesRepository, CalendarEventsRepository calendarEventsRepository, MenusRepository menusRepository, MediaService mediaService)
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
		this.mediaService = mediaService;
	}

	//TODO: REVISAR SI LA PAGINACIÓN ESTÁ BIEN HECHA YA QUE NO ESTÁ COMO EN LAS OTRAS
	@Override
	public Page<PatientDTO> getActivePatients(String name_surnames, String dni, String groupcode, Integer page, Integer size, String order, String orderasc)
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
		if(dni!=null) query.addCriteria(Criteria.where("dni").is(dni));
		if(groupcode!=null) query.addCriteria(Criteria.where("groupcode").is(groupcode));

		List<PatientDTO> list = this.mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, null)).toList();

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
		return this.mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, null)).toList();
	}





	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerTempFridge(String idworker, Double temperature)
	{
		TempFridge tempFridge = this.tempFridgeRepository.findOne(LocalDate.now());
		if(tempFridge==null) tempFridge = new TempFridge();

		tempFridge.setTemperature(temperature);
		tempFridge.setDay(LocalDate.now());
		tempFridge.setIdworker(idworker);
		tempFridge.setOk(true);
		tempFridge.setWhen(LocalDateTime.now());

		if(temperature>4.0) tempFridge.setOk(false);

		//TODO: ¿NOTIFICAR SI UNA TEMPERATURA ESTÁ MAL?

		this.tempFridgeRepository.save(tempFridge);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerTempService(String idworker, String dish, String dishtype, Double tempreception, Double tempservice)
	{
		TempService tempService = this.tempServicesRepository.findOneByDayAndDish(LocalDate.now(), dish);
		if(tempService==null) tempService = new TempService();

		tempService.setTemperature_service(tempservice);
		tempService.setTemperature_reception(tempreception);
		tempService.setDish(dish);
		tempService.setDish_type(dishtype);
		tempService.setIdworker(idworker);
		tempService.setDay(LocalDate.now());
		tempService.setOk(true);
		tempService.setWhen(LocalDateTime.now());

		if(tempreception>4.0) tempService.setOk(false);
		if(dishtype.equals("COLD") && tempservice>8.0) tempService.setOk(false);
		if(dishtype.equals("HOT") && tempservice<65.0) tempService.setOk(false);

		//TODO: ¿NOTIFICAR SI UNA TEMPERATURA ESTÁ MAL?

		this.tempServicesRepository.save(tempService);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerMealSample(String idworker, String dish, Boolean organoletico, Boolean cuerposextra, String comments)
	{
		MealSample mealSample = this.mealSamplesRepository.findOneByDayAndDish(LocalDate.now(), dish);
		if(mealSample==null) mealSample = new MealSample();

		mealSample.setOrgenolepticoOk(organoletico);
		mealSample.setCuerposExtraOk(cuerposextra);
		mealSample.setComments(comments);
		mealSample.setDish(dish);
		mealSample.setDay(LocalDate.now());
		mealSample.setIdworker(idworker);
		mealSample.setWhen(LocalDateTime.now());

		//TODO: ¿NOTIFICAR SI UNA MUESTRA ESTÁ MAL?

		this.mealSamplesRepository.save(mealSample);
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerLegionella(String idworker, Double value, String point, String signature)
	{
		LegionellaLog legionellaLog = this.legionellaLogRepository.findOne(LocalDate.now());
		if(legionellaLog==null) legionellaLog = new LegionellaLog();

		legionellaLog.setDay(LocalDate.now());
		legionellaLog.setPoint(point);
		legionellaLog.setIdworker(idworker);
		legionellaLog.setSignature(signature);
		legionellaLog.setValue(value);
		legionellaLog.setWhen(LocalDateTime.now());
		legionellaLog.setOk(true);

		if(value<0.2 || value>1.0)  legionellaLog.setOk(false);

		//TODO: ¿NOTIFICAR SI UNA MUESTRA ESTÁ MAL?

		this.legionellaLogRepository.save(legionellaLog);

	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerWC(String idworker, String point, String signature)
	{
		WCLog wcLog = new WCLog();
		wcLog.setIdworker(idworker);
		wcLog.setPoint(point);
		wcLog.setWhen(LocalDateTime.now());
		wcLog.setSignature(signature);
		this.wcLogRepository.save(wcLog);
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

		WorkerAbsence workerAbsence = new WorkerAbsence();
		workerAbsence.setIdpatient(idpatient);
		workerAbsence.setIdworker(idworker);
		workerAbsence.setIdroutestop(idroutestop);
		workerAbsence.setWhen(LocalDateTime.now());
		workerAbsence.setComment(comment);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new WorkerAbsenceDTO(this.workersAbsencesRepository.save(workerAbsence), patient);	}

	@Override
	public void deleteAbsence(String idpatient, String idabsence) {

	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveCalendarEvent(String idworker, String idcalendarevent, LocalDateTime start, LocalDateTime end, Boolean allDay, String title, Boolean dayoff, String description, List<String> roles, List<String> idsusers, LocalDateTime publishdate)
	{
		CalendarEvent calendarEvent = null;
		
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
							new Criteria().orOperator(Criteria.where("roles").is(null), Criteria.where("roles").size(0)),
							new Criteria().orOperator(Criteria.where("idsusers").is(null), Criteria.where("idsusers").size(0))
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
	public void saveMenu(String id, String type, String description, LocalDate from, LocalDate to, MultipartFile file) throws Exception
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
	public Page<FeedingDTO> getFeedings(User worker, String groupcode, String idpatient, LocalDate day, Integer page, Integer size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria();
		if(idpatient!=null)
			criteria.and("idpatient").is(idpatient);
		else if(groupcode != null)
		{
			Query query2 = new Query();
			query2.addCriteria(new Criteria().where("roles").is(Arrays.asList("PATIENT")).and("groupcode").is(groupcode).and("status").is("A"));
			List<String> idspatients =  this.mongoTemplate.find(query2, User.class).stream().map(x -> x.get_id()).toList();
			criteria.and("idpatient").in(idspatients);
		}
		if(day!=null) criteria.and("day").is(day);

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

}
