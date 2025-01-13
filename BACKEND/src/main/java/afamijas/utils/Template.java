package afamijas.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

@Component
@Configuration
public class Template
{
    @Value("${utils.dirtemplates}")
    private String dirtemplates;

    final ApplicationContext appContext;

    public Template(ApplicationContext appContext)
    {
        this.appContext = appContext;
    }

    public String parseString(String templateTXT, HashMap<String, String> map)
    {
        try
        {
            Iterator<String> keys = map.keySet().iterator();
            while(keys.hasNext())
            {
                String key = (String) keys.next();
                String value = (String) map.get(key);
                if(value==null) value = "";
                templateTXT = StringUtils.replaceString(templateTXT, "###" + key.toUpperCase() + "###", value);
                templateTXT = StringUtils.replaceString(templateTXT, "###" + key.toLowerCase() + "###", value);
            }
            return templateTXT;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public String parse(String templatefile, HashMap<String, String> map, String encoding)
    {
        try
        {

            if(this.dirtemplates!=null && this.dirtemplates.length()>0)
                if(!this.dirtemplates.substring(this.dirtemplates.length()-1, this.dirtemplates.length()).equals(File.separator))
                    this.dirtemplates += File.separator;

            String filename = dirtemplates + templatefile;

            File file = appContext.getResource(filename).getFile();

            if(!file.exists()) throw new Exception("No existe el fichero: " + filename);

            String templateTXT = FileUtils.file2String(file.getPath(), encoding);

            return this.parseString(templateTXT, map);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public String parse(String templatefile, HashMap<String, String> map)
    {
        return parse(templatefile, map, "UTF-8");
    }








}
