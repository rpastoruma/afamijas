package afamijas.dao;

import afamijas.model.Absence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AbsencesRepository extends MongoRepository<Absence, String>
{

	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	Absence findOne(String id);

	@Query("{ 'idpatient' : ?0 }")
	List<Absence> findAbsencesByPatient(String idpatient);

	@Query("{ 'idroutestop' : ?0 }")
	List<Absence> findAbsencesByRouteStop(String idroutestop);



}
