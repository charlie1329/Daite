package networking.chat;

public class DisconnectEvent {
    private String roomID;
    private boolean early;

    public DisconnectEvent() {
    }
    public DisconnectEvent(String roomID, boolean early){
        super();
        this.roomID = roomID;
        this.early = early;
    }

    public String getRoomID(){
        return roomID;
    }

    public boolean getEarly() {
        return early;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setEarly(boolean early) {
        this.early = early;
    }
}
