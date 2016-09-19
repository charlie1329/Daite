package network;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import logger.ConvoLogger;
import psychology.Formaliser;
import psychology.NameMatcher;
import structure_building.BuildHashOfGraphs;
import structure_building.BuildWrapper;
import timing.StandardMessageWritingTimer;
import timing.TypingDotsVisibilityCallback;
import timing.WritingCompletedCallback;
import traversal.Conversation;

import org.json.JSONObject;
import org.json.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Analyser;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

/**class contains networking code for ai client
 * as well as the code for making the bot run
 * @author Charlie Street - making bot run, Vlad Rotea - networking client
 *
 */
public class Client {
    private Socket socket;

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private ConvoLogger logger = new ConvoLogger();
    private Conversation convo;
    private StandardMessageWritingTimer writeTimer;
    private WritingCompletedCallback timeCompleted;
    private TypingDotsVisibilityCallback setWriting;
    private String currentMessage;//this is used to check whether I want to send or not
    private Formaliser formalise;
    private NameMatcher nameChooser;
    
    //Match details
    private boolean botMatched = false;
    private String matchName, matchGender;
    private int matchAge;

    //Bot details
    // we need to keep the name assigned in order to ignore/check if the server received the
    // message bot send
    private static String botName = "testBOT";
    

    public Client(String domain, String port, String namespace) {
        log.info("Bot connecting to: {}:{}/{}", domain, port, namespace);
        logger.logMessage("Bot connecting to: " + domain + ":" + port + "/" + namespace);
        try {
            socket = IO.socket(domain + ":" + port + "/" + namespace);
        }
        catch (URISyntaxException e) {

        }
        
        //initialize bot stuff here
        try {
        	//INITIALISE NLP STUFF
        	Analyser analyser = new Analyser();
        	
        	//INITIALISE OTHER ATTRIBUTES
        	formalise = new Formaliser();
        	nameChooser = new NameMatcher();
        	currentMessage = "";
        	writeTimer = new StandardMessageWritingTimer();
        	timeCompleted = (String s) -> {};//for now I have no idea whether this really needs to do anything
        	setWriting = (boolean setDots) -> {if(setDots) {startTyping();} else {stopTyping();}};//sets writing stuff
        	
            // build data structures
        	BuildWrapper dataStructures = BuildHashOfGraphs.build(logger, analyser);
        	
            //initialise conversation 
        	convo = new Conversation(dataStructures.getGraphs(),true,dataStructures.getQuestionList());
        	logger.logMessage("Conversation Ready");
        	
        	//now connect
            connect();
         }catch (Exception e) {
        	 logger.logMessage("Charlie's seriously messed up here. Shame on you, Charlie!!!");
         }
        
        /*
         * Server listeners
         */
        // receiving details of the user which bot was matched with
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
                logger.logMessage("Bot connected to server");
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
        logger.logMessage("Sending bot details to server: name: " + name + ", age: " + age + ", gender: " + gender);
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
        logger.logMessage(Client.botName + ":> "+ msg);//log on our graphical logger
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
    
    /**method will start the conversation for us
     * 
     */
    public void startConvo() {
    	sendMessage(convo.startConvo());
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
            
            if(matchGender == "male") {
            	Client.botName = nameChooser.pickName(matchName, false);
            	sendDetails(Client.botName,20,"female");
            } else if(matchGender == "female") {
            	Client.botName = nameChooser.pickName(matchName, true);
            	sendDetails(Client.botName,20,"male");
            } else { //be female by default
            	Client.botName = nameChooser.pickName(matchName, false);
            	sendDetails(Client.botName,20,"female");
            }
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
                //bot isn't affected by this
            }
            else {
                log.info("Match stopped typing");
                //bot isn't affected by this
            }
        }
    };
    
    /* 
     * Incoming messages
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
                        
                    logger.logMessage(username+":> " + message);//log to fancy logger what the user has said
                    
                    //code to deal with receiving messages
                    String myMessageToRead;//this will be compared with currentMessage before sending
                    synchronized(currentMessage) {//adding to current message, this will concatenate to any previous messages
                    	currentMessage += message;//and is how I will check whether to send or not
                    	myMessageToRead = currentMessage;
                    }
                    
                    synchronized(convo) {//due to internal states I really only want one accessing at the same time
                    	String formalMessage = formalise.expand(myMessageToRead);//formalise for sake of nlp stuff
                    	ArrayList<String> response = convo.respond(formalMessage);//get our response
                    	String[] messagesRead = new String[]{myMessageToRead};
                    	String[] messagesToWrite = response.toArray(new String[response.size()]);
                    	writeTimer.beginTyping(messagesRead, messagesToWrite, setWriting, timeCompleted);
                    	
                    	synchronized(currentMessage) {//CHECK THIS WONT GIVE DEADLOCK
                    		if(myMessageToRead.equals(currentMessage)) {//i.e. no other messages have been sent
                    			currentMessage = "";
                    			
                    			//FORMAT RESPONSE TO SEND
                    			Random newLineOrSpace = new Random();//randomly selecting how to structure message on chat
                    			String toSend = "";//constructing response
                    			for(int i=0; i < response.size()-1; i++) {//don't do last item
                    				toSend += response.get(i);
                    				boolean currentBool = newLineOrSpace.nextBoolean();
                    				if(currentBool) {
                    					toSend += " <br>";
                    				} else {
                    					toSend += " ";
                    				}
                    			}
                    			toSend += response.get(response.size()-1);
                    			
                    			//SEND MESSAGE
                    			sendMessage(toSend);
                    			
                    		} else {//if another message has come on top and that one is waiting to send
                    			convo.rollback();//roll back the data structures so we don't have any weird stuff happening
                    		}
                    	}
                    }
                    
                }
            }
        }
    };

    /**main method for entire chat bot, will run it as a network client
     * 
     * @param args standard java shiz
     */
    public static void main (String[] args) {
        // Setup connection
        Client client = new Client("http://localhost", "6969" , "chat");//this should start the bot running
        client.startConvo();//initiate conversation!
        //TODO make sure we deal with disconnecting
    }
}

