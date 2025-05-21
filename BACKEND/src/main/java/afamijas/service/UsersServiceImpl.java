package afamijas.service;

import afamijas.dao.AddressBookRepository;
import afamijas.dao.UsersRepository;
import afamijas.model.AddressBook;
import afamijas.model.User;
import afamijas.model.dto.UserDTO;
import afamijas.utils.PasswordPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService 
{
    final UsersRepository usersRepository;

	final AddressBookRepository addressBookRepository;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository, AddressBookRepository addressBookRepository)
	{
		this.usersRepository = usersRepository;
        this.addressBookRepository = addressBookRepository;
    }

	@Override
	public User findOne(String id)
	{
		return this.usersRepository.findOne(id);
	}

	@Override
	public User findOne(String id, String role, String status)
	{
		User user =  this.usersRepository.findOne(id, status);
		if(role!=null && user.getRoles().contains(role)) return user;
		return null;
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

		user = this.usersRepository.save(user);

		//ACTUALIZAMOS AGENDA
		try
		{
			List<AddressBook> addressBooks = this.addressBookRepository.findByIduser(user.get_id());
			if(addressBooks!=null && addressBooks.size()>0)
				for(AddressBook addressBook:addressBooks)
				{
					addressBook.setFullname(user.getFullname());
					addressBook.setPhone(user.getPhone());
					addressBook.setEmail(user.getEmail());
					this.addressBookRepository.save(addressBook);
				}
			else
			{
				AddressBook addressBook = new AddressBook(user);
				this.addressBookRepository.save(addressBook);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		return user;
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


	@Override
	public UserDTO changePass(User user, String newpassword)
	{
		user.setPassword(new BCryptPasswordEncoder().encode(newpassword));
		user.setToken(UUID.randomUUID().toString()); //renovamos token
		user.setPassworChanged(true);
		return new UserDTO(this.save(user));
	}


	private List<User> getUsersWithExpiredGeneratedPasswords() {
		LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
		return usersRepository.findByPassworChangedFalseAndPasswordGeneratedBefore(oneDayAgo);
	}


	@Scheduled(fixedRate = 1000 * 60 * 60) //cada hora
	public void invalidNotChangedPasswords() {
		List<User> users = getUsersWithExpiredGeneratedPasswords(); // usando el nuevo método

		for (User user : users) {
			String newpassword = PasswordPolicy.generate();
			user.setPassword(new BCryptPasswordEncoder().encode(newpassword));
			user.setToken(UUID.randomUUID().toString());
			//user.setPassworChanged(false);
			//user.setPasswordGenerated(LocalDateTime.now()); // actualiza fecha si es necesario
			this.save(user);
		}
	}



}
