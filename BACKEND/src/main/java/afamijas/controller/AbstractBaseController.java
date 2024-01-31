package afamijas.controller;


import afamijas.model.Roles;
import afamijas.model.User;
import afamijas.security.JwtUser;
import afamijas.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;


public abstract class AbstractBaseController
{
    protected UsersService usersService;

    @Autowired
    public AbstractBaseController(UsersService usersService)
    {
        this.usersService = usersService;
    }

    protected boolean isROOT()
    {
        return this.getRoles().contains(Roles.ROOT);
    }

    protected boolean isRELATIVE()
    {
        return this.getRoles().contains(Roles.RELATIVE);
    }

    protected boolean isTRANSPORT()
    {
        return this.getRoles().contains(Roles.TRANSPORT);
    }

    protected boolean isADMIN()
    {
        return this.getRoles().contains(Roles.ADMIN);
    }

    protected boolean isCLEANING()
    {
        return this.getRoles().contains(Roles.CLEANING);
    }

    protected boolean isNURSING()
    {
        return this.getRoles().contains(Roles.NURSING);
    }

    protected boolean isNURSING_ASSISTANT()
    {
        return this.getRoles().contains(Roles.NURSING_ASSISTANT);
    }

    protected boolean isLEGIONELLA_CONTROL()
    {
        return this.getRoles().contains(Roles.LEGIONELLA_CONTROL);
    }

    protected boolean isKITCHEN()
    {
        return this.getRoles().contains(Roles.KITCHEN);
    }

    protected boolean isMONITOR()
    {
        return this.getRoles().contains(Roles.MONITOR);
    }

    protected boolean isSOCIAL_WORKER()
    {
        return this.getRoles().contains(Roles.SOCIAL_WORKER);
    }

    protected boolean isPSYCHOLOGIST()
    {
        return this.getRoles().contains(Roles.PSYCHOLOGIST);
    }

    protected boolean isMANAGER()
    {
        return this.getRoles().contains(Roles.MANAGER);
    }

    protected boolean isPHYSIOTHERAPIST()
    {
        return this.getRoles().contains(Roles.PHYSIOTHERAPIST);
    }

    protected boolean isOCCUPATIONAL_THERAPIST()
    {
        return this.getRoles().contains(Roles.OCCUPATIONAL_THERAPIST);
    }

    protected boolean isOPERATOR_EXTRA_1()
    {
        return this.getRoles().contains(Roles.OPERATOR_EXTRA_1);
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



    protected List<String> getRoles()
    {
        try
        {
            JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user==null) return new ArrayList<>();
            else return user.getRoles();
        }
        catch (Exception e)
        {
            return new ArrayList<>();
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
