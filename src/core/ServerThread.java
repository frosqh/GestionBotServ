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
	private static String stringFile;
	private static String path;
	private static HashMap<String, ArrayList<String>> mapSong;
	public static String[] s2;
	public static String ti;
	public static String ar;
	static Lire t;
	private static String listeAtt;
	private static boolean ok;
	
	public ServerThread(Socket socketClient,HashMap<String,ArrayList<String>> mapSong, String path2) {
		this.socket = socketClient;
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
					out.println(getStringFile());
					out.println("\\cfini");
					break;
				case "givemeinfo":
					message = "";
					message = whichPlaying() +"\n"+ whichWaiting();
					message = message.replaceAll("_"," ");
					out.println(message);
					break;
				default:
					if(receivedData.startsWith("vlalachanson")){
						System.out.println(receivedData);
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
						if(receivedData.equals("playPause")){
							if(isPlaying){
								pause();
								setPlaying(false);
							}
							else{
								lireNext();
							}
						}
						else{
							if (receivedData.equals("next")){
								if(isPlaying){
									lireNext();
								}
								else{
									//Code d'erreur si rien ne joue :D
								}
							}
							else{
								//Encore autre chose :D
							}
						}
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

	public static void main(HashMap<String, ArrayList<String>> mapSong2, String path3){
		try{
			ServerThread.path = path3;
			ServerThread.mapSong = mapSong2;
			System.out.println(mapSong);
			ServerSocket socketServer = new ServerSocket(port);
			System.out.println("Lancement du serveur");
			while(!stop_serv){
				Socket socketClient = socketServer.accept();
				ServerThread t = new ServerThread(socketClient, mapSong2, path3);
				t.start();
			}
			socketServer.close();
		}
		catch (Exception e){
			e.printStackTrace();
			System.exit(0);
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
			int totalPondere = 0;
			ArrayList<String> aTraiter = null;
			Object[] tab = mapSong.values().toArray();
			for (Object l : tab){
				totalPondere += ((ArrayList<String>) l).size();
			}
			int i = (int) Math.round(Math.random()*totalPondere);
			for (Object l : tab){
				if (i-((ArrayList<String>) l).size() <= 0){
					aTraiter = (ArrayList<String>) l;
					break;
				}
				else{
					i -= ((ArrayList<String>) l).size();
				}
			}
			Object[] tab2 =(aTraiter).toArray();
			String k;
			if (i < tab2.length) {
				 k = (String) tab2[i];
			}
			else{
				 k = (String) tab2[0];
			}
			String com = path+CompleteName.completeName(mapSong,k)+".mp3";
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

	public String getStringFile() {
		return stringFile;
	}

	public static void setStringFile(String stringFile) {
		ServerThread.stringFile = stringFile;
	}
	
	public static void play(String song){
		String com = path+CompleteName.completeName(mapSong,song)+".mp3";
		com = com.replaceAll(" ","_");
		System.out.println(com);
		if (!isPlaying){
			ok = false;
			t = new Lire(com);
			while(!ok);
				if (!messageError.equals("PasLaChanson")){
					setPlaying(true);
				}
				else{
				}
			}
			else{
					if (listeAtt == null){
						listeAtt = com;
					}
			}
	}
}
