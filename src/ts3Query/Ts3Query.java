package ts3Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
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
		commands.add("getArtist");
		commands.add("list");
		commands.add("help");
		commands.add("dlClient");
		
		
		final TS3Config config = new TS3Config();
		config.setHost("harinman.ddns.net");
		config.setDebugLevel(Level.ALL);
		
		query = new TS3Query(config);
		query.connect();
		
		api = query.getApi();
		GestionServer.setApi(this);
		api.login("paikea2", "YfIZ2XYc");
		api.selectVirtualServerById(1);
		api.setNickname("Paï");
		moveTo(api.getClientByNameExact("Frosqh",true));
		api.sendChannelMessage("Paikea is online ! *-*");
		clientId = api.whoAmI().getId();
		api.registerEvent(TS3EventType.CHANNEL);
		api.registerEvent(TS3EventType.TEXT_CHANNEL);
		api.registerEvent(TS3EventType.TEXT_PRIVATE);
		
		api.addTS3Listeners(new TS3EventAdapter() {
			public void onTextMessage(TextMessageEvent e){
				if (e.getTargetMode() == TextMessageTargetMode.CLIENT){
					String message = e.getMessage();
					if (message.startsWith("troll ")){
						message = message.substring(6);
						Client trollClient = api.getClientByNameExact(message, true);
						if (ServerThread.isPlaying){
							for (int i = 0; i<5; i++){
								api.sendPrivateMessage(trollClient.getId(),"[b]Playing [color=blue]"+ServerThread.songPlaying()+"[color=black] ¸¸.•*¨*•♫♪" );
							}
						}
						api.kickClientFromServer(trollClient.getId());
					}
				}
				if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
					String message = e.getMessage();
					//System.out.println(message);
					if (message.equals("bot:hello")){
						api.sendChannelMessage("PAUL, SALOW");
					}
					
					if (message.startsWith("!")){
						message = message.substring(1);
						//System.out.println(message);
						if (message.startsWith("request ")){
							//api.kickClientFromChannel(api.getClientByNameExact("Rheoklash", true));
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
													case "getArtist":
														api.sendPrivateMessage(e.getInvokerId(),  "getArtist : Print songs of a specified artist");
														break;
													case "list":
														api.sendPrivateMessage(e.getInvokerId(), "list : Print list of existing artists");
														break;
													default:
														api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not exist.\nType !help for further informations.");
														break;
													}
												}
												
											}
										}
										else{
											if (message.startsWith("getArtist ")){
												message = message.substring(10)+" ";
												if (GestionServer.isArtist(message)){
													String songList = GestionServer.getArtistSong(message);
													api.sendPrivateMessage(e.getInvokerId(), "List of " + message + "'s songs : " + songList);
												}
												else{
													api.sendPrivateMessage(e.getInvokerId(), "Artist " + message + "does not exist");
												}
											}
											else{
												if (message.startsWith("list")){
													if (message.equals("list")){
														api.sendPrivateMessage(e.getInvokerId(), "List of artist  : " + GestionServer.getArtistList());
													}
													else{
														api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not take parameters");
													}
												}
												else{
													if (message.startsWith("dlClient")){
														if (message.equals("dlClient")){
															api.sendChannelMessage("Client link : [url=https://github.com/frosqh/GestionBotClient/releases/]here[/url]");
														}
														else{
															api.sendPrivateMessage(e.getInvokerId(), "Command "+message.substring(0, message.indexOf(" "))+" does not take parameters");
														}
													}
													else{
														if (message.startsWith("dontDisturb")){
															if (message.equals("dontDisturb")){
																if (!api.getChannelInfo(api.whoAmI().getChannelId()).getName().contains("Ne pas déranger")){
																	HashMap<ChannelProperty, String> a = new HashMap<ChannelProperty, String>();
																	a.put(ChannelProperty.CHANNEL_NAME, api.getChannelInfo(api.whoAmI().getChannelId()).getName()+ " [Ne pas déranger]");
																	api.editChannel(api.whoAmI().getChannelId(), a);
																}
																else{
																	HashMap<ChannelProperty, String> a = new HashMap<ChannelProperty, String>();
																	a.put(ChannelProperty.CHANNEL_NAME, api.getChannelInfo(api.whoAmI().getChannelId()).getName().substring(0,
																			api.getChannelInfo(api.whoAmI().getChannelId()).getName().length()-18));
																	api.editChannel(api.whoAmI().getChannelId(), a);
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
						}
					}
				}
			}
			
			@Override
			public void onClientMoved(ClientMovedEvent e){
				//System.out.println("Client moved");
				if(api.getChannelInfo(e.getTargetChannelId()).getName().contains("Ne pas déranger")){
					api.kickClientFromChannel(e.getClientId());
				}
			}
		});
		}

	public void sendSelectSong(String song){
		api.sendChannelMessage("[b]Playing [color=blue]"+song+"[color=black] ¸¸.•*¨*•♫♪");
	}
	
	public void moveTo(Client user){
		api.moveQuery(user.getChannelId());
	}

	public TS3Api getApi() {
		return api;
	}
}
