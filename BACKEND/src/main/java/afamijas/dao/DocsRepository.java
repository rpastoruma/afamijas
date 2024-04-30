package afamijas.dao;

import afamijas.model.Doc;
import afamijas.model.HealthLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface DocsRepository extends MongoRepository<Doc, String>
{

	@Query("{ '_id' : ?0 }")
	Doc findOne(String id);



}
