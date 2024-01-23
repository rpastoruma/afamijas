package afamijas.dao;

import afamijas.model.LegionellaLog;
import afamijas.model.WCLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface WCLogRepository extends MongoRepository<WCLog, String>
{

	@Query("{ '_id' : ?0 }")
	WCLog findOne(String id);



}
