package afamijas.controller;


import afamijas.model.dto.MemberDTO;
import afamijas.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.List;

@RestController
@Validated
@RequestMapping("/private/members")
public class MembersController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final MembersService membersService;

	final MediaService mediaService;

	@Autowired
	public MembersController(UsersService usersService, ErrorsService errorsService, MembersService membersService, MediaService mediaService)
	{
		super(usersService);
		this.errorsService = errorsService;
		this.membersService = membersService;
		this.mediaService = mediaService;
	}


	@RequestMapping(method=RequestMethod.GET, value="getMembers", produces="application/json")
	public ResponseEntity<?> getMembers(
			@RequestParam(value = "membernumber", required = false) Integer membernumber,
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
			return new ResponseEntity<>(this.membersService.getMembers(membernumber, name_surnames, documentid, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}









	@RequestMapping(method=RequestMethod.POST, value="uploadRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadRegisterDocument(
			@RequestParam(value = "idmember", required = true) String idmember,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.membersService.uploadRegisterDocument(idmember, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="uploadUnRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadUnRegisterDocument(
			@RequestParam(value = "idmember", required = true) String idmember,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.membersService.uploadUnRegisterDocument(idmember, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="saveMember", produces="application/json")
	public ResponseEntity<?> saveMember(
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

			@RequestParam(value = "fee_euros", required = false) Double fee_euros,
			@RequestParam(value = "fee_period", required = false) String fee_period,
			@RequestParam(value = "fee_payment", required = false) String fee_payment,


			@RequestParam(value = "bank_name", required = false) String bank_name,
			@RequestParam(value = "bank_account_holder_fullname", required = false) String bank_account_holder_fullname,
			@RequestParam(value = "bank_account_holder_dni", required = false) String bank_account_holder_dni,
			@RequestParam(value = "bank_account_iban", required = false) String bank_account_iban,

			@RequestParam(value = "register_document_url", required = false) String register_document_url,
			@RequestParam(value = "is_document_signed", required = false) Boolean is_document_signed,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(is_document_signed==null) is_document_signed = false;

			return new ResponseEntity<>(this.membersService.saveMember(id, name, surname1, surname2, email, phone, documentid, documenttype, postaladdress, idcity, idstate, idcountry, postalcode,
					fee_euros, fee_period, fee_payment, bank_name, bank_account_holder_fullname, bank_account_holder_dni, bank_account_iban, register_document_url, is_document_signed), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="unregisterMember", produces="application/json")
	public ResponseEntity<?> unregisterMember(
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
			this.membersService.unregisterMember(id, unregister_reason, unregister_document_url, is_document_signed);
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
			@RequestParam(value = "idmember", required = true) String idmember,
			@RequestParam(value = "register_document_url_signed", required = true) String register_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.membersService.signDocumentRegister(idmember, register_document_url_signed);
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
			@RequestParam(value = "idmember", required = true) String idmember,
			@RequestParam(value = "unregister_document_url_signed", required = true) String unregister_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.membersService.signDocumentUnRegister(idmember, unregister_document_url_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.GET, value="getRelatives", produces="application/json")
	public ResponseEntity<?> getRelatives(
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
			return new ResponseEntity<>(this.membersService.getRelatives(name_surnames, documentid, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="saveRelative", produces="application/json")
	public ResponseEntity<?> saveRelative(
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

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.membersService.saveRelative(id, name, surname1, surname2, email, phone, documentid, documenttype, postaladdress, idcity, idstate, idcountry, postalcode), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="deleteRelative", produces="application/json")
	public ResponseEntity<?> deleteRelative(
			@RequestParam(value = "id", required = true) String id,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.membersService.deleteRelative(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}

