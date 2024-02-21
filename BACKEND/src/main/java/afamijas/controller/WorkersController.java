package afamijas.controller;


import afamijas.model.CalendarEvent;
import afamijas.model.dto.CalendarEventDTO;
import afamijas.service.ErrorsService;
import afamijas.service.MediaService;
import afamijas.service.UsersService;
import afamijas.service.WorkersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import java.util.Arrays;
import java.util.List;

@RestController
@Validated
@RequestMapping("/private/workers")
public class WorkersController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final WorkersService workersService;

	final MediaService mediaService;

	@Autowired
	public WorkersController(UsersService usersService, ErrorsService errorsService, WorkersService workersService, MediaService mediaService)
	{
		super(usersService);
		this.errorsService = errorsService;
		this.workersService = workersService;
		this.mediaService = mediaService;
	}

	@RequestMapping(method=RequestMethod.GET, value="getActivePatients", produces="application/json")
	public ResponseEntity<?> getActivePatients(
			@RequestParam(value = "name_surnames", required = false) String name_surnames,
			@RequestParam(value = "dni", required = false) String dni,
			@RequestParam(value = "groupcode", required = false) String groupcode,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isKITCHEN() && !isMANAGER() && !isNURSING() && !isNURSING_ASSISTANT()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "name";
			if(orderasc==null) orderasc = "ASC";
			return new ResponseEntity<>(this.workersService.getActivePatients(name_surnames, dni, groupcode, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="registerFeeding", produces="application/json")
	public ResponseEntity<?> registerFeeding(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "dish", required = true) String dish,
			@RequestParam(value = "result", required = true) String result,
			@RequestParam(value = "daymeal", required = true) String daymeal,
			@RequestParam(value = "indications", required = false) String indications,
			@RequestParam(value = "incidences", required = false) String incidences,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isNURSING_ASSISTANT()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerFeeding(idpatient, this.getId(), dish, result, daymeal, indications, incidences);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="registerTempFridge", produces="application/json")
	public ResponseEntity<?> registerTempFridge(
			@RequestParam(value = "temperature", required = true) Double temperature,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerTempFridge(this.getId(), temperature);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="registerTempService", produces="application/json")
	public ResponseEntity<?> registerTempService(
			@RequestParam(value = "dish", required = true) String dish,
			@RequestParam(value = "dishtype", required = true) String dishtype,
			@RequestParam(value = "tempreception", required = false) Double tempreception,
			@RequestParam(value = "tempservice", required = false) Double tempservice,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(tempservice==null && tempreception==null) return new ResponseEntity<>("Se necesita indicar temperatura.", HttpStatus.BAD_REQUEST);

			this.workersService.registerTempService(this.getId(), dish, dishtype,  tempreception, tempservice);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="registerMealSample", produces="application/json")
	public ResponseEntity<?> registerMealSample(
			@RequestParam(value = "dish", required = true) String dish,
			@RequestParam(value = "organoleptico", required = true) Boolean organoleptico,
			@RequestParam(value = "cuerposextra", required = true) Boolean cuerposextra,
			@RequestParam(value = "comments", required = false) String comments,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerMealSample(this.getId(), dish, organoleptico, cuerposextra, comments);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="registerLegionella", produces="application/json")
	public ResponseEntity<?> registerLegionella(
			@RequestParam(value = "value", required = true) Double value,
			@RequestParam(value = "point", required = true) String point,
			@RequestParam(value = "signature", required = true) String signature,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isLEGIONELLA_CONTROL()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerLegionella(this.getId(), value, point, signature);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="registerWC", produces="application/json")
	public ResponseEntity<?> registerWC(
			@RequestParam(value = "point", required = true) String point,
			@RequestParam(value = "signature", required = true) String signature,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isCLEANING()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerWC(this.getId(), point, signature);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="uploadTimetable", produces="application/json")
	public ResponseEntity<?> uploadTimetable(
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.workersService.uploadTimetable(file);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="uploadActivities", produces="application/json")
	public ResponseEntity<?> uploadActivities(
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.workersService.uploadActivities(file);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="saveCalendarEvent", produces="application/json")
	public ResponseEntity<?> saveCalendarEvent(
			@RequestParam(value = "idcalendarevent", required = false) String idcalendarevent,

			@RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,

			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "description", required = false) String description,

			@RequestParam(value = "dayoff", required = false) Boolean dayoff,

			@RequestParam(value = "roles", required = false) List<String> roles,
			@RequestParam(value = "idsusers", required = false) List<String> idsusers,

			@RequestParam(value = "publishdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime publishdate,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(title.trim().equals("")) return new ResponseEntity<>("Se debe aportar un título para el evento.", HttpStatus.BAD_REQUEST);

			if(end==null) end = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 23, 59, 59, 999999999);

			if(end.isBefore(start)) return new ResponseEntity<>("La fecha de finalización del evento no puede ser anterior a la de su comienzo.", HttpStatus.BAD_REQUEST);

			Boolean allDay = false;
			if(start.getHour()==0 && start.getMinute()==0 && end.getHour()==23 && end.getMinute()==59) allDay = true;

			if(dayoff==null) dayoff = false;
			if(dayoff)
			{
				allDay = true;
				roles = null;
				idsusers = null;
				publishdate = null;
				end = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, 999999999);
				start = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0, 0);
			}

			if(idsusers!=null && idsusers.size()>0) roles = null; //SI ES PARA USUARIOS ESPECÍFICOS NO SE PONE ROL

			this.workersService.saveCalendarEvent(this.getId(), idcalendarevent, start, end, allDay, title, dayoff, description, roles, idsusers, publishdate);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="deleteCalendarEvent", produces="application/json")
	public ResponseEntity<?> deleteCalendarEvent(
			@RequestParam(value = "idcalendarevent", required = true) String idcalendarevent,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteCalendarEvent(idcalendarevent);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.GET, value="getAllUsers", produces="application/json")
	public ResponseEntity<?> getAllUsers(
			@RequestParam(value = "roles", required = false) List<String> roles,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.workersService.getAllUsers(roles), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.GET, value="getAllPatients", produces="application/json")
	public ResponseEntity<?> getAllPatients(
			HttpServletRequest request
	)
	{
		try
		{
			//PARA TODOS LOS TRABAJADORES
			if(this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.workersService.getAllPatients(), HttpStatus.OK);
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
			if(this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);  // SOLO WORKERS

			return new ResponseEntity<>(this.workersService.getCalendarEvents(this.getId(), this.getRoles(), this.isADMIN() || this.isMANAGER() ), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="saveMenu", produces="application/json")
	public ResponseEntity<?> saveMenu(
			@RequestParam(value = "idmenu", required = false) String idmenu,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "from", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.saveMenu(idmenu, type, description, from, to, file);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="getMedications", produces="application/json")
	public ResponseEntity<?> getMedications(
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER() && !isNURSING() && !isNURSING_ASSISTANT() &&
					!isPSYCHOLOGIST() && !isSOCIAL_WORKER() && !isPHYSIOTHERAPIST() && !isOCCUPATIONAL_THERAPIST() &&
					!isOPERATOR_EXTRA_1() )
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "name";
			if(orderasc==null) orderasc = "ASC";

			return new ResponseEntity<>(this.workersService.getMedications(idpatient, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="modifyMedication", produces="application/json")
	public ResponseEntity<?> modifyMedication(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "medication_description_morning", required = true) String medication_description_morning,
			@RequestParam(value = "medication_description_evening", required = true) String medication_description_evening,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.modifyMedication(idpatient, medication_description_morning, medication_description_evening);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="getFoods", produces="application/json")
	public ResponseEntity<?> getFoods(
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER() && !isKITCHEN() )
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "name";
			if(orderasc==null) orderasc = "ASC";

			return new ResponseEntity<>(this.workersService.getFoods(idpatient, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="modifyFood", produces="application/json")
	public ResponseEntity<?> modifyFood(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "menu_type", required = true) String menu_type,
			@RequestParam(value = "breakfast_description", required = true) String breakfast_description,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.modifyFood(idpatient, menu_type, breakfast_description);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

