package afamijas.controller;


import afamijas.model.dto.CalendarEventDTO;
import afamijas.service.ErrorsService;

import afamijas.service.RelativesService;
import afamijas.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping("/private/relatives")
public class RelativesController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final RelativesService relativesService;

	@Autowired
	public RelativesController(UsersService usersService, RelativesService relativesService, ErrorsService errorsService)
	{
		super(usersService);
		this.errorsService = errorsService;
		this.relativesService = relativesService;
	}

	@RequestMapping(method=RequestMethod.GET, value="getRoute", produces="application/json")
	public ResponseEntity<?> getRoute(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.getRoute(idpatient), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="changeRouteStop", produces="application/json")
	public ResponseEntity<?> changeRouteStop(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "idroutestop", required = true) String idroutestop,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
			HttpServletRequest request
	)
	{
		try
		{
			if(from!=null && to==null) to = from; //solo para un día
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.changeRouteStop(idpatient, idroutestop, from, to), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

  	// si marca día entero solo se envía day (día puntual entero) --> si quisiera un intervalo de fechas se haría bucle con esto
	// si entra un intervalo de fecha/horo se envía from-to y esto indicaría una falta puntual un día de tal a tal hora
	@RequestMapping(method=RequestMethod.POST, value="addAbsenceByRelative", produces="application/json")
	public ResponseEntity<?> addAbsenceByRelative(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to,
			@RequestParam(value = "notransport", required = true)  Boolean notransport,
			@RequestParam(value = "comment", required = false) String comment,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.addAbsenceByRelative(idpatient, this.getId(), day, from , to, notransport, comment), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="deleteAbsence", produces="application/json")
	public ResponseEntity<?> deleteAbsence(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "idabsence", required = true) String idabsence,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			this.relativesService.deleteAbsence(idpatient, idabsence);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="getMenu", produces="application/json")
	public ResponseEntity<?> getMenu(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.getMenu(idpatient), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="getCalendarEvents", produces="application/json")
	public ResponseEntity<?> getCalendarEvents(
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.relativesService.getCalendarEvents(this.getId()), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="getPendingPermissions", produces="application/json")
	public ResponseEntity<?> getPendingPermissions(
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.relativesService.getPendingPermissions(this.getId()), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="signPermission", produces="application/json")
	public ResponseEntity<?> signPermission(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "idpermission", required = true) String idpermission,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			this.relativesService.signPermission(idpatient, idpatient, file);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




}

