package afamijas.dao;

import afamijas.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LogsRepository extends MongoRepository<Log, String>
{

	@Query("{ '_id' : ?0 }")
	Optional<Log> findById(String id);

	@Query("{ 'username' : ?0 }")
	List<Log> findByUsername(String username);


	
}
