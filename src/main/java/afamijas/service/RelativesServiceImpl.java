package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

	final PermissionsRepository permissionsRepository;

	final MediaService mediaService;

	@Autowired
	public RelativesServiceImpl(UsersRepository usersRepository, RoutesRepository routesRepository, RouteStopsRepository routeStopsRepository, AbsencesRepository absencesRepository, MenusRepository menusRepository, PermissionsRepository permissionsRepository, MediaService mediaService)
	{
		this.usersRepository = usersRepository;
		this.routesRepository = routesRepository;
		this.routeStopsRepository = routeStopsRepository;
		this.absencesRepository = absencesRepository;
		this.menusRepository = menusRepository;
		this.permissionsRepository = permissionsRepository;
		this.mediaService = mediaService;
	}

	@Override
	public RouteDTO getRoute(String idpatient)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
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
	public RouteDTO changeRouteStop(String idpatient, String idroutestop, LocalDate from, LocalDate to)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null) return null;

		if(from==null && to==null)
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
	public AbsenceDTO addAbsence(String idpatient, LocalDate day, String comment)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null) return null;

		Absence absence = new Absence();
		absence.setIdpatient(idpatient);
		absence.setDay(day);
		absence.setComment(comment);

		//TODO: EMAIL A QUIEN CORRESPONDA ANUNCIANDO AUSENCIA PARA ESTE PACIENTE

		return new AbsenceDTO(this.absencesRepository.save(absence), patient, this.routeStopsRepository.findOne(patient.getIdRouteStopForDay(day.atTime(12, 0, 0)), "A"));
	}


	@Override
	public void deleteAbsence(String idabsence)
	{
		//TODO: EMAIL A QUIEN CORRESPONDA ANUNCIANDO ELIMINACIÓN DE AUSENCIA PARA ESTE PACIENTE

		this.absencesRepository.deleteById(idabsence);
	}

	@Override
	public MenuDTO getMenu(String idpatient)
	{
		User patient = this.usersRepository.findOne(idpatient, "A");
		if(patient==null) return null;

		return new MenuDTO(this.menusRepository.findMenuByPatient(idpatient), patient);
	}


	@Override
	public List<PermissionDTO> getPendingPermissions(String idrelative)
	{
		User relative = this.usersRepository.findOne(idrelative, "A");
		if(relative==null) return null;

		return this.permissionsRepository.findPendingPermissionsByRelative(idrelative).stream().map(x -> new PermissionDTO(x, relative, this.usersRepository.findOne(x.getIdpatient(), "A") )).toList();
	}


	@Override
	public PermissionDTO signPermission(String idpermission, MultipartFile file) throws Exception
	{
		Permission permission = this.permissionsRepository.findOne(idpermission, "A");

		User relative = this.usersRepository.findOne(permission.getIdrelative(), "A");
		if(relative==null) return null;

		User patient = this.usersRepository.findOne(permission.getIdpatient(), "A");
		if(patient==null) return null;

		Media media = this.mediaService.create(idpermission, "permission", "signed", file);
		permission.setPermission_signed_url(media.getUrl());

		return new PermissionDTO(this.permissionsRepository.save(permission), relative, patient);
	}


}
