package afamijas.dao;

import afamijas.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MenusRepository extends MongoRepository<Menu, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	Menu findOne(String id);

	@Query("{ '_id' : ?0, 'status' : ?1 }")
	Menu findOne(String id, String status);


}
