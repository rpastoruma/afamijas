package afamijas.dao;

import afamijas.model.Feeding;
import afamijas.model.TempFridge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TempFridgeRepository extends MongoRepository<TempFridge, String>
{

	@Query("{ '_id' : ?0 }")
	TempFridge findOne(String id);


	@Query("{ 'day' : ?0 }")
	TempFridge findOne(LocalDate day);


}
