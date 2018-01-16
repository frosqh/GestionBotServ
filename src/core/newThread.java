package core;

import help.CompleteName;
import help.Lire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class newThread extends Thread{

    public static int port = 2302;
    public static boolean isPlaying = false;
    private int taille = 2048;
    private byte buffer[] = new byte[taille];
    private static boolean stopServ = false;
    private static String messageError = "";
    private static int stackSize = 5;
    private Socket socket;
    private boolean stop = false;
    private static String stringFile;
    private static String path;
    private static HashMap<String, ArrayList<String>> mapSong;
    private static Lire t;
    private static Stack<String> listAtt = new Stack<String>();
    private static boolean ok;
    private String end = "\\cfini";

    public newThread(Socket socketClient){
        socket = socketClient;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public static void setPlaying(boolean playing) {
        newThread.isPlaying = playing;
    }

    public void run(){
        try {
            String receivedData;
            System.out.println("Connecting from " + socket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            while (!stop){ //mainBoucle
                while ((receivedData = in.readLine()) == null){
                    this.wait(500);
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
                    default:
                        if (receivedData.startsWith("vlalachanson")){
                            String com = path+ CompleteName.completeName(mapSong,receivedData.substring(12))+".mp3";
                            com = com.replaceAll(" ","_");
                            message = "";
                            if (!isPlaying){
                                ok = false;
                                t = new Lire(com);
                                while (!ok){
                                    System.err.print(ok);
                                };
                                if (!messageError.equals("PasLaChanson")){
                                    isPlaying = true;
                                    out.println("Playing");
                                    System.out.println("Playing !");
                                    out.flush();
                                    break;
                                }
                                out.println("PasLaChanson");
                                break;
                            }
                            if (listAtt.size() >= stackSize){
                                out.println("ListeFull");
                                break;
                            }
                            out.println(listAtt);
                            break;
                        }
                        if (receivedData.startsWith("dlYoutube")){
                            String url = receivedData.substring("dlYoutube".length());
                        }
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible to get I/O Stream");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Error while waiting !");
            e.printStackTrace();
        }
    }


    public static void main(HashMap<String, ArrayList<String>> mapSong2, String path3){
        mapSong = mapSong2;
        path = path3;
        try {
            ServerSocket socketServer = new ServerSocket(port);
            while (!stopServ){
                Socket socketClient = socketServer.accept();
                if (GestionServer.getClient().containsKey(socketClient.getInetAddress().toString())){
                    GestionServer.getClient().get(socketClient.getInetAddress().toString()).interrupt();
                    GestionServer.getClient().remove(socketClient.getInetAddress().toString());
                    System.out.println("Thread " + GestionServer.getClient().get(socketClient.getInetAddress().toString()) + " has been stopped !");
                }

                newThread st = new newThread(socketClient);
                GestionServer.getClient().put(socketClient.getInetAddress().toString(),st);
                st.start();
            }
            socketServer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void pause(){
        t.suspend();
        isPlaying = false;
    }

    public static void lireNext(){
        if (isPlaying) pause();
        if (listAtt.size() > stackSize){
            t = new Lire(listAtt.pop());
            sendMessage();
            isPlaying = true;
            return;
        }
        System.out.println("Randomed song !");
        int totalPondere = 0;
        ArrayList<String>  toProcess = null;
        Object[] tab = mapSong.values().toArray();
        for (Object artist : tab){
            totalPondere += ((ArrayList<String> ) artist).size();
        }
        int randomedIndex = (int) Math.round(Math.random()*totalPondere);
        for (Object artist : tab){
            if (randomedIndex - ((ArrayList<String> ) artist).size() <= 0){
                toProcess = (ArrayList<String>) artist;
                break;
            }
            randomedIndex  -= ((ArrayList<String>) artist).size();
        }

        if (toProcess == null){
            System.err.println("WUTDEFUCK");
        }

        Object[] tab2 = toProcess.toArray();
        String k;
        if (randomedIndex > tab2.length){
            randomedIndex = 0;
        }
        k = (String) tab2[randomedIndex];
        String com = path + CompleteName.completeName(mapSong,k)+".mp3";
        com = com.replaceAll(" ","_");
        t = new Lire(com);
        sendMessage();
        isPlaying = true;
    }

    private static void sendMessage(){
        int indexSong = t.getSong().lastIndexOf("\\");
        GestionServer.getWindow().displayMessage(t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")).replace("_", " "));
        //GestionServer.getApi().sendSelectSong(t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")).replace("_", " "));
    }

    public static String whichPlaying(){
        if (isPlaying){
            int indexSong = t.getSong().lastIndexOf("\\");
            return("Musique en cours : " + t.getSong().substring(indexSong+1,t.getSong().lastIndexOf(".")).replace("_", " "));
        }
        return("Aucune musique n'est jouée !");
    }

    public static String whichWaiting(){
        if (listAtt.size()>0){
            String listAttTop = listAtt.peek();
            int indexSong = listAttTop.lastIndexOf("\\");
            return("Musique dans la liste d'attente : " + listAttTop.substring(indexSong+1,listAttTop.lastIndexOf(".")));
        }
        return("Aucune musique dans la liste d'attente !");
    }

    public static void setStringFile(String stringFile) {
        System.out.println(stringFile);
        newThread.stringFile = stringFile;
    }

    public static void stopIt() {
            t.interrupt();
            System.exit(0);
    }

    public static void setMessageError(String messageError) {
        newThread.messageError = messageError;
    }

    public static void setOk(boolean ok1) {
        ok = ok1;
    }
}
