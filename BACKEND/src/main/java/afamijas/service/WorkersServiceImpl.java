package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.*;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkersServiceImpl implements WorkersService
{
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

	final AbsencesRepository absencesRepository;
	
	final CalendarEventsRepository calendarEventsRepository;

	final MediaService mediaService;


	@Autowired
	public WorkersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, FeedingRepository feedingRepository, TempFridgeRepository tempFridgeRepository, TempServicesRepository tempServicesRepository, MealSamplesRepository mealSamplesRepository, LegionellaLogRepository legionellaLogRepository, WCLogRepository wcLogRepository, RouteStopsRepository routeStopsRepository, AbsencesRepository absencesRepository, CalendarEventsRepository calendarEventsRepository, MediaService mediaService)
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
		this.absencesRepository = absencesRepository;
		this.calendarEventsRepository = calendarEventsRepository;
		this.mediaService = mediaService;
	}

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
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerFeeding(String idpatient, String idworker, String dish, String result, String daymeal)
	{
		Feeding feeding = this.feedingRepository.findFeedingByPatientDayDaymealAndDish(idpatient, LocalDate.now(), daymeal, dish);
		if(feeding==null) feeding = new Feeding();

		feeding.setIdpatient(idpatient);
		feeding.setIdworker(idworker);
		feeding.setDish(dish);
		feeding.setResult(result);
		feeding.setDaymeal(daymeal);
		feeding.setDay(LocalDate.now());
		feeding.setWhen(LocalDateTime.now());

		this.feedingRepository.save(feeding);
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
	public AbsenceDTO addAbsenceByWorker(String idpatient, String idworker, LocalDate day, String comment)
	{
		User patient = this.usersRepository.findOne(idpatient, "PATIENT", "A");
		if(patient==null) return null;

		Absence absence = new Absence();
		absence.setIdpatient(idpatient);
		absence.setIdrelative(null);
		absence.setIdworker(idworker);
		absence.setDay(day);
		absence.setAllday(null);
		absence.setFrom(null);
		absence.setTo(null);
		absence.setComment(comment);

		RouteStop routeStop = this.routeStopsRepository.findOne(patient.getIdRouteStopForDay(day.atTime(12, 0, 0)), "A");
		if(routeStop!=null) absence.setIdroutestop(routeStop.get_id());

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new AbsenceDTO(this.absencesRepository.save(absence), patient, routeStop);	}

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
		try { System.out.println(query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
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

		System.out.println("getAllUsers: " + query.getQueryObject().toJson());
		List<User> userList = this.mongoTemplate.find(query, User.class);





		return userList.stream().map(x -> new UserDTO(x)).toList();
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteCalendarEvent(String idcalendarevent)
	{
		this.calendarEventsRepository.deleteById(idcalendarevent);
	}


}
