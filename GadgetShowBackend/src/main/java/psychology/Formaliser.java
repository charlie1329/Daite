package psychology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Formaliser
{
	private String cfilename = "miscFiles/contractions/contractions.csv";
	private String efilename = "miscFiles/contractions/emoticons.csv";
	private HashMap<String, String[]> conts;
	private HashMap<String, String[]> emoticons;
	
	public static void main(String[] args)
	{
		Formaliser f = new Formaliser();
		
		// TODO if this has a choice it currently picks a random one cos anything else requires complex language parsing
		System.out.println(f.expand("u cant do 2moro cos ur 2 fat. ily, omg thx bb"));
	}
	
	public Formaliser()
	{
		conts = CSVtoMap.readContents(cfilename);
		emoticons = CSVtoMap.readContents(efilename);
	}
	
	public String expand(String phrase)
	{
		String[] words = phrase.split(" ");
		
		Random r = new Random();
		
		for (int i = 0; i < words.length; i++)
		{	
			String clean_emoji = words[i];
			if (clean_emoji.endsWith(".") || clean_emoji.endsWith("?") || clean_emoji.endsWith("!"))
			{
				clean_emoji = clean_emoji.substring(0, clean_emoji.length()-1);
			}
			
			if (emoticons.containsKey(clean_emoji))
			{
				words[i] = emoticons.get(clean_emoji)[0];
			}
			else
			{
				String clean = words[i].toLowerCase();
				clean = clean.replaceAll("[^a-zA-Z0-9]", "");
				String[] longhand = conts.get(clean);
				
				if (conts.containsKey(clean))
				{
					
					words[i] = longhand[r.nextInt(longhand.length)]; // or 0
				}
			}
		}
		
		return insertSpaces(words);
	}
	
	private String insertSpaces(String[] arr)
	{
		// capitalise the first letter of the sentence (not necessary)
		//arr[0] = arr[0].substring(0, 1).toUpperCase() + arr[0].substring(1);
		
		String result = "";
		
		for (int i = 0; i < arr.length; i++)
		{
			result += arr[i] + " ";
		}
		
		return result.trim();
	}
}

class CSVtoMap
{
	private static BufferedReader br;
	private static String line = "";
	private static String cvsSplitBy = ",";
	private static String comment = "#";

	public static HashMap<String, String[]> readContents(String filename)
	{
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		try
		{
			br = new BufferedReader(new FileReader(filename));

			while ((line = br.readLine()) != null)
			{
				if (!line.startsWith(comment))
				{
					// use comma as separator
					String[] items = line.split(cvsSplitBy);
					String[] tail = new String[items.length-1];
					for (int i = 1; i < items.length; i++)
					{
						tail[i-1] = items[i].trim();
						
					}
					data.put(items[0], tail);
				}
			}
			return data;
		}
		catch (IOException e)
		{
			System.out.println("Couldn't find file " + filename);
			return null;
		}
	}
}

