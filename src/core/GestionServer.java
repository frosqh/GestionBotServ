package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.github.theholywaffle.teamspeak3.TS3Api;

import display.MainWindow;
import help.DiskFileExplorer;
import ts3Query.Ts3Query;
import webServer.Web;

public class GestionServer {
	public static Thread t;
	private static HashMap<String, ArrayList<String>> mapSong;
	private static String path;
	private static MainWindow window;
	private static String artistList;
	private static Ts3Query api;
	private static HashMap<String, Thread> client = new HashMap<String, Thread>();
	
	public static HashMap<String, Thread> getClient() {
		return client;
	}

	public static void setClient(HashMap<String, Thread> client) {
		GestionServer.client = client;
	}

	public static void main(String[] arg0) throws IOException {
		Thread t = new Thread(new Web());
		t.start();
		window = new MainWindow();
		init();
		ServerThread.main(mapSong, path);
	}
	
	private static void init() {
		System.setProperty("file.encoding","UTF-32");
		path = "C:\\Users\\Frosqh\\Music\\Musique\\";
		DiskFileExplorer d = new DiskFileExplorer(path ,false);
		String stringFile = d.list().replaceAll("_", " ");
		ServerThread.setStringFile(stringFile);
		mapSong = ListHashMap.ListToHash(ListHashMap.recupList(stringFile));
		artistList = mapSong.keySet().toString();
		artistList = artistList.substring(1,  artistList.length()-2);
		
		//System.out.println(isIn("4 Walls"));
		//Ts3Query.main(null);
	}

	public static MainWindow getWindow() {
		return window;
	}

	public static String getArtistList() {
		return artistList;
	}

	public static void setArtistList(String artistList) {
		GestionServer.artistList = artistList;
	}
	
	public static String getSong(String artist){
		artist += " ";
		String tmp = mapSong.get(artist).toString();
		tmp = tmp.substring(2, tmp.length()-1);
		return(tmp);
	}
	
	public static boolean isIn(String song){
		song = " "+song;
		Collection<ArrayList<String>> l = mapSong.values();
		
		for (ArrayList<String> l2 : l){
			if (l2.contains(song)){
				return true;
			}
		}
		return false;
	}
	
	public static String getPath(String song){
		song = " "+song;
		Set<String> set = mapSong.keySet();
		for (String key : set){
			if (mapSong.get(key).contains(song)){
				return key+"-"+song;
			}
		}
		return null;
	}

	public static void setApi(Ts3Query ts3Query) {
		api = ts3Query;
		
	}

	public static Ts3Query getApi() {
		return api;
	}

	public static boolean isArtist(String artist) {
		return mapSong.containsKey(artist);
	}

	public static String getArtistSong(String artist) {
		return mapSong.get(artist).toString();
	}
}
