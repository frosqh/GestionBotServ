package help;

import com.github.axet.vget.AppManagedDownload;
import core.GestionServer;
import org.eclipse.jetty.deploy.App;

public class DownloadThread implements Runnable{
    private String videoId;
    private String directory;

    public DownloadThread(String video, String dir){
        videoId = video;
        directory = dir;
    }


    @Override
    public void run() {
        GestionServer.setDownloading(true);
        AppManagedDownload.main(new String[] {videoId,directory});
        while (AppManagedDownload.getFileNumber() == 0){};
        if (AppManagedDownload.getFileNumber() == 2){
            System.exit(0);
        }
    }
}
