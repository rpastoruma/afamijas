package afamijas.dao;

import afamijas.model.City;
import afamijas.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StatesRepository extends MongoRepository<State, String>
{

	@Query("{ 'id' : ?0 }")
	State findOne(Integer id);

	@Query("{ 'country_id' : ?0 }")
	List<State> findStatesByIdcountry(Integer idcountry);

	@Query("{ 'country_id' : ?0 , 'prefix_postalcode' :  ?1 }")
	State findStateByCountryIdAndPrefixPostalCode(Integer idcountry, String prefixpostalcode);
	
}
