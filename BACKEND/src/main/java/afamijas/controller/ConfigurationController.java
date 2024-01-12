package afamijas.controller;

import afamijas.service.ConfigurationService;
import afamijas.service.ErrorsService;
import afamijas.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/private/configuration")
public class ConfigurationController extends AbstractBaseController
{
	final ConfigurationService configurationService;
	final ErrorsService errorsService;

	@Autowired
	public ConfigurationController(UsersService usersService, ConfigurationService configurationService, ErrorsService errorsService)
	{
		super(usersService);
		this.configurationService = configurationService;
		this.errorsService = errorsService;
	}

	/** FOR CROSS DOMAIN **/
	@CrossOrigin
	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> handle() 
	{
        return new ResponseEntity<Object>(HttpStatus.OK);
    }



	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="/save", produces="application/json")
	public ResponseEntity<Object> save(
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "front", required = false) Boolean front,
			@RequestParam(value = "admin", required = false) Boolean admin
	)
	{
		try
		{
			if(!this.isAdmin()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			return new ResponseEntity<>(this.configurationService.updateValue(key, value, type, front, admin), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, key + "-" + value + "-" + type + "-" + front + "-" + admin);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="/add", produces="application/json")
	public ResponseEntity<Object> add(
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value
	)
	{
		try
		{
			if(!this.isAdmin()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			return new ResponseEntity<>(this.configurationService.addValue(key, value), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, key + "-" + value);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
