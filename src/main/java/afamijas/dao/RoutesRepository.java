package afamijas.dao;

import afamijas.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RoutesRepository extends MongoRepository<Route, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	Route findOne(String id);

	@Query("{ '_id' : ?0, 'status' : ?1 }")
	Route findOne(String id, String status);



}
