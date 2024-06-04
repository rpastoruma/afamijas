package afamijas.service;

import afamijas.dao.CitiesRepository;
import afamijas.dao.ConfigurationRepository;
import afamijas.dao.PostalcodesRepository;
import afamijas.dao.StatesRepository;
import afamijas.model.City;
import afamijas.model.Configuration;
import afamijas.model.PostalCode;
import afamijas.model.State;
import ch.qos.logback.core.joran.spi.ElementSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService
{
    final ConfigurationRepository configurationRepository;

    final StatesRepository statesRepository;

    final CitiesRepository citiesRepository;

    final PostalcodesRepository postalcodesRepository;


    @Autowired
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository, StatesRepository statesRepository, CitiesRepository citiesRepository, PostalcodesRepository postalcodesRepository)
    {
        this.configurationRepository = configurationRepository;
        this.statesRepository = statesRepository;
        this.citiesRepository = citiesRepository;
        this.postalcodesRepository = postalcodesRepository;
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
            else if(type.equals("NUMERIC"))  Double.parseDouble(StringUtils.replace(value, ",", "."));
            else if(type.equals("BOOLEAN")) Boolean.parseBoolean(value);
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


  /*
    IMPORTA LOS CÓDIGOS POSTALES DEL INE (COLECCIÓN postalcodes) EN LA COLECCIÓN citie (campo array postalcodes)

    TODO: HABRÍA QUE ACTIVARLO DE NUEVO CUANDO SE SOLUCIONEN A MANO TODAS LAS INCIDENCIAS DE BLOQUE1.txt y BLOQUE2.txt


    @Scheduled(fixedRate = 1000*60*500) // solo al inicio
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void importPostalCodes()
    {

        List<State> provincias = this.statesRepository.findStatesByIdcountry(207); //ESPAÑA

        for(State provincia : provincias)
        {
            System.out.println("PROVINCIA: " +  provincia.getId() + " - " + provincia.getName() );
            List<City> municipios = this.citiesRepository.findCitiesByIdState(provincia.getId());

            for(City municipio : municipios)
            {
                if(municipio.getPostalcodes()!=null && municipio.getPostalcodes().size()>0)
                    continue; //YA TIENE

                //System.out.println("\tMUNICIPIO: " +  municipio.getId() + " - " + municipio.getName() );

                List<PostalCode> codigospostales = this.postalcodesRepository.findPostalCodeByCityName(municipio.getName());
                if(codigospostales==null || codigospostales.size()==0)
                {
                    //System.out.println("\t\t(1) NO SE ENCUENTRA CÓDIGOS POSTALES PARA " + municipio.getId() + " - " + municipio.getName());
                    continue;
                }

                List<String> postalcodes = new ArrayList<>();
                for(PostalCode postalCode : codigospostales)
                    if(postalCode.getCodigo_postal().startsWith(provincia.getPrefix_postalcode()))
                        postalcodes.add(postalCode.getCodigo_postal());

                if(postalcodes.size()>0)
                {
                    municipio.setPostalcodes(postalcodes);
                    this.citiesRepository.save(municipio);
                }
                else
                {
                    System.out.println("\t\t(2) NO SE ENCUENTRA CÓDIGOS POSTALES PARA " + municipio.getId() + " - " + municipio.getName());
                }

            }

        }


    }
   */





}
