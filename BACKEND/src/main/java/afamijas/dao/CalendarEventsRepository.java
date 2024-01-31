package afamijas.dao;

import afamijas.model.CalendarEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CalendarEventsRepository extends MongoRepository<CalendarEvent, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
    CalendarEvent findOne(String id);


	@Query("{ 'idsusers' : null, 'roles' : null }")
	List<CalendarEvent> findGeneric();




}
