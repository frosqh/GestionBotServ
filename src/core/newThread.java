package core;

import help.Lire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class newThread extends Thread{

    public static int port = 2302;
    public static boolean isPlaying = false;
    private int taille = 2048;
    private byte buffer[] = new byte[taille];
    private boolean stop_serv = false;
    private String messageError;
    private Socket socket;
    private boolean stop;
    private String stringFile;
    private String path;
    private HashMap<String, ArrayList<String>> mapSong;
    private Lire t;
    private Stack<String> listAtt;
    private boolean ok;
    private String end = "\\cfini";

    public newThread(Socket socketClient, HashMap<String,ArrayList<String>> mapSong, String path2){
        socket = socketClient;
        this.mapSong = mapSong;
    }

    public void run(){
        try {
            String receivedData;
            System.out.println("Connecting from " + socket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            while (!stop){ //mainBoucle
                while ((receivedData = in.readLine()) != null){
                    wait(500);
                    if (stop) System.exit(0);
                }
                String message;
                switch(receivedData){
                    case "coucou":
                        out.println(stringFile);
                        out.println(end);
                        break;
                    case "givemeinfo":
                        message = "";
                        message += whichPlaying() + "\n" + whichWaiting();
                        message = message.replaceAll("_"," ");
                        out.println(message);
                        break;
                    case "playPause":
                        if (isPlaying){
                            pause();
                            isPlaying = false;
                            break;
                        }
                        lireNext();
                        break;
                    case "next":
                        if (isPlaying){
                            lireNext();
                            break;
                        }
                        break;

                }
            }
        } catch (IOException e) {
            System.err.println("Impossible to get I/O Stream");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Error while waiting !");
            e.printStackTrace();
        } finally {
            System.out.println("Aïe");
        }
    }
}
