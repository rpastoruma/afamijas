package afamijas.service;

import afamijas.dao.ConfigurationRepository;
import afamijas.model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService
{
    final ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository)
    {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public String value(String key)
    {
        Optional<Configuration> val = this.configurationRepository.findByKey(key);
        if(val.isPresent()) return val.get().getValue();
        else return "";
    }

    @Override
    public String updateValue(String key, String value, String type, Boolean front, Boolean admin)
    {
        Configuration conf;

        if(type==null) type = "TEXT";
        if(front==null) front = false;
        if(admin==null) admin = false;

        Optional<Configuration> val = this.configurationRepository.findByKey(key);
        if(val.isPresent())
        {
            conf = val.get();
            type = conf.getType();
            front = conf.getFront();
            admin = conf.getAdmin();
        }
        else conf = new Configuration();

        try
        {
            if(type.equals("TEXT") || type.equals("HTML")) ;
            else if(type.equals("NUMERIC")) new Double(StringUtils.replace(value, ",", "."));
            else if(type.equals("BOOLEAN")) new Boolean(value);
            else new SimpleDateFormat(type).parse(value);
        }
        catch(Exception e)
        {
            return null;
        }

        conf.setKey(key);
        conf.setValue(value);
        conf.setType(type);
        conf.setFront(front);
        conf.setAdmin(admin);
        conf = this.configurationRepository.save(conf);
        return conf.getValue();
    }

    @Override
    public String addValue(String key, String value)
    {
        Configuration conf;
        Optional<Configuration> val = this.configurationRepository.findByKey(key);
        if(val.isPresent()) conf = val.get();
        else return null;

        conf.setValue(conf.getValue() +  "," + value);
        conf = this.configurationRepository.save(conf);
        return conf.getValue();
    }


    @Override
    public void delete(String id)
    {
        this.configurationRepository.deleteById(id);
    }

    @Override
    public List<Configuration> getFrontConfiguration()
    {
        return this.configurationRepository.findByFront(true);
    }

    @Override
    public List<Configuration> getAdminConfiguration()
    {
        return this.configurationRepository.findByAdmin(true);
    }
}
