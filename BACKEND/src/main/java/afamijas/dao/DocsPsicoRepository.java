package afamijas.dao;

import afamijas.model.Doc;
import afamijas.model.DocPsico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface DocsPsicoRepository extends MongoRepository<DocPsico, String>
{

	@Query("{ '_id' : ?0 }")
	DocPsico findOne(String id);



}
