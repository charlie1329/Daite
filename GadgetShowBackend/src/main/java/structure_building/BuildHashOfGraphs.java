package structure_building;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import data_structures.*;
import logger.ConvoLogger;

/**this class deals with the building of the data structures
 * from the raw JSON data
 * @author Charlie Street
 *
 */
public class BuildHashOfGraphs {
	
	private final static int HOW_MANY_THREADS = 10;//how many worker threads will be used for the thread pool
	private static HashMap<String,ArrayList<Question>> questionList = new HashMap<String,ArrayList<Question>>();//necessary for traversal

	/**method will retrieve all JSON files in the data folder of this repository
	 * 
	 * @return the String of all JSON files
	 */
	private static String[] getJSONFiles() {
		String[] folder = new File("data").list();
		for(int i = 0; i < folder.length; i++) {
			folder[i] = "data/"+folder[i];
		}
		return folder;
	}
	
	/**method will take a question or response and organise the keywords into one convenient array
	 * 
	 * @param qOrR the object that is either a question or response
	 * @return a string array of keywords
	 */
	private static String[] getKeywords(JSONObject qOrR) {
		JSONArray presentKeywords = null;
		JSONArray absentKeywords = null;
		JSONArray negatedKeywords = null;
		try {presentKeywords = (JSONArray)qOrR.get("presentKeywords");}catch(Exception e){}
		try {absentKeywords = (JSONArray)qOrR.get("absentKeywords");}catch(Exception e) {}
		try {negatedKeywords = (JSONArray)qOrR.get("negatedKeywords");}catch(Exception e) {}
		
		ArrayList<String> keyWords = new ArrayList<String>();//converting into array list of strings
		if(presentKeywords != null) {
			for(int i = 0; i < presentKeywords.size(); i++) {
				keyWords.add((String)presentKeywords.get(i));
			}
		}
		if(absentKeywords != null) {
			for(int i = 0; i < absentKeywords.size(); i++) {
				keyWords.add((String)absentKeywords.get(i));
			}
		}
		if(negatedKeywords != null) {
			for(int i = 0; i < negatedKeywords.size(); i++) {
				keyWords.add((String)negatedKeywords.get(i));
			}
		}
		
		String[] keys = new String[keyWords.size()];//converting array list to standard array
		keys = keyWords.toArray(keys);
		return keys;
	}
	
	/**method will get the follow ups into String array form
	 * 
	 * @param arr the json array
	 * @return the json array as String array
	 */
	private static String[] getFollowUps(JSONArray arr) {
		String[] converted = new String[arr.size()];
		for(int i = 0; i < arr.size(); i++) {
			converted[i] = (String)arr.get(i);
		}
		return converted;
	}
	
	/**method takes a list of children from a json file and forms it into a set of responses
	 * 
	 * @param responses the responses from the json file
	 * @param parent the question those responses are suitable for
	 * @return the responses in response objects
	 */
	private static ArrayList<Response> formResponses(JSONArray responses, Question parent) {
		ArrayList<Response> builtRs = new ArrayList<Response>();
		for(int i = 0; i < responses.size(); i++) {
			JSONObject currentR = (JSONObject)responses.get(i);
			
			String message = (String)currentR.get("message");//should be compulsory!
			String[] keys = getKeywords(currentR);
			boolean changeTopic = false;
			try {changeTopic = (boolean)currentR.get("switchTopic");}catch(Exception e){}//using try-catch due to JSON layout
			boolean ourResponse = false;
			try {ourResponse = (boolean)currentR.get("usedByAI");}catch(Exception e){}
			String response = "";
			try {response = (String)currentR.get("response");}catch(Exception e){}
			
			JSONArray followUp = null;
			try {followUp = (JSONArray)currentR.get("followUp");}catch(Exception e){}
			String[] hexFollowUps = (followUp==null)?null:getFollowUps(followUp);
			
			Response newResponse = new Response(message,keys,changeTopic,ourResponse,parent,response,hexFollowUps);//create response node
			builtRs.add(newResponse);//add to list of responses
			parent.addNeighbour(newResponse);//add as child of question
		}
		return builtRs;
	}
	
	/**method will make sure the links between the questions and responses, other than parent/children relationships
	 * are set up correctly
	 * @param topicQs the questions for a topic
	 * @param topicRs the responses
	 */
	private static void linkUpQsAndRs(ArrayList<Question> topicQs, ArrayList<Response> topicRs) {
		for(int i = 0; i < topicRs.size(); i++) {
			for(int j = 0; j < topicQs.size(); j++) {
				 if(!topicRs.get(i).shouldIChangeTopic()) {//everything bar switch topic nodes have follow ups
					String[] links = topicRs.get(i).getFollowUps();
					for(int k = 0; k < links.length; k++) {
						if(links[k].equals(topicQs.get(j).getID())) {
							topicRs.get(i).addNeighbour(topicQs.get(j));
							break;
						}
					}
				}
			}
		}
	}
	
	/**simple get method for question list
	 * 
	 * @return the question list
	 */
	public static HashMap<String,ArrayList<Question>> getQuestionList() {
		return questionList;
	}
	
	/**method wipes the question list since it's static
	 * 
	 */
	public static void wipeQuestionList() {
		questionList = new HashMap<String,ArrayList<Question>>();
	}
	
	/**this method will build up our hash map by building each topic in a separate thread using thread pools
	 * 
	 * @param logger our on-screen logger for how the AI is doing
	 * @return the full data structure used for conversations
	 */
	public static HashMap<String,Question> build(ConvoLogger logger) {
		HashMap<String,Question> convoMap = new HashMap<String,Question>();//the data structure itself
		ExecutorService threadPool = Executors.newFixedThreadPool(HOW_MANY_THREADS);
		String[] files = getJSONFiles();
		
		for(int i=0; i<files.length; i++) {
			String currentFile = files[i];
			threadPool.submit(new Runnable() {
				public void run() {
					try {
						JSONParser parser = new JSONParser();
						JSONObject topic = (JSONObject)parser.parse(new FileReader(currentFile));
						String topicName = (String)topic.get("topic");//the key for the hash table
						Question opener = null;//will be the value of the hash table!
						JSONArray questions = (JSONArray)topic.get("questions");
						
						ArrayList<Question> topicQs = new ArrayList<Question>();
						ArrayList<Response> topicRs = new ArrayList<Response>();
						
						for(int i = 0; i < questions.size(); i++) {//looping through all questions
							JSONObject question = (JSONObject)questions.get(i);
							String message = (String)question.get("message");//the actual question
							
							String[] keywords = getKeywords(question);//combine keywords to one array
							
							boolean usedByAI = false;//does the ai use this question?
							try {usedByAI = (boolean)question.get("usedByAI");}catch(Exception e){}
							
							boolean isOpener = false;//is this the opening question
							try {isOpener = (boolean)question.get("isOpener");}catch(Exception e){}
							
							String qID = (String)question.get("id");//will always have a question ID
							
							Question newQuestion = new Question(message, keywords, isOpener, usedByAI, qID);
							if(newQuestion.isOpener()) {opener = newQuestion;}//should only happen once
							topicQs.add(newQuestion);//add to list
							
							JSONArray responses = (JSONArray)question.get("children");//getting responses for question
							
							topicRs.addAll(formResponses(responses,newQuestion));
						}
						
						linkUpQsAndRs(topicQs,topicRs);//link up graph correctly
						
						synchronized(convoMap) {//putting into synchronised block to prevent any issues
							convoMap.put(topicName, opener);//with multiple threads modifying the same object
						}
						synchronized(questionList) {//adding to topics list
							questionList.put(topicName, topicQs);
						}
						logger.logMessage("Task finished");
						
					} catch(Exception e) {//if something goes wrong parsing a JSON file
						logger.logMessage("Error parsing JSON file: " + currentFile + "\n" +
										  e.getMessage() +
										  "\nBuilding failed. It is recommended to restart the system;\n" +
										  "Incorrect builds will lead to bad conversations!");
					}
				}
			});
		}
		logger.logMessage("All topics submitted to thread pool");
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(1000000, TimeUnit.SECONDS);//extremely long timeout, just to check everything is finished
		} catch(InterruptedException e) {
			logger.logMessage("interrupted while building");//if interrupted
		}
		logger.logMessage("Finished building of Hash Map");
		return convoMap;
	}
	
	public static void main(String[] args) {
		build(new ConvoLogger());
	}
}
