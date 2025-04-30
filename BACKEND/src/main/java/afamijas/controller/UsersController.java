package afamijas.controller;

import afamijas.service.ErrorsService;
import afamijas.service.UsersService;
import afamijas.utils.PasswordPolicy;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/private/users")
public class UsersController extends AbstractBaseController
{
	final ErrorsService errorsService;

	@Autowired
	public UsersController(UsersService usersService, ErrorsService errorsService)
	{
		super(usersService);
        this.errorsService = errorsService;
    }

	/** FOR CROSS DOMAIN **/
	@CrossOrigin
	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> handle() 
	{
        return new ResponseEntity<Object>(HttpStatus.OK);
    }


	@RequestMapping(method=RequestMethod.POST, value="changePass", produces="application/json")
	public ResponseEntity<?> changePass(
			@RequestParam(value = "newpassword", required = true) String newpassword,
			@RequestParam(value = "newpassword2", required = true) String newpassword2,
			HttpServletRequest request
	)
	{
		try
		{
			if(!newpassword2.equals(newpassword)) return new ResponseEntity<>(HttpStatus.CONFLICT);

			if(!PasswordPolicy.check(newpassword)) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

			return new ResponseEntity<>(this.usersService.changePass(this.getUser(), newpassword), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





}
