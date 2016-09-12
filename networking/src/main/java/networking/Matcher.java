package networking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.Hashtable;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;

import networking.chat.ChatUser;
import networking.chat.MatchEvent;
import networking.Match;

/**
 * Class to match clients with other clients or AI(?).
 * @author andreas
 *
 */
public class Matcher {
	private Logger log = LoggerFactory.getLogger(Matcher.class);
	private BlockingQueue<SocketIOClient> matchQueue;
    private Hashtable<String, Match> matches = new Hashtable<String, Match>();;
	
	public Matcher() {
		matchQueue = new LinkedBlockingDeque<>();
	}

    /*
     * Where the actual matching is done
     */
	public void match(SocketIOClient user) throws InterruptedException {
		synchronized (matchQueue) {
			matchQueue.put(user);
            //TODO match first 10 connections male with female, then match the remaining with the AI
			if(matchQueue.size() >= 2) {

				SocketIOClient matchedClient = matchQueue.take();
				matchQueue.remove(user);
				
                // Create a new match
                Match newMatch = new Match(matchedClient, user);
                
                // Store the new match into the matches list;
                matches.put(newMatch.getRoomID(), newMatch);

                listAllMatches();
                
               	return;
			} 
            else {
				//TODO: Match with an AI
			}
   
        }
	}

    /*
     * Room is a valid room
     */
    public boolean isCurrentRoom(String id) {
        Enumeration roomIDs = matches.keys();
        while(roomIDs.hasMoreElements()) {
            if(id == (String) roomIDs.nextElement()){
                return true;
            } 
        }
        return false;
    }

    /*
     * Returns paired client
     */
    public SocketIOClient getMatchedClient (SocketIOClient client) { 
        //TODO catch empty room ID
        String clientRoomID = client.getAllRooms().iterator().next();
        Match match = searchMatch(clientRoomID);
        if(match != null) {
            ChatUser chatuser = client.get("userData");
            return match.getPairedClient(client.getSessionId());
        }
        else
            return null;

    }

    public Match searchMatch(String roomID) {
        Enumeration roomIDs = matches.keys();
        while(roomIDs.hasMoreElements()) {
            if(roomID == (String) roomIDs.nextElement()) {
                return matches.get(roomID);
            }
        }
        return null;
    }

    // DEBUGING
    private void listAllMatches() {
        log.info("Rooms in use: ----------");
        Enumeration roomIDs = matches.keys();
        while (roomIDs.hasMoreElements()){
            String roomID = (String) roomIDs.nextElement();
            Match match = matches.get(roomID);
            log.info("  Room ID: {}, clients: {}, {}", roomID, match.getChatClient1().getName(), match.getChatClient2().getName());
        }
    }

}



