package afamijas.queuemail.repositories;

import afamijas.queuemail.model.QueueEmail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QueueEmailsRepository extends MongoRepository<QueueEmail, String>
{

	List<QueueEmail> findAll();
}
