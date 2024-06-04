package afamijas.dao;

import afamijas.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CountriesRepository extends MongoRepository<Country, String>
{

	@Query("{ 'id' : ?0 }")
	Country findOne(Integer id);


	List<Country> findAll();


	
}
