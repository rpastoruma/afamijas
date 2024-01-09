package afamijas.queuemail.repositories;

import afamijas.queuemail.model.QueueEmailAttachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueEmailAttachmentsRepository extends MongoRepository<QueueEmailAttachment, String>
{

	@Query("{ 'idemail' : ?0 }")
	List<QueueEmailAttachment> findByEmail(String idemail);

	@Query(value="{'idemail' : ?0}", delete = true)
	public void deleteByEmail(String idemail);
}
