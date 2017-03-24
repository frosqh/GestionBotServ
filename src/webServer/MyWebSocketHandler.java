package webServer;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import core.GestionServer;
import core.ServerThread;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class MyWebSocketHandler {

    private String artist;
	private String song;

	@OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        session.getRemote().sendString("artist" + GestionServer.getArtistList());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Message: " + message);
        if (message.equals("whichPlaying")){
        	session.getRemote().sendString(ServerThread.whichPlaying());
        }
        else {
        	if (message.equals("whichWaiting")){
        		session.getRemote().sendString(ServerThread.whichWaiting());
        	}
        	else{
        		if (message.equals("playPause")){
        			if (ServerThread.isPlaying()){
        				ServerThread.pause();
        			}
        			else{
        				ServerThread.lireNext();
        			}
        		}
        		else{
        			if (message.equals("Next")){
        				if (ServerThread.isPlaying()){
            				ServerThread.lireNext();	
        				}
        			}
        			else{
        				if (message.startsWith("artist")){
        					message=message.substring("artist".length());
        			        session.getRemote().sendString("song" + GestionServer.getSong(message));
        			        artist = message;
        				}
        				else {
        					if (message.startsWith("song")){
        						message = message.substring(4);
        						song = message;
        						System.out.println("Artist : " + artist);
        						System.out.println("Song : " + song);
        						ServerThread.play(" "+song);
        					}
        					else{
        						if (message.equals("giveMeArtist")){
        					        session.getRemote().sendString("artist" + GestionServer.getArtistList());
        						}
        					}
        				}
        			}
        		}
        	}
        }
    }
}
