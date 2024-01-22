package afamijas.dao;

import afamijas.model.TempFridge;
import afamijas.model.TempService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TempServicesRepository extends MongoRepository<TempService, String>
{

	@Query("{ '_id' : ?0 }")
	TempService findOne(String id);


	@Query("{ 'day' : ?0 }")
	List<TempService> findTempByDay(LocalDate day);

	@Query("{ 'day' : ?0, 'dish' : ?1 }")
	TempService findOneByDayAndDish(LocalDate day, String dish);


}
