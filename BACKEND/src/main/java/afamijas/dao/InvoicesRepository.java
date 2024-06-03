package afamijas.dao;

import afamijas.model.Invoice;
import afamijas.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoicesRepository extends MongoRepository<Invoice, String>
{

	@Query("{ '_id' : ?0 }")
	Invoice findOne(String id);



}
