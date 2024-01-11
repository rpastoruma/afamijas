package afamijas.service;

import afamijas.dao.UsersRepository;
import afamijas.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService 
{
    final UsersRepository usersRepository;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository)
	{
		this.usersRepository = usersRepository;
	}

	@Override
	public User findOne(String id)
	{
		return this.usersRepository.findOne(id);
	}

	@Override
	public User findOne(String id, String status)
	{
		return this.usersRepository.findOne(id, status);
	}

	@Override
	public User findByUsername(String username)
	{
		return this.usersRepository.findUserByUsername(username);
	}

	@Override
	public User findByUsername(String username, String status)
	{
		return this.usersRepository.findUserByUsername(username, status);
	}

	@Override
	public List<User> findByEmail(String email)
	{
		return this.usersRepository.findUserByEmail(email);
	}

	@Override
	public List<User> findByEmail(String email, String status)
	{
		return this.usersRepository.findUserByEmail(email, status);
	}

	@Override
	public User save(User user)
	{
		user.setModified(LocalDateTime.now());
		return this.usersRepository.save(user);
	}

	@Override
	public void delete(String id)
	{
		User user = this.usersRepository.findOne(id);
		if(user==null) return;

		user.setStatus("D");
		user.setModified(LocalDateTime.now());
		this.usersRepository.save(user);
	}


}
