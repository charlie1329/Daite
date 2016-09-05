package data_structures;

import java.util.ArrayList;

/**this class represents a question in a conversation
 * the main addition from it's super class is the implementation
 * of the isQuestion method and the inclusion of an opener field
 * @author Charlie Street
 *
 */
public class Question extends EvaluatedNode {
	
	private boolean isOpener;//will state whether question is an opening question for the topic it is in
	private boolean usedByAI;//as suggested by mac, maybe our ai doesn't want the ability to ask every question
	private int id; //used for linking questions and responses
	
	/**same as super class constructor with inclusion of isOpener value too
	 * 
	 * @param message the question the node represents
	 * @param keywords the keywords in said question
	 * @param isOpener is the question an opener for a topic
	 * @param id the unique question id
	 */
	public Question(String message, String[] keywords, boolean isOpener, boolean usedByAI, int id) {
		super(message, keywords);
		this.isOpener = isOpener;
		this.usedByAI = usedByAI;
		this.id = id;
	}
	
	/**same as super class constructor with inclusion od isOpener value
	 * 
	 * @param message the question
	 * @param keywords the keywords in the question
	 * @param neighbours the neighbours in the graph
	 * @param isOpener is the question an opener for a topic
	 * @param id the unique question id
	 */
	public Question(String message, String[] keywords, ArrayList<BaseNode> neighbours, boolean isOpener, boolean usedByAI, int id) {
		super(message, keywords, neighbours);
		this.isOpener = isOpener;
		this.usedByAI = usedByAI;
		this.id = id;
	}
	
	/**returns status of isOpener field
	 * isOpener is immutable in this class
	 * @return is question an opener for a topic
	 */
	public boolean isOpener() {
		return this.isOpener;
	}
	
	/**returns whether question is asked by our AI
	 * 
	 * @return is the question used by our AI?
	 */
	public boolean isUsedByAI() {
		return this.usedByAI;
	}
	
	/**implementation from BaseNode class
	 * fairly self explanatory
	 */
	public boolean isQuestion() {
		return true;
	}
	
	/**returns question id
	 * 
	 * @return id
	 */
	public int getID() {
		return this.id;
	}
}
