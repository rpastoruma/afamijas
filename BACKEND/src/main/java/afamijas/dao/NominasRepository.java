package afamijas.dao;

import afamijas.model.Nomina;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NominasRepository extends MongoRepository<Nomina, String>
{

	@Query("{ '_id' : ?0 }")
	Nomina findOne(String id);



}
