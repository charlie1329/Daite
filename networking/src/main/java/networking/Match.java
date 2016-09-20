package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import networking.chat.ChatUser;
import networking.chat.MatchEvent;


public class Match {

    private SocketIOClient client1, client2;
    private ChatUser chatClient1, chatClient2;
    private String roomID;
    private boolean validMatch;
    private SocketIOClient bot = null;

    private Logger log = LoggerFactory.getLogger(Matcher.class);


    /*
     * Match between two real users
     */
    public Match(SocketIOClient user1, SocketIOClient user2) {
        client1 = user1;
        client2 = user2;

        //this is called from register event, so isBot shoud be setted
        //get which one is the bot
        if(client1.get("isBot")) {
            log.info("[MATCH] user1 is the bot");
            bot = client1;
        }
        else {
            //is not bot, set details
            chatClient1 = client1.get("userData");

        }
        if(client2.get("isBot")) {
            log.info("[MATCH] user2 is the bot");
            bot = client2;
        }
        else {
            chatClient2 = client2.get("userData");            
        }
        //if(bot != null) {
        //}

        roomID = generateRoomID(client1, client2);

        // Clear previous rooms 
        // Forcing each user to subscribe just to one room
        // by clearing all previous subscribed rooms and joining only the current room
        if (clearRooms(client1) && clearRooms(client2)) {
            client1.joinRoom(roomID);
            client2.joinRoom(roomID);
            
            
            //validMatch = true;
            
            //Send message to clients about the new match
        }
        else {
            //TODO error
            log.error("Failed to create a match between {} and {} with roomID: {}", chatClient1.getName(), chatClient2.getName(), roomID);
            validMatch = false;
            return;
        }

        if(bot == null) {
            log.info("Created new match between {} and {} in room {}", chatClient1.getName(), chatClient2.getName(), roomID);
            validMatch = true;
            emitMatch();
        }
        else 
        {
            log.info("Created room: {} for {} with bot\n Waiting for bot to  details...",roomID, chatClient1.getName());
        }
    }
    /*
     * TODO Match between a user and AI
     */
    public Match(){
    
    }
    public void setBotDetails() {
        chatClient2 = client2.get("userData");
        log.info("Setting bot details: {}", chatClient2.getName());
        emitMatch();
    }
    /*
     * Send match event 
     */
    public void emitMatch() {
        System.out.println(chatClient2.getName());
        client1.sendEvent("matchfound", new MatchEvent(roomID, chatClient2.getName()));
        client2.sendEvent("matchfound", new MatchEvent(roomID, chatClient1.getName()));

    }

    /*
     * Get the other client in a match
     */
    public SocketIOClient getPairedClient(UUID uuid){
        if(client1.getSessionId() == uuid)
            return client2;
        else
            if(client2.getSessionId() == uuid) 
                return client1;
            else
                return null;
    }

    /*
     * Clear previously joined rooms
     */
    public boolean clearRooms(SocketIOClient client) {
        List<String> allRooms= new ArrayList<>(client.getAllRooms());
        if(client.getAllRooms().isEmpty()) {
            return true;
        }
        else {
            for (String room : allRooms) {
                client.leaveRoom(room);
            }
        }

        if(client.getAllRooms().isEmpty()) {
            return true;
        }
        else 
            return false;

    }

    public String getRoomID(){
        return roomID;
    }

    public ChatUser getChatClient1() {
        return chatClient1;
    }

    public ChatUser getChatClient2() {
        return chatClient2;
    }

    private static String generateRoomID(SocketIOClient user1, SocketIOClient user2) {
		long user1Hash = user1.getRemoteAddress().hashCode();
		long user2Hash = user2.getRemoteAddress().hashCode();
		return Long.toString(user1Hash + user2Hash);
	}


    

}
