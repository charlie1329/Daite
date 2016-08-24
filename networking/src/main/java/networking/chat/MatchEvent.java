package networking.chat;

/**
 * Data class for sending match to client
 * @author andreas
 *
 */
public class MatchEvent {
	
	private ChatUser matchedUser;
	private String room;

	public MatchEvent() {}
	
	public MatchEvent(ChatUser matchedUser, String room) {
		super();
		this.matchedUser = matchedUser;
		this.room = room;
	}

	public ChatUser getMatchedUser() {
		return matchedUser;
	}
	
	public void setMatchedUser(ChatUser matchedUser) {
		this.matchedUser = matchedUser;
	}
	
	public String getRoom() {
		return room;
	}
	
	public void setRoom(String room) {
		this.room = room;
	}
}
