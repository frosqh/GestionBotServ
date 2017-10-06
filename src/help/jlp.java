package help;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import core.GestionServer;
import core.ServerThread;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;
 
public class jlp implements Runnable
{
    private String fFilename = null;
    private boolean remote = false;
 
    static public jlp createInstance(String[] args)
    {
        jlp player = new jlp();
        if (!player.parseArgs(args))
                player = null;
        return player;
    }
 
    public jlp() {}
 
    public jlp(String filename)
    {
        init(filename);
    }
 
    public void init(String filename)
    {
        fFilename = filename;
    }
 
    protected boolean parseArgs(String[] args)
    {
        boolean parsed = false;
        if (args.length == 1)
        {
                init(args[0]);
                parsed = true;
                remote = false;
        }
        else if (args.length == 2)
        {
                if (!(args[0].equals("-url")))
                {
                        showUsage();
                }
                else
                {
                        init(args[1]);
                        parsed = true;
                        remote = true;
                }
        }
        else
        {
                showUsage();
        }
        return parsed;
    }
 
    public void showUsage()
    {
        System.out.println("Usage: jlp [-url] <filename>");
        System.out.println("");
        System.out.println(" e.g. : java javazoom.jl.player.jlp localfile.mp3");
 
    }
    public void stop() throws JavaLayerException
    {
        try
        {
                InputStream in = null;
                if (remote == true)
                        try {
                                in = getURLInputStream();
                        } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                else in = getInputStream();
                AudioDevice dev = getAudioDevice();
                Player player = new Player(in, dev);
                player.close();
        }
        catch (IOException ex)  {}
 
    }
 
    public void play()
            throws Exception
    {
            try
            {
                System.out.println("playing "+fFilename+"...");
                InputStream in = null;
                if (remote == true) in = getURLInputStream();
                else in = getInputStream();
                ServerThread.setOk(true);
                System.out.println("On arrive � le lire !");
                AudioDevice dev = getAudioDevice();
                Player player = new Player(in, dev);
                int indexSong = fFilename.lastIndexOf("\\");
                System.out.println(fFilename.lastIndexOf("."));
                //GestionServer.getApi().sendSelectSong(fFilename.substring(indexSong+1,fFilename.lastIndexOf(".")).replace("_", " "));
                player.play();
            }
            catch (Exception ex)
            {
                    throw ex;
            }
    }
 
    /**
     * Jouer fichier de l' URL (Streaming).
     */
    protected InputStream getURLInputStream() throws Exception
    {
 
            URL url = new URL(fFilename);
            InputStream fin = url.openStream();
            BufferedInputStream bin = new BufferedInputStream(fin);
            return bin;
    }
 
    /**
     * Jouer un fichier de FileInputStream.
     */
    protected InputStream getInputStream() throws IOException
    {
            FileInputStream fin = new FileInputStream(fFilename);
            BufferedInputStream bin = new BufferedInputStream(fin);
            return bin;
    }
 
    protected AudioDevice getAudioDevice() throws JavaLayerException
    {
            return FactoryRegistry.systemRegistry().createAudioDevice();
    }
 
    public void run() {
        while(true)
        {
            try {
                this.play();
            }catch (Exception e) {e.printStackTrace(); break;}
        }
    }
}