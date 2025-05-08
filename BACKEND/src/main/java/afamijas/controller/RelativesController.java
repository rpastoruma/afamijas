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
			if(!this.isRELATIVE() && !this.isWORKER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(this.isRELATIVE() && !this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
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





	@RequestMapping(method=RequestMethod.GET, value="getRelativeAbsences", produces="application/json")
	public ResponseEntity<?> getRelativeAbsences(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			HttpServletRequest request
	)
	{
		try
		{
			LocalDateTime from2 = from!=null?from.atTime(0, 0, 0, 0):null;
			LocalDateTime to2 = to!=null?to.atTime(23, 59, 59, 999999999):null;

			if(!this.isRELATIVE() && !this.isWORKER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(this.isRELATIVE() && !this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.getRelativeAbsences(idpatient, this.getId(), from2 , to2, page, size, "from", "DESC"), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="saveAbsenceByRelative", produces="application/json")
	public ResponseEntity<?> saveAbsenceByRelative(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "allday", required = true) Boolean allday,
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime to,
			@RequestParam(value = "transport", required = true)  String transport,
			@RequestParam(value = "comment", required = false) String comment,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.saveAbsenceByRelative(id, idpatient, this.getId(), from , to, allday, transport, comment), HttpStatus.OK);
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
			return new ResponseEntity<>("", HttpStatus.OK);
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


	@RequestMapping(method=RequestMethod.GET, value="getPermissions", produces="application/json")
	public ResponseEntity<?> getPermissions(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "status", required = true) String status,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.relativesService.getPermissions(idpatient, status, page, size, "created", "DESC"), HttpStatus.OK);
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
			@RequestParam(value = "signedfileurl", required = true) String signedfileurl,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			this.relativesService.signPermission(this.getId(), idpermission, idpatient, signedfileurl);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="getPatients", produces="application/json")
	public ResponseEntity<?> getPatients(
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.relativesService.getPatients(this.getId()), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}

