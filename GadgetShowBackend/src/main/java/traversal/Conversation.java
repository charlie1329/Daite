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
	private EvaluatedNode currentNode;//node we are currently at
	private ArrayList<EvaluatedNode> cachedNodes;//nodes used recently than I can access easily and change internal state readily
	private HashMap<String,Question> graphs;
	private boolean aiStart;//who initiates our conversation
	
	
	/**constructor sets up hash map and starting place, depending on who starts conversation
	 * 
	 * @param graphs the hash map of graphs to traverse
	 * @param aiStart who should start the conversation 
	 */
	public Conversation(HashMap<String,Question> graphs, boolean aiStart) {
		this.cachedNodes = new ArrayList<EvaluatedNode>();
		this.graphs = graphs;
		this.aiStart = aiStart;
		if(aiStart) {
			currentNode = this.graphs.get(INITIAL_TOPIC_NAME_IF_STARTING);
			//intended to be something like "Hi, i'm <name>. How are you?"
		} else {
			ArrayList<BaseNode> responsesForOpener = this.graphs.get(INITIAL_TOPIC_NAME_IF_STARTING).getNeighbours();
			for(int i = 0; i < responsesForOpener.size(); i++) {
				if(((Response)responsesForOpener.get(i)).shouldIRespondWithThis()) {
					currentNode = (Response)responsesForOpener.get(i);//will give our response for opening question
					//this basically just puts us in a nice position to look for initial question asked by subject
					break;//break out of for loop
				}
			}
		}
	}
	
	
	/**method will roll-back from most recent traversal 'move'
	 * this will be necessary if the subject adds to their message during our 'writing' time
	 * first element of list will be start node, rest will be visited nodes
	 */
	public void rollback() {
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
}
