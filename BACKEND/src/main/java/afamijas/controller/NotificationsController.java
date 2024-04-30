package afamijas.controller;


//session = game + ads

import afamijas.service.ErrorsService;
import afamijas.service.NotificationsService;
import afamijas.service.UsersService;
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


import java.util.Arrays;

//@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "https://afamijas.org" , "https://app.afamijas.org" })
@RestController
@Validated
@RequestMapping("/private/notifications")
public class NotificationsController extends AbstractBaseController
{
    final NotificationsService notificationsService;

    final ErrorsService errorsService;

    @Autowired
    public NotificationsController(UsersService usersService, NotificationsService notificationsService, ErrorsService errorsService)
    {
        super(usersService);
        this.notificationsService = notificationsService;
        this.errorsService = errorsService;
    }


    /** FOR CROSS DOMAIN **/
    //@CrossOrigin
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> handle()
    {
        return new ResponseEntity<Object>(HttpStatus.OK);
    }



    /** VIEWER **/
    //@CrossOrigin





    /** ADMIN **/





    @RequestMapping(method=RequestMethod.GET, value="/findMyNotifications", produces="application/json")
    public ResponseEntity<?> findMyNotifications(
            HttpServletRequest request
    )
    {
        try
        {
            return new ResponseEntity<>(this.notificationsService.findMyNotifications(this.getUser()), HttpStatus.OK);
        }
        catch(Exception e)
        {
            if(e.getMessage().startsWith("ERROR: "))
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
            else
            {
                this.errorsService.sendError(e, this.getParameters(request));
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }




    @RequestMapping(method=RequestMethod.POST, value="/deleteAll", produces="application/json")
    public ResponseEntity<?> deleteAll(
            HttpServletRequest request
    )
    {
        try
        {
            this.notificationsService.deleteAll(this.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e)
        {
            if(e.getMessage().startsWith("ERROR: "))
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
            else
            {
                this.errorsService.sendError(e, this.getParameters(request));
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    @RequestMapping(method=RequestMethod.POST, value="/delete", produces="application/json")
    public ResponseEntity<?> delete(
            @RequestParam(value = "id", required = false) String id,
            HttpServletRequest request
    )
    {
        try
        {
            this.notificationsService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e)
        {
            if(e.getMessage().startsWith("ERROR: "))
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
            else
            {
                this.errorsService.sendError(e, this.getParameters(request));
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


}
