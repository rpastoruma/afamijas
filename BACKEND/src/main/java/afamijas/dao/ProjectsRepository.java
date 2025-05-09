package afamijas.dao;

import afamijas.model.Invoice;
import afamijas.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectsRepository extends MongoRepository<Project, String>
{

	@Query("{ '_id' : ?0 }")
	Project findOne(String id);



}
