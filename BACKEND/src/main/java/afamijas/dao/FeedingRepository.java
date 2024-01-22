package afamijas.dao;

import afamijas.model.Feeding;
import afamijas.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface FeedingRepository extends MongoRepository<Feeding, String>
{

	@Query("{ '_id' : ?0 }")
	Feeding findOne(String id);

	@Query("{ 'idpatient' : ?0 }")
	List<Feeding> findFeedingByPatient(String idpatient);

	@Query("{ 'idpatient' : ?0, 'day' : ?1, 'daymeal' : ?2, 'dish' : ?3  }")
	Feeding findFeedingByPatientDayDaymealAndDish(String idpatient, LocalDate day, String daymeal, String dish);



}
