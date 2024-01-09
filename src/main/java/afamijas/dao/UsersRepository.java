package afamijas.dao;

import afamijas.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends MongoRepository<User, String> 
{

	@Query("{ '_id' : ?0 }")
	Optional<User> findById(String id);

	@Query("{ 'username' : ?0 }")
	Optional<User> findByUsername(String username);

	@Query("{ 'email' : ?0 }")
	List<User> findByEmail(String email);

	@Query("{ 'apikey' : ?0, 'status' : 'A' }")
	Optional<User> findByApikey(String apikey);


	@Query("{ 'username' : ?0, 'status' : ?1 }")
	Optional<User> findByUsernameAndStatus(String username, String status);

	
}
