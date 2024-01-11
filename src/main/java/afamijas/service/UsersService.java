package afamijas.service;

import afamijas.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface UsersService 
{
	User findOne(String id);

	User findOne(String id, String role, String status);

	User findByUsername(String username);

	User findByUsername(String username, String status);

	List<User> findByEmail(String email);

	List<User> findByEmail(String email, String status);


	User save(User user);

	void delete(String id);


}
