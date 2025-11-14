package afamijas.controller;


import afamijas.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping("/private/volunteers")
public class VolunteersController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final VolunteersService volunteersService;

	final MediaService mediaService;

	@Autowired
	public VolunteersController(UsersService usersService, ErrorsService errorsService, VolunteersService volunteersService, MediaService mediaService)
	{
		super(usersService);
		this.errorsService = errorsService;
        this.volunteersService = volunteersService;
		this.mediaService = mediaService;
	}


	@RequestMapping(method=RequestMethod.GET, value="getVolunteers", produces="application/json")
	public ResponseEntity<?> getVolunteers(
			@RequestParam(value = "name_surnames", required = false) String name_surnames,
			@RequestParam(value = "documentid", required = false) String documentid,
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
			if(!this.isADMIN() && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "name";
			if(orderasc==null) orderasc = "ASC";
			return new ResponseEntity<>(this.volunteersService.getVolunteers(name_surnames, documentid, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}







	@RequestMapping(method=RequestMethod.POST, value="uploadRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadRegisterDocument(
			@RequestParam(value = "idvolunteer", required = true) String idvolunteer,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.volunteersService.uploadRegisterDocument(idvolunteer, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="uploadUnRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadUnRegisterDocument(
			@RequestParam(value = "idvolunteer", required = true) String idvolunteer,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.volunteersService.uploadUnRegisterDocument(idvolunteer, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="saveVolunteer", produces="application/json")
	public ResponseEntity<?> saveVolunteer(
			@RequestParam(value = "id", required = false) String id,

			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "surname1", required = false) String surname1,
			@RequestParam(value = "surname2", required = false) String surname2,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "documentid", required = false) String documentid,
			@RequestParam(value = "documenttype", required = false) String documenttype,

			@RequestParam(value = "postaladdress", required = false) String postaladdress,
			@RequestParam(value = "idcity", required = false) Integer idcity,
			@RequestParam(value = "idstate", required = false) Integer idstate,
			@RequestParam(value = "idcountry", required = false) Integer idcountry,
			@RequestParam(value = "postalcode", required = false) String postalcode,

			@RequestParam(value = "register_document_url", required = false) String register_document_url,
			@RequestParam(value = "is_document_signed", required = false) Boolean is_document_signed,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(is_document_signed==null) is_document_signed = false;

			return new ResponseEntity<>(this.volunteersService.saveVolunteer(id, name, surname1, surname2, email, phone, documentid, documenttype, postaladdress, idcity, idstate, idcountry, postalcode,
																			register_document_url, is_document_signed), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="unregisterVolunteer", produces="application/json")
	public ResponseEntity<?> unregisterVolunteer(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "unregister_reason", required = true) String unregister_reason,
			@RequestParam(value = "unregister_document_url", required = false) String unregister_document_url,
			@RequestParam(value = "is_document_signed", required = false) Boolean is_document_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(is_document_signed==null) is_document_signed = false;
			this.volunteersService.unregisterVolunteer(id, unregister_reason, unregister_document_url, is_document_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="signDocumentRegister", produces="application/json")
	public ResponseEntity<?> signDocumentRegister(
			@RequestParam(value = "idvolunteer", required = true) String idvolunteer,
			@RequestParam(value = "register_document_url_signed", required = true) String register_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.volunteersService.signDocumentRegister(idvolunteer, register_document_url_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="signDocumentUnRegister", produces="application/json")
	public ResponseEntity<?> signDocumentUnRegister(
			@RequestParam(value = "idvolunteer", required = true) String idvolunteer,
			@RequestParam(value = "unregister_document_url_signed", required = true) String unregister_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.volunteersService.signDocumentUnRegister(idvolunteer, unregister_document_url_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




}

