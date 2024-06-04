package afamijas.dao;

import afamijas.model.City;
import afamijas.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CitiesRepository extends MongoRepository<City, String>
{

	@Query("{ 'id' : ?0 }")
	City findOne(Integer id);

	@Query("{ 'state_id' : ?0 }")
	List<City> findCitiesByIdState(Integer idstate);

	@Query("{ 'postalcodes' : { $in : [ ?0 ] }, 'country_id' : ?1 }")
	List<City> findCitiesByPostalCodeAndCountryId(String postalcode, Integer idcountry);



}
