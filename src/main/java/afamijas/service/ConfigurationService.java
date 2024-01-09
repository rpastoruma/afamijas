package afamijas.service;

import afamijas.model.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface ConfigurationService
{
    String value(String key);

    String updateValue(String key, String value, String type, Boolean front, Boolean admin);

    String addValue(String key, String value);

    void delete(String key);

    List<Configuration> getFrontConfiguration();

    List<Configuration> getAdminConfiguration();

}
