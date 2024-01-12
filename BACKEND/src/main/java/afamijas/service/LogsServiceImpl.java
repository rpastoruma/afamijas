package afamijas.service;

import afamijas.dao.LogsRepository;
import afamijas.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogsServiceImpl implements LogsService
{
    final LogsRepository logsRepository;

	@Autowired
	public LogsServiceImpl(LogsRepository logsRepository)
	{
		this.logsRepository = logsRepository;
	}

	public List<Log> findByUsername(String username)
	{
		return this.logsRepository.findByUsername(username);
	}

	public void save(String actiontype, String objecttype, String idobject, String iduser, String comments, String ip)
	{
		this.logsRepository.save(new Log(actiontype, objecttype, idobject, iduser, LocalDateTime.now(), comments, ip));
	}

}
