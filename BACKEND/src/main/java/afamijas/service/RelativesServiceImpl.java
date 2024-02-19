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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RelativesServiceImpl implements RelativesService
{
	@Value("${debug.queries}")
	Boolean debug_queries;

	@Value("${media.path}")
	String mediapath;

    final UsersRepository usersRepository;

	final RoutesRepository routesRepository;

	final RouteStopsRepository routeStopsRepository;

	final RelativesAbsencesRepository relativesAbsencesRepository;

	final MenusRepository menusRepository;

	final CalendarEventsRepository calendarEventsRepository;

	final PermissionsRepository permissionsRepository;

	final MongoTemplate mongoTemplate;

	final MediaService mediaService;

	@Autowired
	public RelativesServiceImpl(UsersRepository usersRepository, RoutesRepository routesRepository, RouteStopsRepository routeStopsRepository, RelativesAbsencesRepository relativesAbsencesRepository, MenusRepository menusRepository, CalendarEventsRepository calendarEventsRepository, PermissionsRepository permissionsRepository, MongoTemplate mongoTemplate, MediaService mediaService)
	{
		this.usersRepository = usersRepository;
		this.routesRepository = routesRepository;
		this.routeStopsRepository = routeStopsRepository;
		this.relativesAbsencesRepository = relativesAbsencesRepository;
		this.menusRepository = menusRepository;
		this.calendarEventsRepository = calendarEventsRepository;
		this.permissionsRepository = permissionsRepository;
		this.mongoTemplate = mongoTemplate;
		this.mediaService = mediaService;
	}

	@Override
	public RouteDTO getRoute(String idpatient)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		if((patient.getIdroute()==null)) return null;

		Route route = this.routesRepository.findOne(patient.getIdroute(), "A");
		if(route==null) return null;

		RouteDTO routeDTO = new RouteDTO(route, patient);

		//obtenemos y ordenamos las paradas de la ruta asignada al paciente
		List<RouteStop> routeStopList = this.routeStopsRepository.findRouteStopsByRoute(patient.getIdroute());
		Collections.sort(routeStopList, Comparator.comparingInt(RouteStop::getOrder));
		routeDTO.setRoutestops(routeStopList.stream().map(x -> new RouteStopDTO(x)).toList());

		RouteStop routeStopForToday = this.routeStopsRepository.findOne(patient.getIdRouteStopForDay(LocalDateTime.now()), "A");
		if(routeStopForToday!=null)
			routeDTO.setIdroutestop_selected_today(routeStopForToday.get_id());
		else
			routeDTO.setIdroutestop_selected_today(null);

		RouteStop routeStopForTomorrow = this.routeStopsRepository.findOne(patient.getIdRouteStopForDay(LocalDateTime.now().plusDays(1)), "A");
		if(routeStopForTomorrow!=null)
			routeDTO.setIdroutestop_selected_tomorrow(routeStopForTomorrow.get_id());
		else
			routeDTO.setIdroutestop_selected_tomorrow(null);

		return routeDTO;
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public RouteDTO changeRouteStop(String idpatient, String idroutestop, LocalDate from, LocalDate to)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		RouteStop routeStop = this.routeStopsRepository.findOne(idroutestop, "A");
		if(routeStop==null) return null;

		if(!patient.getIdroute().equals(routeStop.getIdroute())) return null; // se está intentando asignar una parada que no corresponde a la ruta del paciente

		if(from==null || to==null)
		{
			patient.setIdroutestop(idroutestop);
			this.usersRepository.save(patient);
		}
		else
		{
			patient.setIdroutestop_especial(idroutestop);
			patient.setRoutestop_especial_from(from.atStartOfDay());
			patient.setRoutestop_especial_to(to.atTime(LocalTime.MAX));
			this.usersRepository.save(patient);

			//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO CAMBIO DE PARADA PARA ESTE PACIENTE
		}

		return this.getRoute(idpatient);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public RelativeAbsenceDTO saveAbsenceByRelative(String id, String idpatient, String idrelative, LocalDateTime from, LocalDateTime to, Boolean allday, String transport, String comment)
	{
		if(from==null && to==null) return null;

		if(from==null) from = LocalDateTime.now();
		if(to==null) to = LocalDateTime.of(from.getYear(), from.getMonth(), from.getDayOfMonth(), 23, 59, 59, 9999999);

		if(allday)
		{
			from = LocalDateTime.of(from.getYear(), from.getMonth(), from.getDayOfMonth(), 0, 0, 0, 0);
			to = LocalDateTime.of(to.getYear(), to.getMonth(), to.getDayOfMonth(), 23, 59, 59, 9999999);
			transport = "NO";
		}

		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		RelativeAbsence relativeAbsence = id!=null?this.relativesAbsencesRepository.findOne(id):new RelativeAbsence();
		if(relativeAbsence==null) return null;

		relativeAbsence.setIdpatient(idpatient);
		relativeAbsence.setIdrelative(idrelative);
		relativeAbsence.setAllday(allday);
		relativeAbsence.setFrom(from);
		relativeAbsence.setTo(to);
		relativeAbsence.setTransport(transport);
		relativeAbsence.setComment(comment);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new RelativeAbsenceDTO(this.relativesAbsencesRepository.save(relativeAbsence), patient);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteAbsence(String idpatient, String idabsence)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return;

		RelativeAbsence relativeAbsence = this.relativesAbsencesRepository.findOne(idabsence);
		if(relativeAbsence ==null) return;

		if(!relativeAbsence.getIdpatient().equals(idpatient)) return;

		this.relativesAbsencesRepository.deleteById(idabsence);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA ANUNCIANDO ELIMINACIÓN DE AUSENCIA PARA ESTE PACIENTE
	}

	@Override
	public List<MenuDTO> getMenu(String idpatient)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		Query query = new Query();

		Criteria criteria = new Criteria().where("type").is(patient.getMenu_type())
				.and("from").lte(LocalDateTime.now())
				.and("to").gte(LocalDateTime.now())
				.and("status").is("A");

		query.addCriteria(criteria);

		try { if(debug_queries) System.out.println("getMenu: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		return this.mongoTemplate.find(query, Menu.class).stream().map(x -> new MenuDTO(x)).toList();
	}





	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public PermissionDTO signPermission(String idrelative, String idpermission, String idpatient, String url_signed_file) throws Exception
	{
		Permission permission = this.permissionsRepository.findOne(idpermission, "P");

		if(!permission.getIdpatient().equals(idpatient)) return null;

		User relative = this.usersRepository.findOne(idrelative, "A");
		if(relative==null || !relative.getRoles().contains("RELATIVE")) return null;

		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null || !patient.getRoles().contains("PATIENT")) return null;

		permission.setSigned(LocalDateTime.now());
		permission.setIdrelative(idrelative);
		permission.setPermission_signed_url(url_signed_file);
		permission.setStatus("A");


		return new PermissionDTO(this.permissionsRepository.save(permission), relative, patient);

		//TODO: EMAIL/NOTIFICACIÓN A QUIEN CORRESPONDA INDICANDO QUE SE HA FIRMADO PERMISO
	}

	@Override
	public List<CalendarEventDTO> getCalendarEvents(String idrelative)
	{
		Query query = new Query();

		Criteria criteria = new Criteria();

		criteria.orOperator(Criteria.where("roles").in(Arrays.asList("RELATIVE")),
							Criteria.where("idsusers").in(Arrays.asList(idrelative)),
							new Criteria().andOperator(
									new Criteria().orOperator(Criteria.where("roles").is(null), Criteria.where("roles").size(0)),
									new Criteria().orOperator(Criteria.where("idsusers").is(null), Criteria.where("idsusers").size(0))
							)

				).and("publishdate").lte(LocalDateTime.now());


		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getCalendarEvents: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		List<CalendarEvent> calendarEventList = this.mongoTemplate.find(query, CalendarEvent.class);

		return calendarEventList.stream().map(x -> new CalendarEventDTO(x)).toList();
	}


	@Override
	public List<PatientDTO> getPatients(String idrelative)
	{
		Query query = new Query().with(Sort.by(Sort.Direction.ASC, "name"));;
		Criteria criteria = new Criteria().where("idrelative").is(idrelative).and("roles").in(Arrays.asList("PATIENT")).and("status").is("A");
		query.addCriteria(criteria);
		try { if(debug_queries) System.out.println("getPatients:" + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		return this.mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, null)).toList();
	}


	@Override
	public Page<RelativeAbsenceDTO> getRelativeAbsences(String idpatient, String idrelative, LocalDateTime from, LocalDateTime to, int page, int size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria1 = new Criteria().where("idpatient").is(idpatient).and("idrelative").is(idrelative);
		if(from!=null) query.addCriteria(Criteria.where("from").gte(from));
		if(to!=null) query.addCriteria(Criteria.where("to").lte(to));

		Criteria criteria2 = new Criteria().where("to").gte(LocalDateTime.now());

		Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
		query.addCriteria(criteria);

		long total = this.mongoTemplate.count(query, RelativeAbsence.class);
		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));
		try { if(debug_queries) System.out.println("getRelativeAbsences: " + query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }
		List<RelativeAbsenceDTO> list = this.mongoTemplate.find(query, RelativeAbsence.class).stream().map(x -> new RelativeAbsenceDTO(x, null)).toList();

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public Page<PermissionDTO> getPermissions(String idpatient, String status, int page, int size, String orderby, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);
		Query query = new Query();

		Criteria criteria = new Criteria().where("idpatient").is(idpatient).and("status").is(status);
		query.addCriteria(criteria);
		long total = this.mongoTemplate.count(query, Permission.class);
		query = query.with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, orderby));

		try { if(debug_queries) System.out.println("getPermissions: " +query.getQueryObject().toJson()); } catch (Exception e) { System.out.println("{X}"); }

		List<PermissionDTO> list = this.mongoTemplate.find(query, Permission.class).stream().map(x -> new PermissionDTO(x, null, null)).toList();

		return new PageImpl<>(list, pageable, total);
	}

}
