package afamijas.controller;

import afamijas.model.User;
import afamijas.queuemail.model.dto.SendingResultDTO;
import afamijas.queuemail.services.QueuemailHardyService;
import afamijas.security.JwtAuthenticationResponse;
import afamijas.security.JwtFilter;
import afamijas.service.CaptchaService;
import afamijas.service.ConfigurationService;
import afamijas.service.ErrorsService;
import afamijas.service.UsersService;
import afamijas.utils.PasswordPolicy;
import afamijas.utils.SendMail;
import afamijas.utils.Template;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/auth")
public class AuthController
{
	@Value("${main.url}")
	private String main_url;

	@Value("${api.url}")
	private String api_url;

	@Value("${queuemail.use}")
	private String use_queuemail;

	@Value("${spring.profiles.active}")
	private String active_profile;
	final UsersService usersService;
	final SendMail sendMail;
	final QueuemailHardyService queuemailHardyService;
	final ConfigurationService configurationService;
	final Template template;
	final CaptchaService captchaService;
	final ErrorsService errorsService;


	@Autowired
	public AuthController(UsersService usersService, SendMail sendMail, QueuemailHardyService queuemailHardyService, ConfigurationService configurationService, Template template, CaptchaService captchaService, ErrorsService errorsService)
	{
		this.usersService = usersService;
		this.sendMail = sendMail;
		this.queuemailHardyService = queuemailHardyService;
		this.configurationService = configurationService;
		this.template = template;
		this.captchaService = captchaService;
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
	@RequestMapping(method=RequestMethod.POST, value="/login", produces="application/json")
	public ResponseEntity<JwtAuthenticationResponse> login(
				@RequestParam(value = "username", required = true) String username,
				@RequestParam(value = "password", required = true) String password
             )
	{
 	    try
		{
 			username = username.trim().toLowerCase();
 			password = password.trim();
 			
 			final User user = usersService.findByUsername(username);
 		    if(user==null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
 		    if(!user.getStatus().equals("A")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
 		    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 		    if(!passwordEncoder.matches(password, user.getPassword())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
 		    	
 		    final Instant now = Instant.now();

			JwtAuthenticationResponse response = new JwtAuthenticationResponse(Jwts.builder().setId(""+user.get_id()).setSubject(username).setAudience(String.join(",", user.getRoles())).setIssuer("afamijas").setIssuedAt(Date.from(now)).setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS))).signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(JwtFilter.SECRET )).compact(),
					user.getRoles(),
					user.get_id(),
					user.getUsername(),
					user.getDocumentid(),
					user.getFullname(),
					user.getPhoto_url());

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, username + "-" + password);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}







	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="requestRememberPassword", produces="application/json")
	public ResponseEntity<?> requestRememberPassword(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "captchatoken", required = false) String captchatoken,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.configurationService.value("google.recaptcha.apikey").equals("") && active_profile.equals("prod"))
				try { this.captchaService.processResponse(captchatoken, request.getRemoteAddr()); }
				catch(Exception e) { return new ResponseEntity<>(HttpStatus.CONFLICT); }

			User user = null;
			if(username!=null) user = this.usersService.findByUsername(username);

			if(user==null && email!=null)
			{
				List<User> posible_users = this.usersService.findByEmail(email);
				if(posible_users!=null && posible_users.size()>0)
					for(User theuser : posible_users)
						this.sendRememberPasswordLink(theuser);
			}
			else if(user!=null)
			{
				this.sendRememberPasswordLink(user);
			}

			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, username + "-" + email + "-" + captchatoken);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, value="confirmRequestPassword", produces="application/json")
	public ModelAndView confirmRequestPassword(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "offset", required = true) String offset,
			@RequestParam(value = "token", required = true) String token
	)
	{
		try
		{
			User user = this.usersService.findByUsername(username);
			if(user==null || !user.getStatus().equals("A")) throw new Exception("1");
			if(!offset.equals(user.get_id())) throw new Exception("2");
			if(!token.equals(user.getToken())) throw new Exception("3");

			this.sendNewPassword(user);

			return new ModelAndView("redirect:" + main_url + "/confirmrequestpassword_ok.html");
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, username + "-" + offset + "-" + token);
			return new ModelAndView("redirect:" + main_url + "/confirmrequestpassword_fails.html");
		}
	}


	// PRIVATE

	/* TODO: POR AHORA NO SE USA .. QUIZÁS PARA SOCIOS... SI NO SE ACABA USANDO ELIMINAR
	private void sendConfirmLink(User user) throws Exception
	{
		//String fromemail = this.configurationService.value("fromemail");
		String fromemail = "info@afamijas.org";
		String mainurl = this.configurationService.value("mainurl");

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("signupconfirmlink", mainurl + "/api/auth/signupConfirm?username=" + user.getUsername() + "&offset=" + user.get_id() + "&token=" + user.getToken());
		values.put("mainurl", mainurl);
		values.put("logo", this.configurationService.value("logo"));
		String body = template.parse("mail_signup.html", values);

		if(use_queuemail==null) use_queuemail = this.configurationService.value("queuemail.use");
		if(use_queuemail.equals("true"))
		{
			//ENVIAMOS POR QUEUEMAIL CON SERVICIO REBUSTO QUE IMPLEMENTA COLA LOCAL DE EMAILS FALLIDOS
			SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, fromemail, "AFA Mijas",  user.getEmail(), user.getEmail(), null, null, fromemail,
					this.configurationService.value("signupsubject"), "UTF-8", body, "UTF-8", "text/html",
					null, null, null,
					true, false,
					true, null);

			if(sendingResultDTO==null || (sendingResultDTO.getLocalqueued()!=null && sendingResultDTO.getLocalqueued()==false) )
			{
				this.errorsService.sendWarning("Email local-queuing failed", sendingResultDTO==null?"NULL":sendingResultDTO.getError());
				this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, this.configurationService.value("signupsubject"), "UTF-8", body, "UTF-8", null, "html");
			}
		}
		else
		{
			this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, this.configurationService.value("signupsubject"), "UTF-8", body, "UTF-8", null, "html");
		}

		this.errorsService.sendWarning("New API Key requested", user.getEmail() + " has requested and API Key.");
	}
    */

	/* TODO: POR AHORA NO SE USA .. QUIZÁS PARA SOCIOS... SI NO SE ACABA USANDO ELIMINAR
	private void sendWelcomeEmail(User user) throws Exception
	{
		//String fromemail = this.configurationService.value("fromemail");
		String fromemail = "info@afamijas.org";
		String mainurl = this.configurationService.value("mainurl");

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("mainurl", mainurl);
		values.put("logo", this.configurationService.value("logo"));
		String body = template.parse("mail_welcome.html", values);

		if(use_queuemail==null) use_queuemail = this.configurationService.value("queuemail.use");
		if(use_queuemail.equals("true"))
		{
			//ENVIAMOS POR QUEUEMAIL CON SERVICIO REBUSTO QUE IMPLEMENTA COLA LOCAL DE EMAILS FALLIDOS
			SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, fromemail, "AFA Mijas",  user.getEmail(), user.getEmail(), null, null, fromemail,
					this.configurationService.value("welcomesubject"), "UTF-8", body, "UTF-8", "text/html",
					null, null, null,
					true, false,
					true, null);

			if(sendingResultDTO==null || (sendingResultDTO.getLocalqueued()!=null && sendingResultDTO.getLocalqueued()==false) )
			{
				this.errorsService.sendWarning("Email local-queuing failed", sendingResultDTO==null?"NULL":sendingResultDTO.getError());
				this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, this.configurationService.value("welcomesubject"), "UTF-8", body, "UTF-8", null, "html");
			}
		}
		else
		{
			this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, this.configurationService.value("welcomesubject"), "UTF-8", body, "UTF-8", null, "html");
		}
	}
    */

	private void sendRememberPasswordLink(User user) throws Exception
	{
		//String fromemail = this.configurationService.value("fromemail");
		String fromemail = "info@afamijas.org";

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("username", user.getUsername());
		values.put("email", user.getEmail());
		values.put("resetpasswordlink", api_url + "/api/auth/confirmRequestPassword?username=" + user.getUsername() + "&offset=" + user.get_id() + "&token=" + user.getToken());
		String body = template.parse("mail_rememberpassword.html", values);

		String rememberpasswordsubject = "Confirme su solicitud de nueva contraseña";
		if(use_queuemail==null) use_queuemail = this.configurationService.value("queuemail.use");
		if(use_queuemail.equals("true"))
		{
			//ENVIAMOS POR QUEUEMAIL CON SERVICIO REBUSTO QUE IMPLEMENTA COLA LOCAL DE EMAILS FALLIDOS
			SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, fromemail, "AFA Mijas", user.getEmail(), user.getEmail(), null, null, fromemail,
					rememberpasswordsubject, "UTF-8", body, "UTF-8", "text/html",
					null, null, null,
					true, false,
					true, null);

			if (sendingResultDTO == null || (sendingResultDTO.getLocalqueued() != null && sendingResultDTO.getLocalqueued() == false)) {
				this.errorsService.sendWarning("Email local-queuing failed", sendingResultDTO == null ? "NULL" : sendingResultDTO.getError());
				this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, rememberpasswordsubject, "UTF-8", body, "UTF-8", null, "html");
			}
		}
		else
		{
			this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, rememberpasswordsubject, "UTF-8", body, "UTF-8", null, "html");
		}
	}

	private void sendNewPassword(User user) throws Exception
	{
		//String fromemail = this.configurationService.value("fromemail");
		String fromemail = "info@afamijas.org";

		String newpassword = PasswordPolicy.generate();
		user.setPassword(new BCryptPasswordEncoder().encode(newpassword));
		user.setToken(UUID.randomUUID().toString()); //renovamos token
		user = this.usersService.save(user);

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("newpassword", newpassword);
		values.put("username", user.getUsername());
		String body = template.parse("mail_sendnewpassword.html", values);

		String newpasswordsubject = "Sus nuevos datos de acceso";

		if(use_queuemail==null) use_queuemail = this.configurationService.value("queuemail.use");
		if(use_queuemail.equals("true"))
		{
			//ENVIAMOS POR QUEUEMAIL CON SERVICIO REBUSTO QUE IMPLEMENTA COLA LOCAL DE EMAILS FALLIDOS
			SendingResultDTO sendingResultDTO = this.queuemailHardyService.sendEmail(null, fromemail, "AFA Mijas",  user.getEmail(), user.getEmail(), null, null, fromemail,
					newpasswordsubject, "UTF-8", body, "UTF-8", "text/html",
					null, null, null,
					true, false,
					true, null);

			if(sendingResultDTO==null || (sendingResultDTO.getLocalqueued()!=null && sendingResultDTO.getLocalqueued()==false) )
			{
				this.errorsService.sendWarning("Email local-queuing failed", sendingResultDTO==null?"NULL":sendingResultDTO.getError());
				this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, newpasswordsubject, "UTF-8", body, "UTF-8", null, "html");
			}
		}
		else
		{
			this.sendMail.send(fromemail, user.getEmail(), "AFA Mijas", user.getEmail(), null, null, fromemail, newpasswordsubject, "UTF-8", body, "UTF-8", null, "html");
		}

	}

}
