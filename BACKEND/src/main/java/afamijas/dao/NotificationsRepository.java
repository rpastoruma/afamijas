package afamijas.dao;


import afamijas.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface NotificationsRepository extends MongoRepository<Notification, String>
{

    @Query("{ '_id' : ?0 }")
    Notification findOne(String id);



    @Query("{ 'iduser' : ?0 }")
    List<Notification> findByUser(String iduser);




}
