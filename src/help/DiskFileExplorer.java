package help;
import java.io.File;

/**
 * Lister le contenu d'un répertoire
 * http://www.fobec.com/java/964/lister-fichiers-dossiers-repertoire.html
 * @author fobec 2010
 */
public class DiskFileExplorer {
 
    private String initialpath = "";
    private Boolean recursivePath = false;
    public int filecount = 0;
    public int dircount = 0;
 
/**
 * Constructeur
 * @param path chemin du répertoire
 * @param subFolder analyse des sous dossiers
 */
    public DiskFileExplorer(String path, Boolean subFolder) {
        super();
        this.initialpath = path;
        this.recursivePath = subFolder;
    }
 
    public String list() {
        return this.listDirectory(this.initialpath);
    }
 
    private String listDirectory(String dir) {
    	String s ="";
    	String s2;
    	String ext = "";
        File file = new File(dir);
        File[] files = file.listFiles();
        if (files != null ) {
            for (int i = 0; i < files.length; i++) {
            	ext = "";
            	s2 = files[i].getName();
            	int j = s2.lastIndexOf('.');
            	if (j > 0) {
            	    ext = s2.substring(j+1);
            	}	
                if (ext.equals("mp3")) {
                    s+=(files[i].getName().substring(0,j) + "\n");
                    this.filecount++;
                }
                if (files[i].isDirectory() == true && this.recursivePath == true) {
                    this.listDirectory(files[i].getAbsolutePath());
                }
            }
        }
        return s;
    }    
}