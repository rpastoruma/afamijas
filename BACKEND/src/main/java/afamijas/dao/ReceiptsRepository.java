package afamijas.dao;

import afamijas.model.Doc;
import afamijas.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReceiptsRepository extends MongoRepository<Receipt, String>
{

	@Query("{ '_id' : ?0 }")
	Receipt findOne(String id);



}
