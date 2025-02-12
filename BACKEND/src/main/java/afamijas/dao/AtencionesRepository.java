package afamijas.dao;

import afamijas.model.Atencion;
import afamijas.model.Invoice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AtencionesRepository extends MongoRepository<Atencion, String>
{

	@Query("{ '_id' : ?0 }")
	Atencion findOne(String id);



	// Agregación para encontrar el mayor num_contrato
	@Aggregation(pipeline = {
			"{ $sort: { number: -1 } }",  // Ordenamos de mayor a menor
			"{ $limit: 1 }",                    // Limitar a 1 resultado
			"{ $project: { _id: 0, number: 1 } }"  // Proyectamos solo el campo num_contrato
	})
	String findHighestNumber();

}
