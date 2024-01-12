package afamijas.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils
{

    /**
     * Devuelve un nombre de fichero  único dado un prefijo y un sufijo
     * para dicho nombre del fichero.
     * @param prefix -- Prefijo del nombre del fichero (pudiendo incluir el path).
     * @param suffix -- Sufijo del nombre del fichero.
     *
     * uniqueFileName("/var/log/logprograma", ".log") --> "/var/log/logprograma1.log"
     *
     * @return -- Nombre �nico del fichero.
     */
    public static String uniqueFileName(String prefix, String suffix)
    {
        String filename = "";
        File file = null;

        for(long i=1; true ; i++)
        {
            filename = prefix + (new Long(i)).toString() + suffix;
            try
            {
                file = new File(filename);
                if(!file.exists())
                    break;
            }
            catch(Exception e)
            {
                break;
            }
        }
        return filename;
    }


    public static void string2File(String str, String filename, String encoding)
    {
        string2File(str, filename, encoding, false);
    }


    public static void string2File(String str, String filename, String encoding, boolean append)
    {
        OutputStreamWriter osw =null;
        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(new File(filename), append);
            osw = new OutputStreamWriter(fos, encoding);
            osw.write(str);
            osw.close();
            fos.close();
        }
        catch(Exception e)
        {
            try{osw.close(); fos.close();} catch(Exception ee){ ee.printStackTrace(); };
        }
    }



   /* SE SUPONE QUE ES M�S R�PIDO PERO NO PUEDES PONERLE ENCODING
   public static void string2File(String str, String filename, String encoding, boolean append)
   {
     FileWriter fw = null;

     try
     {
       fw = new FileWriter(new File(filename), append);
       fw.write(str);
       try { fw.flush(); } catch(Exception ee) { }
       try { fw.close(); } catch(Exception ee) { }
     }
     catch(Exception e)
     {
       try { fw.flush(); } catch(Exception ee) { }
       try { fw.close(); } catch(Exception ee) { }
       e.printStackTrace();
     }
   }
   */


    public static void string2File(String str, String filename)
    {
        string2File(str, filename, "UTF-8");
    }

   /*
   public static String file2String(String filename, String encoding)
   {
     String str = "";
     InputStreamReader isr =null;
     FileInputStream fis = null;

     try
     {
       fis = new FileInputStream(new File(filename));
       isr = new InputStreamReader(fis, encoding);
       while(isr.ready())
       {
    	 str += (char)isr.read();
       }
       isr.close();
       fis.close();
     }
     catch(Exception e)
     {
       try{isr.close(); fis.close();} catch(Exception ee){};
     }
     return str;
   }
  */


    public static String file2String(String filename)
    {
        return file2String(filename, "UTF-8");
    }

    public static String file2String(String filename, String encoding)
    {
        String str = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try
        {
            fis = new FileInputStream(filename);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null)
                str += line;

            try { br.close(); } catch(Exception ee) {};
            try { isr.close(); } catch(Exception ee) {};
            try { fis.close(); } catch(Exception ee) {};
            return str;
        }
        catch (Exception e)
        {
            try { br.close(); } catch(Exception ee) {};
            try { isr.close(); } catch(Exception ee) {};
            try { fis.close(); } catch(Exception ee) {};
            e.printStackTrace();
            return str;
        }


    }

    public static boolean unZipIt(String srcFile, String destDirectory)
    {
        int BUFFER_SIZE = 1024;
        FileInputStream fis = null;
        ZipInputStream zis = null;
        FileOutputStream fos = null;
        BufferedOutputStream dest = null;
        try
        {
            File sourceFile = new File(srcFile);

            File destinationDirectory = new File(destDirectory);
            try { destinationDirectory.mkdirs(); } catch(Exception e) {}

            if(!sourceFile.exists() || !destinationDirectory.exists()) throw new Exception("Invalid file or folder");

            //now start with unzip process


            fis = new FileInputStream(sourceFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));

            ZipEntry entry = null;

            while((entry = zis.getNextEntry()) != null)
            {
                String outputFilename = destDirectory + File.separator + entry.getName();

                // Prevent "Zip Slip" vulnerability https://snyk.io/research/zip-slip-vulnerability
                String canonicalDestinationDirPath = destinationDirectory.getCanonicalPath();
                File destinationfile = new File(destDirectory, entry.getName());
                String canonicalDestinationFile = destinationfile.getCanonicalPath();
                if (!canonicalDestinationFile.startsWith(canonicalDestinationDirPath + File.separator))
                    throw new Exception("Entry is outside of the target dir: " + entry.getName());

                createDirIfNeeded(destDirectory, entry);
                if(new File(destDirectory, entry.getName()).isDirectory()) continue;

                int count;

                byte data[] = new byte[BUFFER_SIZE];

                //write the file to the disk
                fos = new FileOutputStream(outputFilename);
                dest = new BufferedOutputStream(fos, BUFFER_SIZE);

                while((count = zis.read(data, 0, BUFFER_SIZE)) != -1)
                {
                    dest.write(data, 0, count);
                }

                //close the output streams
                dest.flush();
                dest.close();
                fos.flush();
                fos.close();
            }

            //we are done with all the files
            //close the zip file
            zis.close();
            fis.close();

            return true;
        }
        catch(Exception e)
        {
            try { dest.close(); } catch(Exception ee) {}
            try { fos.close(); } catch(Exception ee) {}
            try { zis.close(); } catch(Exception ee) {}
            try { fis.close(); } catch(Exception ee) {}
            e.printStackTrace();
            return false;
        }
    }



    private static boolean createDirIfNeeded(String destDirectory, ZipEntry entry)
    {
        String name = entry.getName();

        if(name.contains("/"))
        {
            int index = name.lastIndexOf("/");
            String dirSequence = name.substring(0, index);

            File newDirs = new File(destDirectory + File.separator + dirSequence);

            //create the directory
            newDirs.mkdirs();

            return true;
        }

        return false;
    }

    public static String sanitizeFilename(String inputName) {
        return inputName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }


   /*

	public static void main(String args[])
	{
		System.out.println(file2String("c:\\kk.txt", "ISO-8859-1"));
	}

	*/

}
