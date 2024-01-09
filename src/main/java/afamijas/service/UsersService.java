package afamijas.service;

import afamijas.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface UsersService 
{
	User findById(String id);

	User findByUsername(String username);

	List<User> findByEmail(String email);

	User findByApikey(String apikey);

	User findByUsernameAndStatus(String username, String status);
	
	User save(User user);

	void delete(String id);


}
