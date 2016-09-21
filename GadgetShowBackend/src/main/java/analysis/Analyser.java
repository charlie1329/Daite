package analysis;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;


public class Analyser {
	Properties props;
	StanfordCoreNLP pipeline;
	
	public Analyser()
	{
		props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,parse,sentiment,pos,lemma,depparse, ner, natlog,openie");
		pipeline = new StanfordCoreNLP(props);
	}
	
	public void analyse(String phrase)
	{
		Annotation doc = pipeline.process(bugreplacement(phrase));
		System.out.println("Phrase to be analysed: " + phrase);
		findSentiment(doc);
		getRelations(phrase);
	}
	
	private int findSentiment(Annotation doc)
	{
		int mainSentiment = 0;
		int longest = 0;
		for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class))
		{
			Tree tree = sentence
                    .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = sentiment;
                longest = partText.length();
            }
		}
		//System.out.println("Sentiment score = " + mainSentiment);
		return mainSentiment;
	}
	
	public int compareSentiment(String one, String two)
	{
		Annotation doc1 = pipeline.process(bugreplacement(one));
		Annotation doc2 = pipeline.process(bugreplacement(two));
		int i = Math.abs(findSentiment(doc1) - findSentiment(doc2));
		if(i == 0)
			return 2;
		if(i == 1)
			return 1;
		if (i == 2)
			return 0;
		return 0;
	}
	
	public double compareInformation(RelationTriple one, RelationTriple two)
	{
		try
		{
			double conf = one.confidence+two.confidence;
			double sub = 0;
			if(one.subjectLemmaGloss().equals(two.subjectLemmaGloss()))
					sub = 0.75;
			double obj = 0;
			if(one.objectLemmaGloss().equals(two.objectLemmaGloss()))
					obj = 1;
			double rel = 0;
			if(one.relationLemmaGloss().equals(two.relationLemmaGloss()))
				rel = 0.75;
			return conf*(sub+obj+rel);
		}
		catch(NullPointerException e)
		{
			return 0;
		}
				
	}
	
	private String bugreplacement(String toChange)
	{
		toChange = toChange.replaceAll("like", "liked");
		toChange = toChange.replaceAll("have", "had");
		toChange = toChange.replaceAll("own", "had");
		toChange = toChange.replaceAll("love", "loved");
		toChange = toChange.replaceAll("live", "lived");
		toChange = toChange.replaceAll("hate", "hated");
		toChange = toChange.replaceAll("play", "played");
		toChange = toChange.replaceAll("watch", "watched");
		toChange = toChange.replaceAll("listen", "listened");
		toChange = toChange.replaceAll("are", "were");
		
		return toChange;
	}
	public RelationTriple getRelations(String phrase)
	{
		Annotation doc = pipeline.process(bugreplacement(phrase));
		for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {

		      // Get the OpenIE triples for the sentence
		      Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
		      // Print the triples
		     /**
		      for (RelationTriple triple : triples) {
		        System.out.println(triple.confidence + "\t" +
		            triple.subjectLemmaGloss() + "\t" +
		            triple.relationLemmaGloss() + "\t" +
		            triple.objectLemmaGloss());
		      }
		     */
		      Iterator<RelationTriple> itr = triples.iterator();
		      
		      try
		      {
		    	  RelationTriple last = itr.next();
		    	  while(itr.hasNext()) {
		          last=itr.next();
		    	  }
		    	  System.out.println(last.confidence + "\t" +
		    	            last.subjectLemmaGloss() + "\t" +
		    	            last.relationLemmaGloss() + "\t" +
		    	            last.objectLemmaGloss());
		    	  return last;
		    	  
		      }
		      catch(NoSuchElementException e)
		      {
		    	  return null;
		      }
		      
		      
		}
		return null;
	}
	
  public static void main(String[] args) throws Exception {
    
	Analyser anal = new Analyser();
	double i = anal.compareInformation(anal.getRelations("iasdsadasdasdasdsaddl"), anal.getRelations("i quite like football"));
	 System.out.println(i);
  }
}