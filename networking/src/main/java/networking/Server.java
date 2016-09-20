package networking;

import java.util.*;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;

import networking.Connection;

import networking.chat.ChatMessage;
import networking.chat.ChatUser;
import networking.chat.DisconnectEvent;

/**
 * @author Andreas, @author Vlad
 */
public class Server {
	private static final Logger log = LoggerFactory.getLogger(Server.class);
    //private static Logger log = LogManager.getLogger(Server.class.getName());
    private SocketIOServer socketServer;
	private Matcher matcher;
	
	public Server(Configuration config) {

    	socketServer = new SocketIOServer(config);
    	matcher = new Matcher();
    	
    	SocketIONamespace chatNamespace = socketServer.addNamespace("/chat");

        SocketIONamespace botNamespace = socketServer.addNamespace("/botmanagement");
    	
    	log.info("Created namespace: {}", chatNamespace.getName());
    	
    	chatNamespace.addConnectListener(client -> {
			log.info("Connection from {} to {}", client.getRemoteAddress(), chatNamespace.getName());
		});
    	
    	chatNamespace.addDisconnectListener(client -> {
			log.info("{} disconnected from namespace {}", client.getRemoteAddress(), chatNamespace.getName());
		    // If a client disconnects, check first if it is matching queue, but not already matched
            if(matcher.removeFromQueue(client)) {
                log.info("Client removed from queue");
                return;
            }
            
            // If client is a bot and is waiting 
            if(matcher.removeAvailableBot(client)) {
                log.info("Bot disconnected and was removed from the bots list");
            }
        });
    	
    	// Handle register requests
    	chatNamespace.addEventListener("register", ChatUser.class, (client, data, ackSender) -> {
    		client.set("userData", data);
    		client.set("registered", true);

            // Check if client registering is a bot
            // If this parameter was not set yet, client is not a bot
            if(client.get("isBot") == null) {
                client.set("isBot", false);
            }
            boolean isbot = client.get("isBot");

            // If registering client is a bot
            if(isbot) {
                log.info("Bot: {} registred as {}, {}, {}", client.getRemoteAddress(), data.getName(), data.getAge(), data.getGender());
                // Get the room
                // Room alocation is done when a client connects and is matched with a bot, so at
                // this point bot shoud have been alocated a room
                List<String> allRoomsList = new ArrayList<>(client.getAllRooms());
                if(allRoomsList.size() == 1) {
                    String room = allRoomsList.get(0);
                     // Check if valid room
                    if(matcher.isCurrentRoom(room)) {
                        log.info("Found bot room: {}. Staring conversation...", room);
                        matcher.startBotChat(room);
                        //TODO: send error
                    }
                }
                else {
                    log.info("Bot: {} was not assigned to any room!", client.getRemoteAddress());
                    //TODO: send disconnect 
                    return;
                }
                           }
            // Match a real user here
            else {
                log.info("Registration from {}: {}, {}, {}", client.getRemoteAddress(), data.getName(), data.getAge(), data.getGender());
                matcher.match(client);
            }
        });
        
        // Typing events
        chatNamespace.addEventListener("isTyping", Boolean.class, (client, data, ackSender) -> {
            SocketIOClient matchedClient = matcher.getMatchedClient(client);
            
            if(matchedClient != null) {
                matchedClient.sendEvent("isTyping", data);
            }
            else {
                ChatUser user = client.get("userData");
                log.error("Could not find matched client for username: {}", user.getName());  
            }
        });

    	// Handle leaveroom requests
    	chatNamespace.addEventListener("leaveroom", DisconnectEvent.class, (client, data, ackSender) -> {
            log.info("Received leave room event: {} ", data.getRoomID());

            // Check if valid room
            if(matcher.isCurrentRoom(data.getRoomID())) {
                // Remove the match
                matcher.removeMatch(data.getRoomID());
                // in case the user ended the conversation, send event to the matched user
                if(data.getEarly()) {
                    //TODO send disconnect event to the other client
                }
            }	
		});
    	
    	// Handle messages
    	chatNamespace.addEventListener("message", ChatMessage.class, (client, data, ackSender) -> {
            log.warn("message {} from {}", data.getUsername(), data.getMessage());
            //Check if client is registred
    		if(client.get("registered") != null) {
    			//Got the message
                //FIXME maybe there is a better way to get the roomID
                //FIXME catch get empty room
                List<String> allRoomsList = new ArrayList<>(client.getAllRooms());
                String room = allRoomsList.get(0);                
                
				if(room != null) {
                    log.info("Recieved message from {}: {} to forward in room: {}", data.getUsername(), data.getMessage(), room);

					chatNamespace.getRoomOperations(room).sendEvent("message", data);
					client.sendEvent("message", data);
				} 
                else {
					log.error("Client is not subscribed in a room: {}",  client.getRemoteAddress());
                    //client.sendEvent("error", "Room not found");
				}
            }
            else {
    	        log.error("Client attempted to send message when not registered: {}", client.getRemoteAddress());
    			client.sendEvent("error", "Not registered");
	        }
		});

        /*
         * When a bot connects, add it to available bot list
         */
        chatNamespace.addEventListener("bot-available", Boolean.class, (client, data, ackSender) -> {
			log.info("Bot available at :{}", client.getRemoteAddress());
            client.set("isBot", true);
            matcher.addAvailableBot(client);
        });

        // Probably not needed anymore    	
    	// Handle joinroom requests
    	chatNamespace.addEventListener("joinroom", String.class, (client, data, ackSender) -> {
    		if(client.get("registered") == null) {
    			log.error("Client attempted to join room when not registered: {}", client.getRemoteAddress());
    			client.sendEvent("error", "Not registered");
    		} else {

				String oldroom = client.get("room");
				if(oldroom != null) {
					client.leaveRoom(oldroom);
				}
                
				client.set("room", data);
				client.joinRoom(data);
				
				ChatUser userData = client.get("userData");
				log.info("{} requested to join room {}", userData.getName(), data);
				chatNamespace.getRoomOperations(data).sendEvent("message", new ChatMessage(userData.getName(), "joined the room"));
    		}
		});	
	}

	public void start() {
		socketServer.start();
	}
	
	
    public static void main(String[] args) {
		Configuration config = new Configuration();
    	config.setPort(6969);

    	SocketConfig socketConfig = new SocketConfig();
    	socketConfig.setReuseAddress(true);
    	config.setSocketConfig(socketConfig);
    	
    	Server chatServer = new Server(config);
    	chatServer.start();
	}
}
