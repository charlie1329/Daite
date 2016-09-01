package structure_building;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data_structures.*;
import logger.ConvoLogger;


public class BuildHashOfGraphs {
	
	private final static int HOW_MANY_THREADS = 10;//how many worker threads will be used for the thread pool

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
	
	/**this method will build up our hash map by building each topic in a separate thread using thread pools
	 * 
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
							
						}
						
						synchronized(convoMap) {//putting into synchronized block to prevent any issues
							convoMap.put(topicName, opener);//with multiple threads modifying the same object
						}
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
		return convoMap;
	}
	
	public static void main(String[] args) {
		build(new ConvoLogger());
	}
}
