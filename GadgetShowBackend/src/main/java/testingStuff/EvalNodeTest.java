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

     public static testqs(String h, String q1, String q2, String q3, String q4, String q5, String[] kwords) {
         Question node = new Question(h, kwords, false, false, "test", analyser);
         System.out.println("Score: " + node.evaluate(badQuestion));
         System.out.println("Score: " + node.evaluate(mehQuestion));
         System.out.println("Score: " + node.evaluate(avgQuestion));
         System.out.println("Score: " + node.evaluate(goodQuestion));
         System.out.println("Score: " + node.evaluate(incQuestion));
 }

	String heldMessage = "do you like memes?";
	String[] kWords = ["memes", "like"];
	String incQuestion = "do you like memes?";
	String goodQuestion = "how much do you like memes?";
	String avgQuestion = "what is your opinion on memes?";
	String mehQuestion = "do you like hitler?";
	String badQuestion = "how is your pet rhinocerous this fine morning?";
	testqs(heldMessage,badQuestion,mehQuestion,avgQuestion,goodQuestion,incQuestion, kWords);

     heldMessage = "what do you think about the untimely death of harambe?";
     kWords = ["think", "death", "harambe"];
     incQuestion = "what do you think about the untimely death of harambe?";
	 goodQuestion = "what do you think about harambe's death?";
     avgQuestion = "how did you feel about harambe's death";
     mehQuestion = "do you think about death a lot?";

     testqs(heldMessage,badQuestion,mehQuestion,avgQuestion,goodQuestion,incQuestion, kWords);
 }
}
