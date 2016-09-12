package data_structures;

import java.util.ArrayList;

/**this class represents a response to a question with a third step acknowledgement
 * 
 * @author Charlie Street
 *
 */
public class Response extends EvaluatedNode {
	
	private boolean changeTopic;//if this response is used, should we call for a change in topic
	//if this is true, neighbours should be empty
	private boolean ourResponse;//if we are asked the parent question, should we answer with this response
	private Question parent;//the parent question that can be answered with this response
	private String acknowledgement;//the third step to a round of conversation
	private String[] followUp; //a String array with follow up question id's; these are hex strings
	
	/**same as super class constructor but added new fields into constructor
	 * 
	 * @param msg the response
	 * @param keys keywords from response
	 * @param changeTopic should we change topic?
	 * @param ourResponse should we use this response?
	 * @param parent the parent question
	 * @param ack third step acknowledgement
	 * @param followUp the array of question id's
	 */
	public Response(String msg, String[] keys, boolean changeTopic, boolean ourResponse, Question parent, String ack, String[] followUp) {
		super(msg,keys);
		this.changeTopic = changeTopic;
		this.ourResponse = ourResponse;
		this.parent = parent;
		this.acknowledgement = ack;
		this.followUp = followUp;
	}
	
	/** overwriting second super class constructor
	 * 
	 * @param msg see above
	 * @param keys see above
	 * @param neigh neighbours on graph
	 * @param change see above
	 * @param ours see above
	 * @param parent see above
	 * @param ack see above
	 * @param followUp see above
	 */
	public Response(String msg, String[] keys, ArrayList<BaseNode> neigh, boolean change, boolean ours, Question parent, String ack, String[] followUp) {
		super(msg,keys,neigh);
		this.changeTopic = change;
		this.ourResponse = ours;
		this.parent = parent;
		this.acknowledgement = ack;
		this.followUp = followUp;
	}
	
	/**self explanatory get method
	 * 
	 * @return changeTopic
	 */
	public boolean shouldIChangeTopic() {
		return this.changeTopic;
	}
	
	/**self explanatory get method
	 * 
	 * @return ourResponse
	 */
	public boolean shouldIRespondWithThis(){
		return this.ourResponse;
	}
	
	/**self explanatory get method
	 * 
	 * @return parent
	 */
	public Question getParent() {
		return this.parent;
	}
	
	/**self explanatory get method
	 * 
	 * @return acknowledgement
	 */
	public String getAck() {
		return this.acknowledgement;
	}
	
	/**self explanatory get method
	 * 
	 * @return array of question ids
	 */
	public String[] getFollowUps() {
		return this.followUp;
	}
	
	/**self explanatory method, implemented from BaseNode class
	 * 
	 */
	public boolean isQuestion() {
		return false;
	}
}
