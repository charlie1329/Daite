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
	
	/**same as super class constructor with inclusion of isOpener value too
	 * 
	 * @param message the question the node represents
	 * @param keywords the keywords in said question
	 * @param isOpener is the question an opener for a topic
	 */
	public Question(String message, String[] keywords, boolean isOpener) {
		super(message, keywords);
		this.isOpener = isOpener;
	}
	
	/**same as super class constructor with inclusion od isOpener value
	 * 
	 * @param message the question
	 * @param keywords the keywords in the question
	 * @param neighbours the neighbours in the graph
	 * @param isOpener is the question an opener for a topic
	 */
	public Question(String message, String[] keywords, ArrayList<BaseNode> neighbours, boolean isOpener) {
		super(message, keywords, neighbours);
		this.isOpener = isOpener;
	}
	
	/**returns status of isOpener field
	 * isOpener is immutable in this class
	 * @return is question an opener for a topic
	 */
	public boolean isOpener() {
		return this.isOpener;
	}
	
	/**implementation from BaseNode class
	 * fairly self explanatory
	 */
	public boolean isQuestion() {
		return true;
	}
}
