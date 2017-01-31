package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import display.MainWindow;
import help.DiskFileExplorer;

public class GestionServer {
	public static Thread t;
	private static HashMap<String, ArrayList<String>> mapSong;
	private static String path;
	private static MainWindow window;
	
	public static void main(String[] arg0) throws IOException {
		window = new MainWindow();
		init();
		ServerThread.main(mapSong, path);
	}
	
	private static void init() {
		System.setProperty("file.encoding","UTF-32");
		path = "C:\\Users\\Admin\\Music\\Musique\\";
		//path = "/home/projecteur/Musique/Divers/Divers";
		DiskFileExplorer d = new DiskFileExplorer(path ,false);
		String stringFile = d.list().replaceAll("_", " ");
		mapSong = ListHashMap.ListToHash(ListHashMap.recupList(stringFile));
		
	}

	public static MainWindow getWindow() {
		return window;
	}
}
