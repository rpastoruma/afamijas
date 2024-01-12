package afamijas.dao;

import afamijas.model.CalendarDay;
import afamijas.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CalendarDaysRepository extends MongoRepository<CalendarDay, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	CalendarDay findOne(String id);

	@Query("{ 'iduser' : ?0 }")
	List<CalendarDay> findCalendarDaysByUser(String iduser);

	@Query("{ 'role' : ?0 }")
	List<CalendarDay> findCalendarDaysByRole(String role);

	@Query("{ 'iduser' : null, 'role' : null }")
	List<CalendarDay> findGeneric();




}
