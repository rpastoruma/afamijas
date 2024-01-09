package afamijas.service;

import afamijas.dao.UsersRepository;
import afamijas.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService 
{
    final UsersRepository usersRepository;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository)
	{
		this.usersRepository = usersRepository;
	}
 
	public User findById(String id)
	{
		Optional<User> oitem = this.usersRepository.findById(id);
		if(oitem.isPresent()) return oitem.get();
		else return null;
	}

	public User findByUsername(String username)
	{
		Optional<User> oitem = this.usersRepository.findByUsername(username);
		if(oitem.isPresent()) return oitem.get();
		else return null;
	}

	public List<User> findByEmail(String email)
	{
		return this.usersRepository.findByEmail(email);
	}

	public User findByApikey(String apikey)
	{
		Optional<User> oitem = this.usersRepository.findByApikey(apikey);
		if(oitem.isPresent()) return oitem.get();
		else return null;
	}

	public User findByUsernameAndStatus(String system, String status)
	{
		Optional<User> oitem = this.usersRepository.findByUsernameAndStatus(system, status);
		if(oitem.isPresent()) return oitem.get();
		else return null;
	}
	
	public User save(User user)
	{
		user.setModified(LocalDateTime.now());
		return this.usersRepository.save(user);
	}
	
	public void delete(String id)
	{
		User user = null;
		Optional<User> val = this.usersRepository.findById(id);
		if(val.isPresent()) user = val.get();
		else return;

		user.setStatus("D");
		user.setModified(LocalDateTime.now());
		this.usersRepository.save(user);
	}


}
