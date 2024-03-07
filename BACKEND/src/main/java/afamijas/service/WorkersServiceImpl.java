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

		this.wcLogRepository.save(wcLog);	}

	@Override
	public void deleteWCLog(String id)
	{
		WCLog wcLog = this.wcLogRepository.findOne(id);
		if(wcLog!=null) this.wcLogRepository.delete(wcLog);
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
