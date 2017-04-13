package ts3Query;

import java.util.ArrayList;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import core.GestionServer;
import core.ServerThread;

public class Ts3Query {
	private ArrayList<String> commands = new ArrayList<String>();	
	private final TS3Query query;
	private final TS3Api api;
	final int clientId;
	//private final ScheduledExecutorService executor;

	public static void main(String[] args){
		new Ts3Query();
	}
	
	public Ts3Query(){
		commands.add("request");
		commands.add("next");
		commands.add("playPause");
		commands.add("getSong");
		commands.add("help");
		
		
		final TS3Config config = new TS3Config();
		config.setHost("localhost");
		config.setDebugLevel(Level.ALL);
		
		query = new TS3Query(config);
		query.connect();
		
		api = query.getApi();
		api.login("paikea2", "m88wFuk1");
		api.selectVirtualServerById(1);
		api.setNickname("Paï");
		moveTo(api.getClientByNameExact("frosqh",true));
		api.sendChannelMessage("Paikea is online ! *-*");
		clientId = api.whoAmI().getId();
		api.registerEvent(TS3EventType.TEXT_CHANNEL, 0);
		
		api.addTS3Listeners(new TS3EventAdapter() {
			public void onTextMessage(TextMessageEvent e){
				if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
					String message = e.getMessage();
					System.out.println(message);
					if (message.startsWith("!")){
						message = message.substring(1);
						System.out.println(message);
						if (message.startsWith("request ")){
							message = message.substring(8);
							if (GestionServer.isIn(message)){
								//String path = GestionServer.getPath(message);
								//System.out.println(path);
								ServerThread.play(" "+message);
							}
							else{
								api.sendPrivateMessage(e.getInvokerId(), message+" is not a downloaded song");
							}
						}
						else{
							if (message.startsWith("next")){
								if (message.equals("next")){
									ServerThread.lireNext();
								}
								else{
									api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not take parameters");
								}
							}
							else{
								if (message.startsWith("getSong")){
									if (message.equals("getSong")){
										api.sendPrivateMessage(e.getInvokerId(),ServerThread.whichPlaying());
									}
									else{
										api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not take parameters");
									}
								}
								else{
									if (message.startsWith("play") || message.startsWith("pause")){
										if (message.equals("play") || message.equals("pause") || message.equals("playPause")){
												if (ServerThread.isPlaying()){
													ServerThread.pause();
												}
												else{
													ServerThread.lireNext();
												}
										}
										else{
											api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not take parameters");
										}
									}
									else{
										if (message.startsWith("help")){
											if (message.equals("help")){
												api.sendPrivateMessage(e.getInvokerId(), "These commands are available : "+commands.toString());
											}
											else{
												if (message.startsWith("help ")){
													message = message.substring(5);
													switch(message){
													case "request":
														api.sendPrivateMessage(e.getInvokerId(),"request : Allow you to play a song or to add it to the waiting queue");
														break;
													case "next":
														api.sendPrivateMessage(e.getInvokerId(),"next : Allow you to pass a song if one is playing");
														break;
													case "playPause":
														api.sendPrivateMessage(e.getInvokerId(),"playPause : Allow you to pause if playing or play if not");
														break;
													case "help":
														api.sendPrivateMessage(e.getInvokerId(),"help : Show help informations");
														break;
													case "getSong":
														api.sendPrivateMessage(e.getInvokerId(),"getSong : Print current song");
														break;
													default:
														api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not exist.\nType !help for further informations.");
														break;
													}
												}
												
											}
										}
										else{
											message = message + " ";
											api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not exist.\nType !help for further informations.");
										}
									}
								}
							}
						}
					}
				}
			}
		});
		}

	public void sendSelectSong(String song, String user){
		api.sendChannelMessage("[b]Playing [color=blue]"+song+"[color=black] requested by [color=red]"+user);
	}
	
	public void moveTo(Client user){
		api.moveQuery(user.getChannelId());
	}
}
