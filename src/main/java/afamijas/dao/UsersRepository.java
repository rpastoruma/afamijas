package afamijas.dao;

import afamijas.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UsersRepository extends MongoRepository<User, String> 
{

	@Query("{ '_id' : ?0 }")
	User findOne(String id);

	@Query("{ '_id' : ?0, 'role' : ?1, 'status' : ?2 }")
	User findOne(String id, String role, String status);

	@Query("{ 'username' : ?0 }")
	User findUserByUsername(String username);

	@Query("{ 'username' : ?0, 'status' : ?1 }")
	User findUserByUsername(String username, String status);

	@Query("{ 'email' : ?0 }")
	List<User> findUserByEmail(String email);

	@Query("{ 'email' : ?0, 'status' : ?1  }")
	List<User> findUserByEmail(String email, String status);



}
