package traversal;

import java.util.ArrayList;
import java.util.HashMap;

import data_structures.*;

/**this class will contain the code to allow
 * the responding to user input messages
 * @author Charlie Street
 *
 */
public class Conversation {
	
	private final String INITIAL_TOPIC_NAME_IF_STARTING = "greetings";//this may change 
	private final double CONFIDENCE_THRESHOLD = 0.3;//will require tweaking as I have just put this off the top of my head
	private EvaluatedNode currentNode;//node we are currently at
	private String currentTopic;//useful for accessing questions 
	private String previousTopic;
	private HashMap<String,ArrayList<Question>> questionList;
	private ArrayList<EvaluatedNode> cachedNodes;//nodes used recently than I can access easily and change internal state readily
	private HashMap<String,Question> graphs;
	private boolean aiStart;//who initiates our conversation
	
	
	/**constructor sets up hash map and starting place, depending on who starts conversation
	 * 
	 * @param graphs the hash map of graphs to traverse
	 * @param aiStart who should start the conversation 
	 */
	public Conversation(HashMap<String,Question> graphs, boolean aiStart, HashMap<String,ArrayList<Question>> questionList) {
		this.cachedNodes = new ArrayList<EvaluatedNode>();
		this.graphs = graphs;
		this.aiStart = aiStart;
		this.currentTopic = INITIAL_TOPIC_NAME_IF_STARTING;
		this.previousTopic = INITIAL_TOPIC_NAME_IF_STARTING;
		this.questionList = questionList;
		if(aiStart) {
			this.currentNode = this.graphs.get(INITIAL_TOPIC_NAME_IF_STARTING);
			//intended to be something like "Hi, i'm <name>. How are you?"
		} else {
			ArrayList<BaseNode> responsesForOpener = this.graphs.get(INITIAL_TOPIC_NAME_IF_STARTING).getNeighbours();
			for(int i = 0; i < responsesForOpener.size(); i++) {
				if(((Response)responsesForOpener.get(i)).shouldIRespondWithThis()) {
					this.currentNode = (Response)responsesForOpener.get(i);//will give our response for opening question
					//this basically just puts us in a nice position to look for initial question asked by subject
					break;//break out of for loop
				}
			}
		}
	}
	
	/**this method takes a message and, depending on the internal state of the 
	 * corresponding object of this class, will attempt to respond to the message
	 * in a *reasonable* manner
	 * returning an array list over one string lets us choose whether to send the response over one 
	 * or more than one chat messages (more than one probably helps us look more human)
	 * @param message the user entered message
	 * @return an array list of string, which, when combined give us the full response
	 */
	public ArrayList<String> respond(String message) {
		this.cachedNodes.add(this.currentNode);//cache start node
		this.previousTopic = this.currentTopic;
		if(this.currentNode.isQuestion()) {//either (Q)(RQ)(ARQ) or (Q)(R)(AQ) where first Q by AI
			return startAtQuestionResponse(message);
		}
		else { //(Q)(RQ) where first Q by subject
			return startAtResponseResponse(message);
		}
		
	}
	
	/**method is used to ask a new question on a new topic
	 * this method will be used in the scenario that a 'timeout occurs'
	 * i.e. there is a silence on the messenger window
	 * in this case we return with a new question to restart the conversation
	 * @return a new question as a string
	 */
	public String timeoutQuestion() {
		changeTopic();//change to new topic to start conversation afresh
		//after changeTopic, currentNode will ALWAYS be a question
		this.cachedNodes.add(this.currentNode);//cache node just in case
		this.currentNode.setVisited(true);//visited node!
		return this.currentNode.getMessage();
	}
	
	/**method will roll-back from most recent traversal 'move'
	 * this will be necessary if the subject adds to their message during our 'writing' time
	 * first element of list will be start node, rest will be visited nodes
	 */
	public void rollback() {
		this.currentTopic = this.previousTopic;
		if(this.cachedNodes.size() == 0) {//dealing with this unlikely scenario
			return;
		} else {
			this.currentNode = this.cachedNodes.get(0);//resetting start node
			for(int i = 1; i < this.cachedNodes.size(); i++) {
				this.cachedNodes.get(i).setVisited(false);//regress back to not being visited
				//this is the only part of the internal state that will be changed
			}
		}
	}
	
	/**self explanatory get method
	 * 
	 * @return aiStart
	 */
	public boolean willAIStart() {
		return this.aiStart;
	}
	
	/**method will change the current node so it is in a different topic graph (unvisited but fairly random)
	 * 
	 */
	private void changeTopic() {
		//TODO fill in!
	}
	
	/**method will carry out a round of traversing the tree, if we were at a question at the start
	 * i.e. we have just asked a question
	 * @param message the user input message
	 * @return the possibly multiple strings which form our response
	 */
	private ArrayList<String> startAtQuestionResponse(String message) {
		//TODO fill in!
		return new ArrayList<String>();
	}
	
	/**method will carry out a round of traversing the tree, if we were at a response at the start
	 * i.e. we have been asked a question
	 * @String message the user input message
	 * @return the possibly multiple strings which form our response
	 */
	private ArrayList<String> startAtResponseResponse(String message) {
		ArrayList<String> toReturn = new ArrayList<String>();
		//attempting to find all questions!
		ArrayList<Question> questions = this.questionList.get(this.currentTopic);//will get all available questions on a topic
		
		//finding the most likely question being asked
		Question mostLikely = null;
		double currentMax = CONFIDENCE_THRESHOLD;
		for(int i = 0; i < questions.size(); i++) {//simply finding max likelihood in array list
			double currentVal = questions.get(i).evaluate(message);
			if(currentVal > currentMax) {
				mostLikely = (Question)questions.get(i);
				currentMax = currentVal;
			}
		}
		
		if(currentMax > CONFIDENCE_THRESHOLD && mostLikely != null) {//if we have a suitable level of confidence we have found the question
		    //'moving' to this node so to speak
			this.cachedNodes.add(mostLikely);
			mostLikely.setVisited(true);
			//TODO get our response, make reply and ask follow up question
			//TODO if no follow ups are available (i.e. all asked, switch to new topic)
		} else {//if we can't find the question
			//TODO search for question within other topics
			//TODO if found do same as above :)
			//TODO otherwise use a desperation tactic
		}
		return toReturn;
	}
	
}
