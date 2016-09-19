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
	String heldMessage = "do you have any pets?";
	String[] kWords = new String[1];
	kWords[0] = "pets";
	//kWords[1] = "pet";
	ArrayList<String> neighbours = new ArrayList<String>();
	String incQuestion = "so then, do you have any pets?";
	Question node = new Question(heldMessage, kWords, false, false, "test", analyser);
	System.out.println(node.evaluate(incQuestion));
			
			
	
 }
}
