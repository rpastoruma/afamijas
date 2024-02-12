package afamijas.dao;

import afamijas.model.RelativeAbsence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RelativesAbsencesRepository extends MongoRepository<RelativeAbsence, String>
{

	//RELATIVES:

	@Query("{ '_id' : ?0 }")
    RelativeAbsence findOne(String id);

	@Query("{ 'idpatient' : ?0 }")
	List<RelativeAbsence> findAbsencesByPatient(String idpatient);




}
