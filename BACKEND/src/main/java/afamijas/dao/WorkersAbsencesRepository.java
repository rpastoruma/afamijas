package afamijas.dao;

import afamijas.model.RelativeAbsence;
import afamijas.model.WorkerAbsence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WorkersAbsencesRepository extends MongoRepository<WorkerAbsence, String>
{

	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	WorkerAbsence findOne(String id);

	@Query("{ 'idpatient' : ?0 }")
	List<WorkerAbsence> findAbsencesByPatient(String idpatient);

	@Query("{ 'idroutestop' : ?0 }")
	List<WorkerAbsence> findAbsencesByRouteStop(String idroutestop);



}
