package afamijas.dao;

import afamijas.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PermissionsRepository extends MongoRepository<Permission, String>
{
	//RELATIVES:

	@Query("{ '_id' : ?0 }")
	Permission findOne(String id);

	@Query("{ '_id' : ?0, 'status' : ?1 }")
	Permission findOne(String id, String status);

	@Query("{ 'idrelative' : ?0, 'status' : 'P' }")
	List<Permission> findPendingPermissionsByRelative(String idrelative);



}
