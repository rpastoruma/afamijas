package afamijas.dao;

import afamijas.model.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String>
{

	@Query("{ 'key' : ?0 }")
	Optional<Configuration> findByKey(String key);

	@Query("{ 'front' : ?0 }")
	List<Configuration> findByFront(Boolean front);

	@Query("{ 'admin' : ?0 }")
	List<Configuration> findByAdmin(Boolean admin);


}
