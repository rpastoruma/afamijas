package afamijas.service;

import afamijas.dao.CitiesRepository;
import afamijas.dao.ConfigurationRepository;
import afamijas.dao.PostalcodesRepository;
import afamijas.dao.StatesRepository;
import afamijas.model.City;
import afamijas.model.Configuration;
import afamijas.model.PostalCode;
import afamijas.model.State;
import afamijas.utils.StringUtils;
import ch.qos.logback.core.joran.spi.ElementSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.*;

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
            else if(type.equals("NUMERIC"))  Double.parseDouble(StringUtils.replaceString(value, ",", "."));
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
            //System.out.println("PROVINCIA: " +  provincia.getId() + " - " + provincia.getName() );
            List<City> municipios = this.citiesRepository.findCitiesByIdState(provincia.getId());

            for(City municipio : municipios)
            {
                if(municipio.getPostalcodes()!=null && municipio.getPostalcodes().size()>0)
                    continue; //YA TIENE

                //System.out.println("\tMUNICIPIO: " +  municipio.getId() + " - " + municipio.getName() );

                List<PostalCode> codigospostales = this.postalcodesRepository.findPostalCodeByCityName(municipio.getName().toLowerCase());
                if(codigospostales==null || codigospostales.size()==0)
                {
                    //System.out.println(municipio.getName() + "|" + provincia.getName());
                    continue;
                }

                List<String> postalcodes = new ArrayList<>();
                for(PostalCode postalCode : codigospostales)
                    if(postalCode.getCodigo_postal().startsWith(provincia.getPrefix_postalcode()))
                        postalcodes.add(postalCode.getCodigo_postal());

                if(postalcodes.size()>0)
                {
                    System.out.println(municipio.getName());
                    municipio.setPostalcodes(postalcodes);
                    this.citiesRepository.save(municipio);
                }
                else
                {
                    //System.out.println("\t\t(2) NO SE ENCUENTRA CÓDIGOS POSTALES PARA " + municipio.getId() + " - " + municipio.getName());
                }

            }

        }



    }

*/






    //@Scheduled(fixedRate = 1000*60*500) // solo al inicio
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void namesInINENotInBD()
    {

        List<PostalCode> postalcodes = this.postalcodesRepository.findAll();

        Set<String> already = new HashSet<>();
        for(PostalCode postalCode : postalcodes)
        {
            List<City> cities = this.citiesRepository.findCitiesByNameAndCountryCode(postalCode.getMunicipio_nombre(), "ES");

            if(cities==null || cities.size()==0)
            {
                if(!already.contains(postalCode.getMunicipio_nombre().trim()))
                {
                    System.out.println(postalCode.getMunicipio_nombre());
                    already.add(postalCode.getMunicipio_nombre().trim());
                }
            }

        }
        System.out.println("FINALIZADO");
        System.exit(-1);


    }



    //@Scheduled(fixedRate = 1000*60*500) // solo al inicio
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void fixNamesInBD()
    {
        List<City> cityList = this.citiesRepository.findCitiesByCountryCode("ES");

        for(City city : cityList)
        {
            if(city.getName().indexOf(",")==-1)
            {
                if(city.getName().toLowerCase().endsWith(" el"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " el", ", el");
                    name = StringUtils.replaceString(name, " El", ", El");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" la"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " la", ", la");
                    name = StringUtils.replaceString(name, " La", ", La");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" los"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " los", ", los");
                    name = StringUtils.replaceString(name, " Los", ", Los");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" las"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " las", ", las");
                    name = StringUtils.replaceString(name, " Las", ", Las");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" os"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " os", ", os");
                    name = StringUtils.replaceString(name, " Os", ", Os");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" as"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " as", ", as");
                    name = StringUtils.replaceString(name, " As", ", As");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" o"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " o", ", o");
                    name = StringUtils.replaceString(name, " O", ", O");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" a"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " a", ", a");
                    name = StringUtils.replaceString(name, " A", ", A");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" l'"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " l'", ", l'");
                    name = StringUtils.replaceString(name, " L'", ", L'");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" els'"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " els", ", els");
                    name = StringUtils.replaceString(name, " Els", ", Els");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
                else if(city.getName().toLowerCase().endsWith(" les'"))
                {
                    String name = city.getName();
                    System.out.println(name + " ==> ");
                    name = StringUtils.replaceString(name, " les", ", les");
                    name = StringUtils.replaceString(name, " Les", ", Les");
                    System.out.println("\t" + name);
                    city.setName(name);
                    this.citiesRepository.save(city);
                }
            }
        }

        System.out.println("FINALIZADO");
        System.exit(-1);




    }




}
