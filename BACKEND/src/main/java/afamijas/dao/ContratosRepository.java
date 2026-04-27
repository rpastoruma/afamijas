package afamijas.dao;

import afamijas.model.Contrato;
import afamijas.model.Nomina;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ContratosRepository extends MongoRepository<Contrato, String>
{

	@Query("{ '_id' : ?0 }")
    Contrato findOne(String id);



}
