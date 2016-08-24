package networking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;

import networking.chat.ChatUser;
import networking.chat.MatchEvent;

/**
 * Class to match clients with other clients or AI(?).
 * @author andreas
 *
 */
public class Matcher {
	private Logger log = LoggerFactory.getLogger(Matcher.class);
	private BlockingQueue<SocketIOClient> matchQueue;
	
	public Matcher() {
		matchQueue = new LinkedBlockingDeque<>();
	}
	
	public void match(SocketIOClient user) throws InterruptedException {
		synchronized (matchQueue) {
			matchQueue.put(user);
			if(matchQueue.size() >= 2) {
				SocketIOClient match = matchQueue.take();
				matchQueue.remove(user);
				String room = generateRoomID(user, match);
				ChatUser matchUser = match.get("userData");
				ChatUser userUser = user.get("userData");
				user.sendEvent("matchevent", new MatchEvent(matchUser, room));
				match.sendEvent("matchevent", new MatchEvent(userUser, room));
				log.info("Matched, users {}, {} in room {}", userUser.getName(), matchUser.getName(), room);
				return;
			} else {
				//TODO: Match with an AI
			}
		}
	}
	
	/**
	 * Generate 'unique' ID from two clients
	 * @param user1
	 * @param user2
	 * @return the generated ID.
	 */
	private static String generateRoomID(SocketIOClient user1, SocketIOClient user2) {
		long user1Hash = user1.getRemoteAddress().hashCode();
		long user2Hash = user2.getRemoteAddress().hashCode();
		return Long.toString(user1Hash + user2Hash);
	}
}
