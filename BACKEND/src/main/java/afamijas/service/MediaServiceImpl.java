package afamijas.service;


import afamijas.model.Media;
import afamijas.utils.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MediaServiceImpl implements MediaService
{

    @Value("${media.path}")
    String mediapath;

    @Value("${cdn.baseurl}")
    String CDN_BASE_URL;

    @Value("${cdn.host}")
    String FTP_HOST;

    @Value("${cdn.path}")
    String FTP_PATH;

    @Value("${cdn.user}")
    String FTP_USER;

    @Value("${cdn.pass}")
    String FTP_PASS;


    final MongoTemplate mongoTemplate;

    @Autowired
    public MediaServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Media> findByObjectAndObjecType(String idobject, String objecttype)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("A").and("idobject").is(idobject).and("objecttype").is(objecttype));


        return this.mongoTemplate.find(query, Media.class);
    }



    @Override
    public List<Media> findByObjectAndObjecTypeAndMediaType(String idobject, String objecttype, String mediatype)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("A").and("idobject").is(idobject).and("objecttype").is(objecttype).and("mediatype").is(mediatype));


        return this.mongoTemplate.find(query, Media.class);
    }



    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Media create(String idobject, String objecttype, String mediatype, MultipartFile file) throws Exception
    {
        String cdnurl = null;
        String fileName = UUID.randomUUID() + "-" + FileUtils.sanitizeFilename(file.getOriginalFilename());
        String wholePath = mediapath + File.separator + fileName;

        file.transferTo(Paths.get(wholePath));

        try { cdnurl = this.uploadFileFTP(objecttype + "_" + mediatype, fileName, new FileInputStream(wholePath) ); } catch (Exception e) { throw new Exception("ERROR: Cannot upload the file " + fileName);  }

        try { File f = new File(wholePath); f.delete(); } catch (Exception e) { e.printStackTrace(); }

        if(cdnurl!=null)
        {
            Media media = new Media();
            media.setIdobject(idobject);
            media.setObjecttype(objecttype);
            media.setMediatype(mediatype);
            media.setFilename(fileName);
            media.setUrl(cdnurl);
            media.setMimetype(file.getContentType());

            return mongoTemplate.insert(media);
        }

        return null;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Media create(String idobject, String objecttype, String mediatype, String filename, String url, String mimetype)
    {
        Media media = new Media();
        media.setIdobject(idobject);
        media.setObjecttype(objecttype);
        media.setMediatype(mediatype);
        media.setFilename(filename);
        media.setUrl(url);
        media.setMimetype(mimetype);

        return mongoTemplate.insert(media);
    }

    @Override
    public Media create(Media m)
    {
        return this.create(m.getIdobject(), m.getObjecttype(), m.getMediatype(), m.getFilename(), m.getUrl(), m.getMimetype());
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Media update(Media media)
    {
        return mongoTemplate.save(media);
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void delete(Media media)
    {
        media.setStatus("D");
        media.setModified(LocalDateTime.now());
        mongoTemplate.save(media);
    }



    @Override
    public String uploadFileFTP(String path, String filename, InputStream fileInputStream) throws Exception
    {
        FTPClient con = null;
        try
        {
            con = new FTPClient();
            con.connect(FTP_HOST);

			/*
			System.out.println("FTP_HOST:" + FTP_HOST);
			System.out.println("FTP_USER:" + FTP_USER);
			System.out.println("FTP_PASS:" + FTP_PASS);
			System.out.println("FTP_PATH:" + FTP_PATH);
			System.out.println("filename:" + filename);
			System.out.println("path:" + path);
			 */

            if (con.login(FTP_USER, FTP_PASS))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);

                if(FTP_PATH!=null && !FTP_PATH.equals(""))
                {
                    //con.makeDirectory(FTP_PATH);
                    con.changeWorkingDirectory(FTP_PATH);
					/*
					if(!con.changeWorkingDirectory(FTP_PATH))
					{
						System.out.println("No puedo cambiar a " + FTP_PATH);
						throw new Exception("NO CHANGE DIR");
					}*/
                }

                if(path!=null && !path.equals(""))
                {
                    this.ftpCreateDirectoryTree(con, path);
                    con.changeWorkingDirectory(path);
                }

                boolean result = con.storeFile(filename, fileInputStream);
                con.logout();
                con.disconnect();
                try { fileInputStream.close(); } catch (Exception e) {}
                return result?CDN_BASE_URL + FTP_PATH + path + "/" + filename:null;
            }
            else
            {
                if(con!=null) { try {  con.disconnect(); } catch(Exception ee) {} }
                throw new Exception("NO CONNECTED");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(); //TODO: Mejorar forma de log
            try { fileInputStream.close(); } catch (Exception ee) {}
            if(con!=null) { try { con.logout(); con.disconnect(); } catch(Exception ee) {} }
            throw e;
        }

    }




    private void ftpRename(String oldname, String newname )
    {
        FTPClient con = null;
        try
        {
            con = new FTPClient();
            con.connect(FTP_HOST);

            if (con.login(FTP_USER, FTP_PASS))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);

                if (FTP_PATH != null && !FTP_PATH.equals(""))
                {
                    //con.makeDirectory(FTP_PATH);
                    con.changeWorkingDirectory(FTP_PATH);
					/*
					if(!con.changeWorkingDirectory(FTP_PATH))
					{
						System.out.println("No puedo cambiar a " + FTP_PATH);
						throw new Exception("NO CHANGE DIR");
					}*/
                }
            }

            con.rename(oldname, newname);
            con.logout();
            con.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace(); //TODO: Mejorar forma de log
            if(con!=null) { try { con.logout(); con.disconnect(); } catch(Exception ee) {} }
            return;
        }
    }

    private static void ftpCreateDirectoryTree( FTPClient client, String dirTree ) throws IOException
    {
        boolean dirExists = true;

        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] directories = dirTree.split("/");
        for (String dir : directories ) {
            if (!dir.isEmpty() ) {
                if (dirExists) {
                    dirExists = client.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!client.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
                    }
                    if (!client.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
                    }
                }
            }
        }
    }



}
