package core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import help.CompleteName;
import help.Lire;

public class ServerThread extends Thread {
	
	
	public static int port = 2302;
	public static boolean isPlaying = false;
	private static int taille = 2048;
	private static byte buffer[] = new byte[taille];
	private static boolean stop_serv = false;
	private static String messageError = "";
	private Socket socket;
	private boolean stop;
	private String stringFile;
	private String path;
	private static HashMap<String, ArrayList<String>> mapSong;
	public static String[] s2;
	public static String ti;
	public static String ar;
	static Lire t;
	private static String listeAtt;
	private static boolean ok;
	
	public ServerThread(Socket socketClient,HashMap<String,ArrayList<String>> mapSong, String path2) {
		this.socket = socketClient;
		this.path = path2;
		System.out.println(mapSong);
	}

	public void run() {
		try{
			String receivedData;
			System.out.println("Connexion de " + socket.getInetAddress());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream());
			while(!stop){
				while((receivedData = in.readLine()) == null){
				}
				String message;
				switch(receivedData){
				case "coucou": //Ok pour envoi liste musique
					out.println(stringFile);
					out.println("\\cfini");
					break;
				case "givemeinfo":
					message = "";
					if (isPlaying()){
						int indexSong = t.getSong().lastIndexOf("\\");
						message = "Musique en cours : " + t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")) + "\n";
					}
					else{
						message = "Aucune musique n'est jouée ! \n";
					}
					if (listeAtt!=null){
						int indexSong = listeAtt.lastIndexOf("\\");
						message += "Musique dans la liste d'attente : " + listeAtt.substring(indexSong+1,listeAtt.lastIndexOf("."));
					}
					else{
						message += "Aucune musique dans la liste d'attente !";
					}
					message = message.replaceAll("_"," ");
					out.println(message);
					break;
				default:
					if(receivedData.startsWith("vlalachanson")){ //ï¿½ complï¿½ter ^^
						String com = path+CompleteName.completeName(mapSong,receivedData.substring(12))+".mp3";
    	  				com = com.replaceAll(" ","_");
    	  				message = "";
    	  				if (!isPlaying){
    	  					ok = false;
    	  					t = new Lire(com);
    	  					while(!ok);
    	  					if (!messageError.equals("PasLaChanson")){
        	  					setPlaying(true);
        	  					out.println("Playing");
    	  					}
    	  					else{
    	  						out.println("PasLaChanson");
    	  					}
    	  				}
    	  				else{
    	  					if (listeAtt !=null){
    	  						out.println("ListeFull");
    	  					}
    	  					else{
    	  						listeAtt = com;
    	  						out.println("ListeAtt");
    	  					}
    	  				}
					}
					else{
						// Si ce n'est pas vlalachanson
					}
				}
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}






















	public static boolean isPlaying() {
		return isPlaying;
	}

	public static void main(HashMap<String, ArrayList<String>> mapSong2, String path){
		try{
			ServerThread.mapSong = mapSong2;
			System.out.println(mapSong);
			ServerSocket socketServer = new ServerSocket(port);
			System.out.println("Lancement du serveur");
			while(!stop_serv){
				Socket socketClient = socketServer.accept();
				ServerThread t = new ServerThread(socketClient, mapSong2, path);
				t.start();
			}
			socketServer.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void setOk(boolean b) {
		ok = b;
	}

	public static void setPlaying(boolean isPlaying) {
		ServerThread.isPlaying = isPlaying;
	}

	public String getMessageError() {
		return messageError;
	}

	public static void setMessageError(String messageError) {
		ServerThread.messageError = messageError;
	}
	
	@SuppressWarnings("deprecation")
	public static void pause(){
		t.suspend();
		setPlaying(false);
	}
	
	@SuppressWarnings("unchecked")
	public static void lireNext() {
		if (isPlaying){
			pause();
		}
		if (listeAtt !=null){
			t = new Lire(listeAtt);
			sendMessage();
			listeAtt = null;
			isPlaying = true;
		}
		else{
			System.out.println("Chanson tirée au hasard :D");
			Object[] tab = mapSong.values().toArray();
			int i = (int) Math.round(Math.random()*(tab.length-1));
			ArrayList<String> l = (ArrayList<String>) tab[i];
			Object[] tab2 =(l).toArray();
			i = (int) Math.round(Math.random()*(tab2.length-1));
			String k = (String) tab2[i];
			String com = "C:\\Users\\Admin\\Music\\Musique\\"+CompleteName.completeName(mapSong,k)+".mp3";
			com = com.replaceAll(" ","_");
			t = new Lire(com);
			sendMessage();
			isPlaying = true;
		}
	}
	
	private static void sendMessage() {
		int indexSong = t.getSong().lastIndexOf("\\");
		GestionServer.getWindow().displayMessage(t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")).replace("_", " "));
	}

	public static String whichPlaying(){
		if (isPlaying()){
			int indexSong = t.getSong().lastIndexOf("\\");
			return("Musique en cours : " + t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")).replace("_", " "));
		}
		else{
			return("Aucune musique n'est jouée !");
		}
	}
	
	public static String whichWaiting(){
		if (listeAtt!=null){
			int indexSong = listeAtt.lastIndexOf("\\");
			return("Musique dans la liste d'attente : " + listeAtt.substring(indexSong+1,listeAtt.lastIndexOf(".")));
		}
		else{
			return("Aucune musique dans la liste d'attente !");
		}
	}

	public static void init() {
	}

	public static void stopIt() {
		t.stop();
		System.exit(0);
		
	}
}
