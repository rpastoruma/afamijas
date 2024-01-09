package afamijas.utils;

import java.io.Serializable;

public class Attachment implements Serializable
{

    private static final long serialVersionUID = 5809708011666583806L;

    private String wholepath = null;

    private String filename = null;

    private String contenttype = null;

    public Attachment(String wholepath, String filename, String contenttype)
    {
        this.wholepath = wholepath;
        this.filename = filename;
        this.contenttype = contenttype;
    }

    //nombre completo del fichero
    public String getWholePath()
    {
        return this.wholepath;
    }

    //nombre del adjunto
    public String getFileName()
    {
        return this.filename;
    }

    //contenttype
    public String getContenttype()
    {
        return this.contenttype;
    }




}