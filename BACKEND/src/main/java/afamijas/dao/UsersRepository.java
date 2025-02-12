package afamijas.dao;

import afamijas.model.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface UsersRepository extends MongoRepository<User, String> 
{

	@Query("{ '_id' : ?0 }")
	User findOne(String id);


	@Query("{ '_id' : ?0, 'status' : ?1 }")
	User findOne(String id, String status);

	@Query("{ 'username' : ?0 }")
	User findUserByUsername(String username);

	@Query("{ 'username' : ?0, 'status' : ?1 }")
	User findUserByUsername(String username, String status);

	@Query("{ 'email' : ?0 }")
	List<User> findUserByEmail(String email);

	@Query("{ 'email' : ?0, 'status' : ?1  }")
	List<User> findUserByEmail(String email, String status);

	@Query("{ 'name' : ?0, 'status' : ?1  }")
	List<User> findUserByName(String name, String status);

	@Query("{ 'surname1' : ?0, 'status' : ?1  }")
	List<User> findUserBySurname1(String surname1, String status);

	@Query("{ 'surname2' : ?0, 'status' : ?1  }")
	List<User> findUserBySurname2(String surname2, String status);

	@Query("{ 'documentid' : ?0, 'status' : ?1  }")
	List<User> findUserByDNI(String documentid, String status);

	@Query("{ 'roles' : { $in : ?0 }, 'status' : ?1 }")
	List<User> findUsersByRoleAndStatus(List<String> roles, String status);


	@Query("{ 'status' : ?0 }")
	List<User> findUsersByStatus(String status);

	// Agregación para encontrar el mayor num_contrato
	@Aggregation(pipeline = {
			"{ $sort: { num_contrato: -1 } }",  // Ordenamos de mayor a menor
			"{ $limit: 1 }",                    // Limitar a 1 resultado
			"{ $project: { _id: 0, num_contrato: 1 } }"  // Proyectamos solo el campo num_contrato
	})
	String findHighestNumContrato();

	// Agregación para encontrar el mayor fs_num_expediente
	@Aggregation(pipeline = {
			"{ $sort: { fs_num_expediente: -1 } }",  // Ordenamos de mayor a menor
			"{ $limit: 1 }",                        // Limitar a 1 resultado
			"{ $project: { _id: 0, fs_num_expediente: 1 } }"  // Proyectamos solo el campo fs_num_expediente
	})
	String findHighestFsNumExpediente();
}
