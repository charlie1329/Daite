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
	String heldMessage = "bye bye for now";
	String[] kWords = new String[1];
	kWords[0] = "bye";
	//kWords[1] = "you";
	//kWords[2] = "memes";
	//kWords[1] = "pet";
	ArrayList<String> neighbours = new ArrayList<String>();
	String incQuestion = "i just wanted to fill this table up tbh";
	Question node = new Question(heldMessage, kWords, false, false, "test", analyser);
	System.out.println("Score: " + node.evaluate(incQuestion));
			
			
	
 }
}
