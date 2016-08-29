package data_structures;

import java.util.ArrayList;

/**this class will implement the evaluate method from the BaseNode class
 * this will just make it a bit easier to account for other team members current work
 * @author Ian Temple/Charlie Street
 *
 */
public abstract class EvaluatedNode extends BaseNode {

	/**constructor same as superclass
	 * 
	 * @param message message for node
	 * @param keywords the keywords of said message
	 */
	public EvaluatedNode(String message, String[] keywords) {
		super(message, keywords);
	}
	
	/**constructor same as superclass
	 * 
	 * @param message the message for the node
	 * @param keywords the keywords of said message
	 * @param neighbours the neighbours on the graph
	 */
	public EvaluatedNode(String message, String[] keywords, ArrayList<BaseNode> neighbours) {
		super(message, keywords, neighbours);
	}
	
	/**implemented from BaseNode class
	 * see that class for reference about what this should do
	 */
	public double evaluate(String message) {
		return 0.5;
	}
	
}
