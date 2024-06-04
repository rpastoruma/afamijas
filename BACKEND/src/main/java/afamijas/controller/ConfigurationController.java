package afamijas.controller;

import afamijas.dao.CitiesRepository;
import afamijas.dao.CountriesRepository;
import afamijas.dao.StatesRepository;
import afamijas.model.City;
import afamijas.model.State;
import afamijas.service.ConfigurationService;
import afamijas.service.ErrorsService;
import afamijas.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/configuration")
public class ConfigurationController extends AbstractBaseController
{
	final ConfigurationService configurationService;
	final ErrorsService errorsService;

	final CountriesRepository countriesRepository;

	final StatesRepository statesRepository;

	final CitiesRepository citiesRepository;

	@Autowired
	public ConfigurationController(UsersService usersService, ConfigurationService configurationService, ErrorsService errorsService, CountriesRepository countriesRepository, StatesRepository statesRepository, CitiesRepository citiesRepository)
	{
		super(usersService);
		this.configurationService = configurationService;
		this.errorsService = errorsService;
		this.countriesRepository = countriesRepository;
		this.statesRepository = statesRepository;
		this.citiesRepository = citiesRepository;
	}

	/** FOR CROSS DOMAIN **/
	@CrossOrigin
	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> handle() 
	{
        return new ResponseEntity<Object>(HttpStatus.OK);
    }


	/* TODO: Mover estas llamadas a un controlador que empiece por /private

	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="/save", produces="application/json")
	public ResponseEntity<Object> save(
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "front", required = false) Boolean front,
			@RequestParam(value = "admin", required = false) Boolean admin
	)
	{
		try
		{
			if(!this.isRoot()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			return new ResponseEntity<>(this.configurationService.updateValue(key, value, type, front, admin), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, key + "-" + value + "-" + type + "-" + front + "-" + admin);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="/add", produces="application/json")
	public ResponseEntity<Object> add(
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value
	)
	{
		try
		{
			if(!this.isRoot()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			return new ResponseEntity<>(this.configurationService.addValue(key, value), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, key + "-" + value);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	*/

	@RequestMapping(method=RequestMethod.GET, value="/getFrontEndValues", produces="application/json")
	public ResponseEntity<?> getFrontEndValues()
	{
		try
		{
			return new ResponseEntity<>(configurationService.getFrontConfiguration(), HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="/getCountries", produces="application/json")
	public ResponseEntity<?> getCountries()
	{
		try
		{
			return new ResponseEntity<>(this.countriesRepository.findAll(), HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.GET, value="/getStates", produces="application/json")
	public ResponseEntity<?> getStates(@RequestParam(value = "idcountry", required = true) Integer idcountry)
	{
		try
		{
			return new ResponseEntity<>(this.statesRepository.findStatesByIdcountry(idcountry), HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="/getCities", produces="application/json")
	public ResponseEntity<?> getCities(@RequestParam(value = "idstate", required = true) Integer idstate)
	{
		try
		{
			return new ResponseEntity<>(this.citiesRepository.findCitiesByIdState(idstate), HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.GET, value="/getStateAndCitiesByPostalCodeAndCountryId", produces="application/json")
	public ResponseEntity<?> getStateAndCitiesByPostalCodeAndCountryId(
			@RequestParam(value = "postalcode", required = true) String postalcode,
			@RequestParam(value = "idcountry", required = true) Integer idcountry
	)
	{
		try
		{
			String prefixpostalcode = postalcode.substring(0, 2);
			State state = this.statesRepository.findStateByCountryIdAndPrefixPostalCode(idcountry, prefixpostalcode);
			if(state==null) return null;

			List<City> cities = this.citiesRepository.findCitiesByPostalCodeAndCountryId(postalcode, idcountry);

			if(cities==null)
			{
				cities = this.citiesRepository.findCitiesByIdState(state.getId());
			}

			HashMap<String, Object> result = new HashMap<>();
			result.put("state", state);
			result.put("cities", cities);

			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





}
