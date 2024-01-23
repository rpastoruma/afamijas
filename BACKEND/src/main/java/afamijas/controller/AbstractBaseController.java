package afamijas.controller;


import afamijas.model.User;
import afamijas.security.JwtUser;
import afamijas.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class AbstractBaseController
{
    protected UsersService usersService;

    @Autowired
    public AbstractBaseController(UsersService usersService)
    {
        this.usersService = usersService;
    }

    protected boolean isRoot()
    {
        return this.getRole().equals("ROOT");
    }

    protected boolean isRelative()
    {
        return this.getRole().equals("RELATIVE");
    }

    protected boolean isAdmin()
    {
        return this.getRole().equals("ADMIN");
    }

    protected boolean isKitchen()
    {
        return this.getRole().equals("KITCHEN");
    }

    protected boolean isManager()
    {
        return this.getRole().equals("MANAGER");
    }

    protected boolean isNursing()
    {
        return this.getRole().equals("NURSING");
    }

    protected boolean isNursingAssistant()
    {
        return this.getRole().equals("NURSING_ASSISTANT");
    }

    protected boolean isLegionellaControl()
    {
        return this.getRole().equals("LEGIONELLA_CONTROL");
    }


    protected boolean isMonitor()
    {
        return this.getRole().equals("MONITOR");
    }



    protected boolean isPatientForRelative(String idpatient)
    {
        User patient = this.usersService.findOne(idpatient, "PATIENT", "A");
        if(patient==null) return false;

        if(patient.getIdrelative().equals(this.getId())) return true;
        else return false;
    }



    protected String getId()
    {
        try
        {
            JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user==null) return null;
            else return user.getId();
        }
        catch (Exception e)
        {
            return null;
        }
    }



    private String getRole()
    {
        try
        {
            JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user==null) return null;
            else return user.getRole();
        }
        catch (Exception e)
        {
            return null;
        }
    }



    protected String getParameters(HttpServletRequest httpServletRequest)
    {
        String res = "";
        Map<Object, Object> parameters = Collections.list(httpServletRequest.getParameterNames()).stream().collect(Collectors.toMap(parameterName -> parameterName, httpServletRequest::getParameterValues));

        for (Map.Entry<Object, Object> entry : parameters.entrySet())
        {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if(value==null) continue;

            res += (String) key +  " = ";
            if(value instanceof String[])
            {
                int len = ((String[])value).length;
                if(len>1)
                {
                    res += "{";
                    for(int i = 0; i<len; i++) res += ((String[])value)[i] + ", ";
                    res += "}\n";
                }
                else
                    res += Arrays.toString((String[])value) + "\n";
            }
            else
            {
                res += "N/A\n";
            }
        }
        return res;
    }

}
