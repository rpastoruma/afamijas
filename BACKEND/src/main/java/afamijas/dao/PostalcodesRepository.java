package afamijas.dao;

import afamijas.model.City;
import afamijas.model.PostalCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostalcodesRepository extends MongoRepository<PostalCode, String>
{
	List<PostalCode> findAll();


	@Query("{ 'id' : ?0 }")
	PostalCode findOne(Long id);

	@Query("{ 'municipio_nombre' : ?0 }")
	List<PostalCode> findPostalCodeByCityName(String municipio_nombre);

	
}
