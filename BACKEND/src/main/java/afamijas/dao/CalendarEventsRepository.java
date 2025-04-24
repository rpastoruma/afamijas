package afamijas.dao;

import afamijas.model.CalendarEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface CalendarEventsRepository extends MongoRepository<CalendarEvent, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
    CalendarEvent findOne(String id);


	@Query("{ 'idsusers' : null, 'roles' : null }")
	List<CalendarEvent> findGeneric();

	//List<CalendarEvent> findByIdsusersContaining(String iduser);


	//List<CalendarEvent> findByIdsusersContainingAndEndAfterOrderByStartAsc(String iduser, LocalDateTime now);

	//@Query("{ 'idsusers': ?0, 'end': { $gt: ?1 }, 'title': { $regex: ?2, $options: 'i' } }")
	//List<CalendarEvent> findUpcomingByUserAndTitlePrefix(String iduser, LocalDateTime now, String titleRegex);

	List<CalendarEvent> findByIdsusersContainingOrderByStartDesc(String iduser);
}
