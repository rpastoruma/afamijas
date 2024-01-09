package afamijas.service;

import afamijas.model.Log;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface LogsService
{
	List<Log> findByUsername(String username);

	void save(String actiontype, String objecttype, String idobject, String iduser, String comments, String ip);

}

