package afamijas.service;

import afamijas.dao.*;
import afamijas.model.*;
import afamijas.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class WorkersServiceImpl implements WorkersService
{
	@Value("${media.path}")
	String mediapath;

	final MongoTemplate mongoTemplate;

    final UsersRepository usersRepository;

	final FeedingRepository feedingRepository;

	final TempFridgeRepository tempFridgeRepository;

	final TempServicesRepository tempServicesRepository;

	final MealSamplesRepository mealSamplesRepository;

	final MediaService mediaService;


	@Autowired
	public WorkersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, FeedingRepository feedingRepository, TempFridgeRepository tempFridgeRepository, TempServicesRepository tempServicesRepository, MealSamplesRepository mealSamplesRepository, MediaService mediaService)
	{
		this.mongoTemplate = mongoTemplate;
		this.usersRepository = usersRepository;
		this.feedingRepository = feedingRepository;
		this.tempFridgeRepository = tempFridgeRepository;
		this.tempServicesRepository = tempServicesRepository;
		this.mealSamplesRepository = mealSamplesRepository;
		this.mediaService = mediaService;
	}

	@Override
	public Page<PatientDTO> getActivePatients(String name_surnames, String dni, String groupcode, Integer page, Integer size, String order, String orderasc)
	{
		Pageable pageable = PageRequest.of(page, size);

		Query query = new Query().addCriteria(Criteria.where("status").is("A")).with(pageable).with(Sort.by(orderasc.equals("ASC")?Sort.Direction.ASC:Sort.Direction.DESC, order));

		if(name_surnames!=null)
		{
			Criteria names_or_criteria = new Criteria();
			names_or_criteria.orOperator(Criteria.where("name").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname1").regex(".*"+name_surnames+".*", "i"),
					Criteria.where("surname2").regex(".*"+name_surnames+".*", "i"));

			query.addCriteria(names_or_criteria);
		}
		if(dni!=null) query.addCriteria(Criteria.where("dni").is(dni));
		if(groupcode!=null) query.addCriteria(Criteria.where("groupcode").is(groupcode));

		List<PatientDTO> list = mongoTemplate.find(query, User.class).stream().map(x -> new PatientDTO(x, null, null, null, null)).toList();

		return PageableExecutionUtils.getPage(
				list,
				pageable,
				() -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerFeeding(String idpatient, String idworker, String dish, String result, String daymeal)
	{
		Feeding feeding = this.feedingRepository.findFeedingByPatientDayDaymealAndDish(idpatient, LocalDate.now(), daymeal, dish);
		if(feeding==null) feeding = new Feeding();

		feeding.setIdpatient(idpatient);
		feeding.setIdworker(idworker);
		feeding.setDish(dish);
		feeding.setResult(result);
		feeding.setDaymeal(daymeal);
		feeding.setDay(LocalDate.now());

		this.feedingRepository.save(feeding);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerTempFridge(String idworker, Double tempfridge, Double tempfreezer)
	{
		TempFridge tempFridge = this.tempFridgeRepository.findOne(LocalDate.now());
		if(tempFridge==null) tempFridge = new TempFridge();

		tempFridge.setTemperature_fridge(tempfridge);
		tempFridge.setTemperature_freezer(tempfreezer);
		tempFridge.setDay(LocalDate.now());
		tempFridge.setIdworker(idworker);
		tempFridge.setOk(true);

		if(tempfridge>4.0) tempFridge.setOk(false);
		if(tempfreezer>-18.0) tempFridge.setOk(false);

		//TODO: ¿NOTIFICAR SI UNA TEMPERATURA ESTÁ MAL?

		this.tempFridgeRepository.save(tempFridge);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerTempService(String idworker, String dish, String dishtype, Double tempreception, Double tempservice)
	{
		TempService tempService = this.tempServicesRepository.findOneByDayAndDish(LocalDate.now(), dish);
		if(tempService==null) tempService = new TempService();

		tempService.setTemperature_service(tempservice);
		tempService.setTemperature_reception(tempreception);
		tempService.setDish(dish);
		tempService.setDish_type(dishtype);
		tempService.setIdworker(idworker);
		tempService.setDay(LocalDate.now());
		tempService.setOk(true);

		if(tempreception>4.0) tempService.setOk(false);
		if(dishtype.equals("COLD") && tempservice>8.0) tempService.setOk(false);
		if(dishtype.equals("HOT") && tempservice<65.0) tempService.setOk(false);

		//TODO: ¿NOTIFICAR SI UNA TEMPERATURA ESTÁ MAL?

		this.tempServicesRepository.save(tempService);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void registerMealSample(String idworker, String dish, Boolean organoletico, Boolean cuerposextra, String comments)
	{
		MealSample mealSample = this.mealSamplesRepository.findOneByDayAndDish(LocalDate.now(), dish);
		if(mealSample==null) mealSample = new MealSample();

		mealSample.setOrgenolepticoOk(organoletico);
		mealSample.setCuerposExtraOk(cuerposextra);
		mealSample.setComments(comments);
		mealSample.setDish(dish);
		mealSample.setDay(LocalDate.now());
		mealSample.setIdworker(idworker);

		//TODO: ¿NOTIFICAR SI UNA MUESTRA ESTÁ MAL?

		this.mealSamplesRepository.save(mealSample);
	}



}
