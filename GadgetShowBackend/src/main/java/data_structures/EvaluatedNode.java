package data_structures;

import java.util.ArrayList;

import analysis.Analyser;

/**this class will implement the evaluate method from the BaseNode class
 * this will just make it a bit easier to account for other team members current work
 * @author Ian Temple/Charlie Street
 *
 */
public abstract class EvaluatedNode extends BaseNode {

	private Analyser analyser;
	
	/**constructor same as superclass
	 * 
	 * @param message message for node
	 * @param keywords the keywords of said message
	 * @param analyser the object for nlp
	 */
	public EvaluatedNode(String message, String[] keywords, Analyser analyser) {
		super(message, keywords);
		this.analyser = analyser;
	}
	
	/**constructor same as superclass
	 * 
	 * @param message the message for the node
	 * @param keywords the keywords of said message
	 * @param neighbours the neighbours on the graph
	 * @param analyser the object for nlp
	 */
	public EvaluatedNode(String message, String[] keywords, ArrayList<BaseNode> neighbours, Analyser analyser) {
		super(message, keywords, neighbours);
		this.analyser = analyser;
	}
	
	/**implemented from BaseNode class
	 * see that class for reference about what this should do
	 */
	public double evaluate(String message) {
		return 0.5;
	}
	
}
