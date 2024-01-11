package afamijas.service;


import afamijas.model.Media;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@Component
public interface MediaService
{
    List<Media> findByObjectAndObjecType(String idobject, String objecttype);

    List<Media> findByObjectAndObjecTypeAndMediaType(String idobject, String objecttype, String mediatype);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    Media create(String idobject, String objecttype, String mediatype, MultipartFile file) throws Exception;

    Media create(String idobject, String objecttype, String mediatype, String filename, String url, String mimetype);

    Media create(Media media);

    Media update(Media media);

    void delete(Media media);

    String uploadFileFTP(String path, String filename, InputStream fileInputStream) throws Exception;

}
