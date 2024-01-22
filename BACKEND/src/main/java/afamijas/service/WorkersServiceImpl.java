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

	final MediaService mediaService;


	@Autowired
	public WorkersServiceImpl(MongoTemplate mongoTemplate, UsersRepository usersRepository, FeedingRepository feedingRepository, MediaService mediaService)
	{
		this.mongoTemplate = mongoTemplate;
		this.usersRepository = usersRepository;
		this.feedingRepository = feedingRepository;
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


}
