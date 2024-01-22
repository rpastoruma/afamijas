package afamijas.dao;

import afamijas.model.MealSample;
import afamijas.model.TempService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface MealSamplesRepository extends MongoRepository<MealSample, String>
{

	@Query("{ '_id' : ?0 }")
	MealSample findOne(String id);


	@Query("{ 'day' : ?0 }")
	List<MealSample> findTempByDay(LocalDate day);

	@Query("{ 'day' : ?0, 'dish' : ?1,  }")
	MealSample findOneByDayAndDish(LocalDate day, String dish);


}
