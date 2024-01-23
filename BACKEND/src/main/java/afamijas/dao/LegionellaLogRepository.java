package afamijas.dao;

import afamijas.model.LegionellaLog;
import afamijas.model.TempFridge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface LegionellaLogRepository extends MongoRepository<LegionellaLog, String>
{

	@Query("{ '_id' : ?0 }")
	LegionellaLog findOne(String id);


	@Query("{ 'day' : ?0 }")
	LegionellaLog findOne(LocalDate day);


}
