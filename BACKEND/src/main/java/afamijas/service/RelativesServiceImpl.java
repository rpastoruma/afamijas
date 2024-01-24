package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RelativesServiceImpl implements RelativesService
{
	@Value("${media.path}")
	String mediapath;

    final UsersRepository usersRepository;

	final RoutesRepository routesRepository;

	final RouteStopsRepository routeStopsRepository;

	final AbsencesRepository absencesRepository;

	final MenusRepository menusRepository;

	final CalendarDaysRepository calendarDaysRepository;

	final PermissionsRepository permissionsRepository;

	final MediaService mediaService;

	@Autowired
	public RelativesServiceImpl(UsersRepository usersRepository, RoutesRepository routesRepository, RouteStopsRepository routeStopsRepository, AbsencesRepository absencesRepository, MenusRepository menusRepository, CalendarDaysRepository calendarDaysRepository, PermissionsRepository permissionsRepository, MediaService mediaService)
	{
		this.usersRepository = usersRepository;
		this.routesRepository = routesRepository;
		this.routeStopsRepository = routeStopsRepository;
		this.absencesRepository = absencesRepository;
		this.menusRepository = menusRepository;
		this.calendarDaysRepository = calendarDaysRepository;
		this.permissionsRepository = permissionsRepository;
		this.mediaService = mediaService;
	}

	@Override
	public RouteDTO getRoute(String idpatient)
	{
		User patient = this.usersRepository.findOne(idpatient, "PATIENT", "A");
		if(patient==null) return null;

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
		{
			routeDTO.setIdroutestop_selected_today(routeStopForToday.get_id());
			routeDTO.setRoutestop_name_selected_today(routeStopForToday.getName());
		}
		else
		{
			routeDTO.setIdroutestop_selected_today(null);
			routeDTO.setRoutestop_name_selected_today("**SIN PARADA ASIGNADA**");
		}

		RouteStop routeStopForTomorrow = this.routeStopsRepository.findOne(patient.getIdRouteStopForDay(LocalDateTime.now().plusDays(1)), "A");
		if(routeStopForToday!=null)
		{
			routeDTO.setIdroutestop_selected_today(routeStopForTomorrow.get_id());
			routeDTO.setRoutestop_name_selected_today(routeStopForTomorrow.getName());
		}
		else
		{
			routeDTO.setIdroutestop_selected_today(null);
			routeDTO.setRoutestop_name_selected_today("**SIN PARADA ASIGNADA**");
		}

		return routeDTO;
	}



	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public RouteDTO changeRouteStop(String idpatient, String idroutestop, LocalDate from, LocalDate to)
	{
		User patient = this.usersRepository.findOne(idpatient, "PATIENT", "A");
		if(patient==null) return null;

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

			//TODO: EMAIL A QUIEN CORRESPONDA ANUNCIANDO CAMBIO DE PARADA PARA ESTE PACIENTE
		}

		return this.getRoute(idpatient);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public AbsenceDTO addAbsenceByRelative(String idpatient, String idrelative, LocalDate day, LocalDateTime from, LocalDateTime to, Boolean notransport, String comment)
	{
		User patient = this.usersRepository.findOne(idpatient, "PATIENT", "A");
		if(patient==null) return null;
		Boolean allday = false; if(from==null && to==null) allday = true;

		Absence absence = new Absence();
		absence.setIdpatient(idpatient);
		absence.setIdrelative(idrelative);
		absence.setIdworker(null);
		absence.setDay(day);
		absence.setAllday(allday);
		absence.setFrom(from);
		absence.setTo(to);
		absence.setComment(comment);

		RouteStop routeStop = null;
		if(notransport) routeStop = this.routeStopsRepository.findOne(patient.getIdRouteStopForDay(day.atTime(12, 0, 0)), "A");
		if(routeStop!=null) absence.setIdroutestop(routeStop.get_id());

		//TODO: EMAIL A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new AbsenceDTO(this.absencesRepository.save(absence), patient, notransport?routeStop:null);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void deleteAbsence(String idpatient, String idabsence)
	{
		User patient = this.usersRepository.findOne(idpatient, "PATIENT", "A");
		if(patient==null) return;

		Absence absence = this.absencesRepository.findOne(idabsence);
		if(absence==null) return;

		if(!absence.getIdpatient().equals(idpatient)) return;

		this.absencesRepository.deleteById(idabsence);

		//TODO: EMAIL A QUIEN CORRESPONDA ANUNCIANDO ELIMINACIÓN DE AUSENCIA PARA ESTE PACIENTE
	}

	@Override
	public MenuDTO getMenu(String idpatient)
	{
		User patient = this.usersRepository.findOne(idpatient, "PATIENT", "A");
		if(patient==null) return null;

		return new MenuDTO(this.menusRepository.findMenuByPatient(idpatient), patient);
	}


	@Override
	public List<PermissionDTO> getPendingPermissions(String idrelative)
	{
		User relative = this.usersRepository.findOne(idrelative, "RELATIVE", "A");
		if(relative==null) return null;

		return this.permissionsRepository.findPendingPermissionsByRelative(idrelative).stream().map(x -> new PermissionDTO(x, relative, this.usersRepository.findOne(x.getIdpatient(), "PATIENT", "A") )).toList();
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public PermissionDTO signPermission(String idpermission, String idpatient, MultipartFile file) throws Exception
	{
		Permission permission = this.permissionsRepository.findOne(idpermission, "A");

		if(!permission.getIdpatient().equals(idpatient)) return null;

		User relative = this.usersRepository.findOne(permission.getIdrelative(), "RELATIVE", "A");
		if(relative==null) return null;

		User patient = this.usersRepository.findOne(permission.getIdpatient(),"PATIENT",  "A");
		if(patient==null) return null;

		Media media = this.mediaService.create(idpermission, "permission", "signed", file);
		permission.setPermission_signed_url(media.getUrl());
		permission.setStatus("A");

		return new PermissionDTO(this.permissionsRepository.save(permission), relative, patient);

		//TODO: EMAIL A QUIEN CORRESPONDA INDICANDO QUE SE HA FIRMADO PERMISO
	}

	@Override
	public List<CalendarDay> getCalendar(String idrelative, LocalDate day, Integer numdays)
	{
		List<CalendarDay> alldays = this.calendarDaysRepository.findCalendarDaysByRole("RELATIVE");
		alldays.addAll(this.calendarDaysRepository.findCalendarDaysByUser(idrelative));
		alldays.addAll(this.calendarDaysRepository.findGeneric());
		Collections.sort(alldays, Comparator.comparing(CalendarDay::getDay));
		return alldays;
	}


}
