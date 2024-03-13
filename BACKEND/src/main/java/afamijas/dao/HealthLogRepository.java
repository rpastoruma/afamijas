package afamijas.dao;

import afamijas.model.HealthLog;
import afamijas.model.WCLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface HealthLogRepository extends MongoRepository<HealthLog, String>
{

	@Query("{ '_id' : ?0 }")
	HealthLog findOne(String id);



}
