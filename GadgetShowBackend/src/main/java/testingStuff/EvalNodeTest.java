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

	String heldMessage = "do you like memes?";
	String[] kWords = new String[2];
	kWords[0] = "memes";
	kWords[1] = "like";
	String incQuestion = "do you like memes?";
	String goodQuestion = "how much do you like memes?";
	String avgQuestion = "what is your opinion on memes?";
	String mehQuestion = "do you like hitler?";
	String badQuestion = "how is your pet rhinocerous this fine morning?";
    Question node = new Question(heldMessage, kWords, false, false, "test", analyser);
    System.out.println("Actual Question: " + heldMessage);
    System.out.println("Question: " + badQuestion + " | Score: " + node.evaluate(badQuestion));
    System.out.println("Question: " + mehQuestion + " | Score: " + node.evaluate(mehQuestion));
    System.out.println("Question: " + avgQuestion + " | Score: " + node.evaluate(avgQuestion));
    System.out.println("Question: " + goodQuestion + " | Score: " + node.evaluate(goodQuestion));
    System.out.println("Question: " + incQuestion + " | Score: " + node.evaluate(incQuestion));

    System.out.println("--------------------------------------------------");
    
     heldMessage = "what do you think about the untimely death of harambe?";
     kWords = new String[3];
     kWords[0] = "think";
     kWords[1] = "death";
     kWords[2] = "harambe";
     incQuestion = "what do you think about the untimely death of harambe?";
	 goodQuestion = "what do you think about harambe death?";
     avgQuestion = "how did you feel about harambe death?";
     mehQuestion = "do you think about death a lot?";

     node = new Question(heldMessage, kWords, false, false, "test", analyser);
     System.out.println("Actual Question: " + heldMessage);
     System.out.println("Question: " + badQuestion + " | Score: " + node.evaluate(badQuestion));
     System.out.println("Question: " + mehQuestion + " | Score: " + node.evaluate(mehQuestion));
     System.out.println("Question: " + avgQuestion + " | Score: " + node.evaluate(avgQuestion));
     System.out.println("Question: " + goodQuestion + " | Score: " + node.evaluate(goodQuestion));
     System.out.println("Question: " + incQuestion + " | Score: " + node.evaluate(incQuestion));
 }
}
