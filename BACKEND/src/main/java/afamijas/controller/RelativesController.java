package afamijas.controller;


import afamijas.model.CalendarDay;
import afamijas.model.dto.AbsenceDTO;
import afamijas.model.dto.MenuDTO;
import afamijas.model.dto.PermissionDTO;
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
import java.util.List;

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
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.changeRouteStop(idpatient, idroutestop, from, to), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="addAbsence", produces="application/json")
	public ResponseEntity<?> addAbsence(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "day", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day,
			@RequestParam(value = "comment", required = false) String comment,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.addAbsence(idpatient, day, comment), HttpStatus.OK);
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
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.getMenu(idpatient), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="getCalendar", produces="application/json")
	public ResponseEntity<?> getCalendar(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "day", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day,
			@RequestParam(value = "numdays", required = true) Integer numdays,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!this.isPatientForRelative(idpatient)) return new ResponseEntity<>(HttpStatus.CONFLICT);
			return new ResponseEntity<>(this.relativesService.getCalendar(idpatient, day, numdays), HttpStatus.OK);
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
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
			if(!this.isRelative()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
