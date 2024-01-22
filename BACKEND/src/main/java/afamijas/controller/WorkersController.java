package afamijas.controller;


import afamijas.service.ErrorsService;
import afamijas.service.UsersService;
import afamijas.service.WorkersService;
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

@RestController
@Validated
@RequestMapping("/private/workers")
public class WorkersController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final WorkersService workersService;

	@Autowired
	public WorkersController(UsersService usersService, ErrorsService errorsService, WorkersService workersService)
	{
		super(usersService);
		this.errorsService = errorsService;
		this.workersService = workersService;
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
			if(!this.isAdmin() && !this.isKitchen() && !isManager() && !isNursing() && !isNursingAssistant()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

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
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isMonitor()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			this.workersService.registerFeeding(idpatient, this.getId(), dish, result, daymeal);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}

