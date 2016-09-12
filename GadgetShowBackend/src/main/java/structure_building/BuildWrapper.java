package structure_building;

import java.util.ArrayList;
import java.util.HashMap;

import data_structures.Question;

/**class is a wrapper for the build method in BuildHashOfGraphs
 * 
 * @author Charlie Street
 *
 */
public class BuildWrapper {
	
	private HashMap<String,Question> graphs;
	private HashMap<String,ArrayList<Question>> questionList;
	
	/**constructor initialises fields
	 * 
	 * @param graphs the hash of graphs
	 * @param questionList the list of questions by topic
	 */
	public BuildWrapper(HashMap<String,Question> graphs, HashMap<String,ArrayList<Question>> questionList) {
		this.graphs = graphs;
		this.questionList = questionList;
	}
	
	//SELF EXPLANATORY GET METHODS FOR WRAPPER CLASS
	public HashMap<String,Question> getGraphs() {
		return this.graphs;
	}
	
	public HashMap<String,ArrayList<Question>> getQuestionList() {
		return this.questionList;
	}
}
