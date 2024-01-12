package afamijas.dao;

import afamijas.model.RouteStop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RouteStopsRepository extends MongoRepository<RouteStop, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	RouteStop findOne(String id);

	@Query("{ '_id' : ?0, 'status' : ?1 }")
	RouteStop findOne(String id, String status);

	@Query("{ 'idroute' : ?0, 'status' : 'A' }")
	List<RouteStop> findRouteStopsByRoute(String idroute);



}
