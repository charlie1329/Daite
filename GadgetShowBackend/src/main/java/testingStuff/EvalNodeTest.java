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

	String heldMessage = "do you like cake?";
	String[] kWords = new String[2];
	kWords[0] = "cake";
	kWords[1] = "like";
	String incQuestion = "do you like cake?";
	String goodQuestion = "how much do you like cake?";
	String avgQuestion = "what is your opinion on cake?";
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
    
     heldMessage = "what do you think about the untimely death of cake?";
     kWords = new String[3];
     kWords[0] = "think";
     kWords[1] = "death";
     kWords[2] = "cake";
     incQuestion = "what do you think about the untimely death of cake?";
	 goodQuestion = "what do you think about cake death?";
     avgQuestion = "how did you feel about cake death?";
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
