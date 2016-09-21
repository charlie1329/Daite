package testingStuff;

import java.util.ArrayList;

import analysis.Analyser;
import data_structures.EvaluatedNode;
import data_structures.Question;

public class EvalNodeTest 
{
 public static void main(String[] args) 
 {
	Analyser analyser = new Analyser();
	String heldMessage = "Do you like memes?";
	String[] kWords = new String[3];
	kWords[0] = "memes";
	kWords[1] = "like";
	kWords[2] = "dank";
	ArrayList<String> neighbours = new ArrayList<String>();
	String incQuestion = "how do you like your eggs in the morning?";
	Question node = new Question(heldMessage, kWords, false, false, "test", analyser);
	System.out.println(node.evaluate(incQuestion));
			
			
	
 }
}
