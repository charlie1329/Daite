package networking.chat;

/**
 * Class to represent a chat message.
 * @author andreas
 *
 */
public class ChatMessage {
	private String username;
	private String message;
	
	public ChatMessage(){}
	public ChatMessage(String username, String message) {
		super();
		this.username = username;
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
