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
	String[] kWords = new String[2];
	kWords[0] = "memes";
	kWords[1] = "like";
	ArrayList<String> neighbours = new ArrayList<String>();
	String incQuestion = "do you like memes?";
	Question node = new Question(heldMessage, kWords, false, false, "test", analyser);
	System.out.println("Score: " + node.evaluate(incQuestion));
			
			
	
 }
 
 
}
