package networking.chat;

/**
 * Data class for sending match to client
 * @author andreas
 *
 */
public class MatchEvent {
	
	private String matchUsername;
    private String roomID;

	public MatchEvent(String roomID, String matchUsername) {
        this.matchUsername = matchUsername;
        this.roomID = roomID;
    }

    public String getMatchUsername() {
        return matchUsername;
    }
    
	public String getRoomID() {
		return roomID;
	}
	
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
}
