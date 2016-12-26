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
	static int taille = 2048;
	static byte buffer[] = new byte[taille];
	static boolean stop_serv = false;
	public Thread t1;
	private String messageError = "";
	private Socket socket;
	private boolean stop;
	private String stringFile;
	private String path;
	private HashMap<String, ArrayList<String>> mapSong;
	public static String[] s2;
	public static String ti;
	public static String ar;
	public static InetAddress ip;
	public static DatagramPacket paquet;
	static Lire t;
	private static String listeAtt;
	private static boolean ok;
	
	public ServerThread(Socket socketClient,HashMap<String,ArrayList<String>> mapSong, String path2) {
		this.socket = socketClient;
		this.mapSong = mapSong;
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
					if(receivedData.startsWith("vlalachanson")){ //À compléter ^^
						String com = path+CompleteName.completeName(mapSong,receivedData.substring(12))+".mp3";
    	  				com = com.replaceAll(" ","_");
    	  				message = "";
    	  				if (!isPlaying){
    	  					ok = false;
    	  					t = new Lire(com,this);
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






















	private static boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(HashMap<String, ArrayList<String>> mapSong2, String path){
		try{
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

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public static void lireNext() {
	}
	
	public static String whichPlaying(){
		if (isPlaying()){
			int indexSong = t.getSong().lastIndexOf("\\");
			return("Musique en cours : " + t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")));
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
}
