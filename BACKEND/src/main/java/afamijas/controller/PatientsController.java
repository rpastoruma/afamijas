package afamijas.controller;


import afamijas.service.*;
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
@RequestMapping("/private/patients")
public class PatientsController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final PatientsService patientsService;

	final MediaService mediaService;

	@Autowired
	public PatientsController(UsersService usersService, ErrorsService errorsService, PatientsService patientsService, MediaService mediaService)
	{
		super(usersService);
		this.errorsService = errorsService;
		this.patientsService = patientsService;
		this.mediaService = mediaService;
	}


	@RequestMapping(method=RequestMethod.GET, value="getPatients", produces="application/json")
	public ResponseEntity<?> getPatients(
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
			return new ResponseEntity<>(this.patientsService.getPatients(name_surnames, documentid, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}









	@RequestMapping(method=RequestMethod.POST, value="uploadRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadRegisterDocument(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.patientsService.uploadRegisterDocument(idpatient, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="uploadUnRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadUnRegisterDocument(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.patientsService.uploadUnRegisterDocument(idpatient, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="savePatient", produces="application/json")
	public ResponseEntity<?> savePatient(
			@RequestParam(value = "id", required = false) String id,

			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "surname1", required = false) String surname1,
			@RequestParam(value = "surname2", required = false) String surname2,
			@RequestParam(value = "birthdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
			@RequestParam(value = "documentid", required = false) String documentid,
			@RequestParam(value = "documenttype", required = false) String documenttype,

			@RequestParam(value = "idrelative", required = false) String idrelative,
			@RequestParam(value = "relativerelation", required = false) String relativerelation,

			@RequestParam(value = "postaladdress", required = false) String postaladdress,
			@RequestParam(value = "idcity", required = false) Integer idcity,
			@RequestParam(value = "idstate", required = false) Integer idstate,
			@RequestParam(value = "idcountry", required = false) Integer idcountry,
			@RequestParam(value = "postalcode", required = false) String postalcode,

			@RequestParam(value = "servicetype", required = false) String servicetype,
			@RequestParam(value = "tallerpsico", required = false) Boolean tallerpsico,

			@RequestParam(value = "transportservice", required = false) Boolean transportservice,
			@RequestParam(value = "transportservice_text", required = false) String transportservice_text,

			@RequestParam(value = "comedorservice", required = false) Boolean comedorservice,
			@RequestParam(value = "comedorservice_text", required = false) String comedorservice_text,

			@RequestParam(value = "ayudadomicilioservice", required = false) Boolean ayudadomicilioservice,
			@RequestParam(value = "ayudadomicilioservice_text", required = false) String ayudadomicilioservice_text,

			@RequestParam(value = "duchaservice", required = false) Boolean duchaservice,
			@RequestParam(value = "duchaservice_text", required = false) String duchaservice_text,

			@RequestParam(value = "register_document_url", required = false) String register_document_url,
			@RequestParam(value = "is_document_signed", required = false) Boolean is_document_signed,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(is_document_signed==null) is_document_signed = false;

			return new ResponseEntity<>(this.patientsService.savePatient(id, name, surname1, surname2, birthdate, documentid, documenttype, postaladdress, idrelative, relativerelation,
					idcity, idstate, idcountry, postalcode,
					servicetype, tallerpsico, transportservice, transportservice_text, comedorservice, comedorservice_text, ayudadomicilioservice, ayudadomicilioservice_text, duchaservice, duchaservice_text,
					register_document_url, is_document_signed), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="unregisterPatient", produces="application/json")
	public ResponseEntity<?> unregisterPatient(
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
			this.patientsService.unregisterPatient(id, unregister_reason, unregister_document_url, is_document_signed);
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
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "register_document_url_signed", required = true) String register_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.patientsService.signDocumentRegister(idpatient, register_document_url_signed);
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
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "unregister_document_url_signed", required = true) String unregister_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.patientsService.signDocumentUnRegister(idpatient, unregister_document_url_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}

