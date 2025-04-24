package afamijas.dao;

import afamijas.model.AddressBook;
import afamijas.model.Atencion;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressBookRepository extends MongoRepository<AddressBook, String>
{

	@Query("{ '_id' : ?0 }")
	AddressBook findOne(String id);

	@Query("{ 'iduser' : ?0 }")
	List<AddressBook> findByIduser(String iduser);


}
