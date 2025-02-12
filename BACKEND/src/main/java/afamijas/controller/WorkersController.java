package afamijas.controller;


import afamijas.model.CalendarEvent;
import afamijas.model.dto.AtencionDTO;
import afamijas.model.dto.CalendarEventDTO;
import afamijas.service.ErrorsService;
import afamijas.service.MediaService;
import afamijas.service.UsersService;
import afamijas.service.WorkersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
			@RequestParam(value = "documentid", required = false) String documentid,
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
			return new ResponseEntity<>(this.workersService.getActivePatients(name_surnames, documentid, groupcode, page, size, order, orderasc), HttpStatus.OK);
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
			@RequestParam(value = "groupcode", required = false) String groupcode,
			HttpServletRequest request
	)
	{
		try
		{
			//PARA TODOS LOS TRABAJADORES
			if(this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(groupcode!=null && groupcode.trim().equals("")) groupcode = null;

			return new ResponseEntity<>(this.workersService.getAllPatients(groupcode), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="getPatientById", produces="application/json")
	public ResponseEntity<?> getPatientById(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request
	)
	{
		try
		{
			//PARA TODOS LOS TRABAJADORES
			if(this.isRELATIVE()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


			return new ResponseEntity<>(this.workersService.getPatientById(id), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.GET, value="getAllMembers", produces="application/json")
	public ResponseEntity<?> getAllMembers(
			HttpServletRequest request
	)
	{
		try
		{
			//PARA ADMIN Y DIRECTOR
			if(!this.isMANAGER() && !this.isADMIN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.workersService.getAllMembers(), HttpStatus.OK);
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

			this.workersService.saveMenu(this.getId(), idmenu, type, description, from, to, file);
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





	@RequestMapping(method=RequestMethod.GET, value="getFeedings", produces="application/json")
	public ResponseEntity<?> getFeedings(
			@RequestParam(value = "groupcode", required = false) String groupcode,
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER() && !isNURSING_ASSISTANT() )
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getFeedings(this.getUser(), groupcode, idpatient, dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="registerFeeding", produces="application/json")
	public ResponseEntity<?> registerFeeding(
			@RequestParam(value = "id", required = false) String id,
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

			this.workersService.registerFeeding(id, idpatient, this.getId(), dish, result, daymeal, indications, incidences);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="deleteFeeding", produces="application/json")
	public ResponseEntity<?> deleteFeeding(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isNURSING_ASSISTANT()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteFeeding(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}







	@RequestMapping(method=RequestMethod.GET, value="getTempFridges", produces="application/json")
	public ResponseEntity<?> getTempFridges(
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
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

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getTempFridges(this.getUser(), dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="registerTempFridge", produces="application/json")
	public ResponseEntity<?> registerTempFridge(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "temperature", required = true) Double temperature,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerTempFridge(id, this.getId(), temperature);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="deleteTempFridge", produces="application/json")
	public ResponseEntity<?> deleteTempFridge(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteTempFridge(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.GET, value="getTempServices", produces="application/json")
	public ResponseEntity<?> getTempServices(
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
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

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getTempServices(this.getUser(), dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="registerTempService", produces="application/json")
	public ResponseEntity<?> registerTempService(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "dish", required = true) String dish,
			@RequestParam(value = "dish_type", required = true) String dish_type,
			@RequestParam(value = "temperature_reception", required = true) Double temperature_reception,
			@RequestParam(value = "temperature_service", required = false) Double temperature_service,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerTempService(id, this.getId(), dish, dish_type, temperature_reception, temperature_service);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteTempService", produces="application/json")
	public ResponseEntity<?> deleteTempService(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteTempService(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.GET, value="getMealSamples", produces="application/json")
	public ResponseEntity<?> getMealSamples(
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
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

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getMealSamples(this.getUser(), dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="registerMealSample", produces="application/json")
	public ResponseEntity<?> registerMealSample(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "dish", required = true) String dish,
			@RequestParam(value = "orgenolepticoOk", required = false) Boolean orgenolepticoOk,
			@RequestParam(value = "cuerposExtraOk", required = false) Boolean cuerposExtraOk,
			@RequestParam(value = "comments", required = false) String comments,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerMealSample(id, this.getId(), dish, orgenolepticoOk, cuerposExtraOk, comments);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteMealSample", produces="application/json")
	public ResponseEntity<?> deleteMealSample(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isKITCHEN()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteMealSample(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}










	@RequestMapping(method=RequestMethod.GET, value="getLegionellaLogs", produces="application/json")
	public ResponseEntity<?> getLegionellaLogs(
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER() && !isLEGIONELLA_CONTROL() )
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getLegionellaLogs(this.getUser(), dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="registerLegionellaLog", produces="application/json")
	public ResponseEntity<?> registerLegionellaLog(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "point", required = true) String point,
			@RequestParam(value = "value", required = false) Double value,
			@RequestParam(value = "temperature", required = false) Double temperature,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isLEGIONELLA_CONTROL()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerLegionellaLog(id, this.getId(), point, value, temperature);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteLegionellaLog", produces="application/json")
	public ResponseEntity<?> deleteLegionellaLog(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isLEGIONELLA_CONTROL()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteLegionellaLog(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}









	@RequestMapping(method=RequestMethod.GET, value="getWCLogs", produces="application/json")
	public ResponseEntity<?> getWCLogs(
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER() && !isCLEANING() )
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getWCLogs(this.getUser(), dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="registerWCLog", produces="application/json")
	public ResponseEntity<?> registerWCLog(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "point", required = true) String point,
			@RequestParam(value = "hour", required = true) String hour,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isCLEANING()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerWCLog(id, this.getId(), point, hour);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteWCLog", produces="application/json")
	public ResponseEntity<?> deleteWCLog(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isCLEANING()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteWCLog(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}






	@RequestMapping(method=RequestMethod.GET, value="getHealthLogs", produces="application/json")
	public ResponseEntity<?> getHealthLogs(
			@RequestParam(value = "groupcode", required = false) String groupcode,
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "dayfrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER() && !isNURSING()  && !isNURSING_ASSISTANT() )
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getHealthLogs(this.getUser(), groupcode, idpatient, dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="registerHealthLog", produces="application/json")
	public ResponseEntity<?> registerHealthLog(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "low_pressure", required = false) Double low_pressure,
			@RequestParam(value = "high_pressure", required = false) Double high_pressure,
			@RequestParam(value = "sugar", required = false) Double sugar,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isNURSING_ASSISTANT()  && !isNURSING()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerHealthLog(id, idpatient, this.getId(), low_pressure, high_pressure, sugar);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="deleteHealthLog", produces="application/json")
	public ResponseEntity<?> deleteHealthLog(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isNURSING_ASSISTANT()  && !isNURSING()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteHealthLog(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}










	@RequestMapping(method=RequestMethod.GET, value="getDocs", produces="application/json")
	public ResponseEntity<?> getDocs(
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "dayfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getDocs(this.getUser(), text, dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="saveDoc", produces="application/json")
	public ResponseEntity<?> saveDoc(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "dayfrom", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "roles", required = false)  List<String> roles,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.saveDoc(id, this.getId(), title, description, url, dayfrom, dayto, roles);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="deleteDoc", produces="application/json")
	public ResponseEntity<?> deleteDoc(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteDoc(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}









	@RequestMapping(method=RequestMethod.GET, value="getReceipts", produces="application/json")
	public ResponseEntity<?> getReceipts(
			@RequestParam(value = "idmember", required = false) String idmember,
			@RequestParam(value = "dayfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			if(idmember!=null && idmember.trim().equals("")) idmember = null;
			if(status!=null && status.trim().equals("")) status = null;

			return new ResponseEntity<>(this.workersService.getReceipts(this.getUser(), idmember, dayfrom, dayto, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="saveReceipt", produces="application/json")
	public ResponseEntity<?> saveReceipt(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "idmember", required = false) String idmember,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "total", required = true) Double total,
			@RequestParam(value = "duedate", required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate duedate,
			@RequestParam(value = "status", required = true) String status,
			@RequestParam(value = "paiddate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paiddate,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.saveReceipt(id, idmember, url, total, duedate, status, paiddate);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteReceipt", produces="application/json")
	public ResponseEntity<?> deleteReceipt(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteReceipt(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}








	@RequestMapping(method=RequestMethod.GET, value="getInvoices", produces="application/json")
	public ResponseEntity<?> getInvoices(
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "dayfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			if(idpatient!=null && idpatient.trim().equals("")) idpatient = null;
			if(status!=null && status.trim().equals("")) status = null;

			return new ResponseEntity<>(this.workersService.getInvoices(this.getUser(), idpatient, dayfrom, dayto, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="saveInvoice", produces="application/json")
	public ResponseEntity<?> saveInvoice(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "total", required = true) Double total,
			@RequestParam(value = "duedate", required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate duedate,
			@RequestParam(value = "status", required = true) String status,
			@RequestParam(value = "paiddate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paiddate,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.saveInvoice(id, idpatient, url, total, duedate, status, paiddate);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteInvoice", produces="application/json")
	public ResponseEntity<?> deleteInvoice(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteInvoice(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}














	@RequestMapping(method=RequestMethod.GET, value="getDocsPsico", produces="application/json")
	public ResponseEntity<?> getDocsPsico(
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getDocsPsico(this.getUser(), idpatient, type, text, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="saveDocPsico", produces="application/json")
	public ResponseEntity<?> saveDocPsico(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "description", required = false) String description,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.saveDocPsico(id, this.getId(), idpatient, type, description, url);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="deleteDocPsico", produces="application/json")
	public ResponseEntity<?> deleteDocPsico(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteDocPsico(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}











	@RequestMapping(method=RequestMethod.GET, value="getAtenciones", produces="application/json")
	public ResponseEntity<?> getAtenciones(
			@RequestParam(value = "dayfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayfrom,
			@RequestParam(value = "dayto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayto,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,

			HttpServletRequest request
	)
	{
		try
		{
			if(order==null) order = "created";
			if(orderasc==null) orderasc = "DESC";

			return new ResponseEntity<>(this.workersService.getAtenciones(dayfrom, dayto, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="registerAtencion", produces="application/json")
	public ResponseEntity<?> registerAtencion(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "datedone", required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate datedone,
			@RequestParam(value = "clientfullname", required = true) String clientfullname,
			@RequestParam(value = "sex", required = true) String sex,
			@RequestParam(value = "nationality", required = true) String nationality,
			@RequestParam(value = "relationship", required = true) String relationship,
			@RequestParam(value = "why", required = true) String why,
			@RequestParam(value = "via", required = true) String via,
			@RequestParam(value = "professional", required = false) String professional,
			@RequestParam(value = "observations", required = false) String observations,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()  && !isSOCIAL_WORKER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerAtencion(id, this.getId(), number, datedone, clientfullname, sex, nationality, relationship, why, via, professional, observations);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="deleteAtencion", produces="application/json")
	public ResponseEntity<?> deleteAtencion(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN()  && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.deleteAtencion(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}







}

