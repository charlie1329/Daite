package network;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONObject;
import org.json.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class Client {
    private Socket socket;

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    //Match details
    private boolean botMatched = false;
    private String matchName, matchGender;
    private int matchAge;

    //Bot details
    // we need to keep the name assigned in order to ignore/check if the server received the
    // message bot send
    private String botName = "testBOT";
    

    public Client(String domain, String port, String namespace) {
        log.info("Bot connecting to: {}:{}/{}", domain, port, namespace);
        try {
            socket = IO.socket(domain + ":" + port + "/" + namespace);
        }
        catch (URISyntaxException e) {

        }
        // TODO initialize bot stuff here
        // try {
            // bot stuff
            
            // connect to server after initialization is done
            connect();
        // }
        // catch () {
        // }
        
        /*
         * Server listeners
         */
        // receving details of the user which bot was matched with
        socket.on("bot-matched", onMatch);
        // user typing events
        socket.on("isTyping", onTyping);
        // receiving messages
        socket.on("message", onMessage);
    }
   
    /*
     * Connect to server
     */
    private void connect() {
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("bot-available");
                log.info("Bot connected to server");
            }
        });
    }

    /*
     * Send computed bot details to server 
     */
    public void sendDetails(String name, int age, String gender) {
        JSONObject details = new JSONObject();

        try {
            details.put("name", name);
            details.put("age", age);
            details.put("gender", gender);
        }
        catch(JSONException e) {
            log.error(e.getMessage());
            return;
        }

        log.info("Sending bot details to server: name: {}, age: {}, gender: {}", name, age, gender);
        socket.emit("register", details);
    }

    /*
     * Send message
     */
    public void sendMessage(String message) {
        if(botName == null) {
            return;
        }
        
        JSONObject msg  = new JSONObject();
        try {
            msg.put("username", botName);
            msg.put("message", message);
        }
        catch (JSONException e) {
            log.error(e.getMessage());
        }

        log.info("Sending message: {}");
        socket.emit("message", msg);
    }

    /*
     * Stop typing indicator
     */
    public void stopTyping() {
        socket.emit("isTyping", false); 
    }

    /*
     * Trigger typing indicator
     */
    public void startTyping() {
        socket.emit("isTyping", true);
    }
    
    /*
     * Parse the match details received from the server
     */
    private Emitter.Listener onMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject matchDetails = (JSONObject) args[0];
            try {
                matchName = matchDetails.getString("name");
                matchAge = matchDetails.getInt("age");
                matchGender = matchDetails.getString("gender");
            }
            catch(JSONException e) {
                log.error(e.getMessage());
            }

            log.info("Received match details. name: {}, gender: {}, age: {}", matchName, matchGender, matchAge);
            botMatched = true;
            //TODO here you have the match details
        }
    };

    /*
     * On typing listener
     */
    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            boolean isTyping = (boolean) args[0];
            if (isTyping) {
                log.info("Match started typing");
                // TODO: match started typing
            }
            else {
                log.info("Match stopped typing");
                // TODO: match stoped typing
            }
        }
    };
    
    /* 
     * Incomming messages
     */
    private Emitter.Listener onMessage = new Emitter.Listener() {
        String username, message; 
        
        @Override
        public void call(Object... args) {
            JSONObject msg = (JSONObject) args[0];
            try {
                username = msg.getString("username");
                message = msg.getString("message");
            }
            catch(JSONException e){
                log.error(e.getMessage());
                return;
            }

            if(username == botName) {
                // ignore for now the messages bot sent
            }
            else {
                if(message != null){
                    log.info("Received message from match ({}): {}", username, message);
                        // TODO here you have the message from the match
                }
            }
        }
    };

    public static void main (String[] args) {
        // Setup connection
        Client client = new Client("http://localhost", "6969" , "chat");
        
        //client.sendDetails("bot", 21, "male");
        //client.sendMessage("this is a message from the bot");
    }
}

