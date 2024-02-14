package afamijas.controller;


import afamijas.service.ErrorsService;
import afamijas.service.MediaService;
import afamijas.service.RelativesService;
import afamijas.service.UsersService;
import afamijas.utils.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/private/media")
public class MediaController extends AbstractBaseController
{
	@Value("${media.path}")
	String mediapath;

	final MediaService mediaService;

	final ErrorsService errorsService;

	@Autowired
	public MediaController(UsersService usersService, MediaService mediaService, ErrorsService errorsService)
	{
		super(usersService);
		this.mediaService = mediaService;
		this.errorsService = errorsService;
	}

	@RequestMapping(method=RequestMethod.POST, value="uploadFile", produces="application/json")
	public ResponseEntity<?> uploadFile(
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			String fileName = UUID.randomUUID() + "-" + FileUtils.sanitizeFilename(file.getOriginalFilename());
			String wholePath = mediapath + File.separator + fileName;
			file.transferTo(Paths.get(wholePath));
			String cdnurl = this.mediaService.uploadFileFTP("signed_" + this.getId(), fileName,  new FileInputStream(wholePath));
			try { new File(wholePath).delete(); } catch (Exception e) { e.printStackTrace(); }
			return new ResponseEntity<>(cdnurl, HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

