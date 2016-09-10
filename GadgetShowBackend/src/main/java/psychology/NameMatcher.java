package psychology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class NameMatcher
{
	private static boolean M = true;
	private static boolean F = false;
	
	private ArrayList<String> MaleNames;
	private String maleLoc = "miscFiles/names/MaleNames.txt";

	private ArrayList<String> FemaleNames;
	private String femLoc = "miscFiles/names/FemaleNames.txt";

	public NameMatcher()
	{
		MaleNames = readNames(maleLoc);
		FemaleNames = readNames(femLoc);
	}

	/**
	 * Given the name of a partner, generate a name that they should find
	 * attractive
	 * 
	 * @param partner The name of the partner
	 * @param male True if *GENERATED* name should be male, false if female.
	 * This is not sexist.
	 */
	public String pickName(String partner, boolean male)
	{

		/*
		 * Attractive male names:
		 * 1-3 syllables
		 * Small vowel sounds
		 * Lev. D. of 7 from female partner's name
		 * Bonus points if same initial but Lev. D. > 2
		 * e.g. Jenny and Joseph
		 */
		if (male)
		{
			return pickName(partner, 7, MaleNames);
		}
		/*
		 * Attractive female names:
		 * 2-4 syllables
		 * Round-sounding
		 * Lev. D. of 5 from female partner's name
		 * Bonus points if same initial but Lev. D. > 2
		 * e.g. Jenny and Joseph
		 */
		else
		{
			return pickName(partner, 5, FemaleNames);
		}

	}

	private String pickName(String partner, int levD, ArrayList<String> names)
	{
		HashMap<String, Integer> levTable = new HashMap<String, Integer>();

		for (String name : names)
		{
			levTable.put(name, lev(partner, name));
			//System.out.println("distance(" + partner + ", " + name + ") = " + lev(partner, name));
		}

		// Find the names with the Levenshtien distance closest to the target
		int target1 = levD;
		int target2 = levD;
		ArrayList<String> shortlist = new ArrayList<String>();

		Set<Entry<String, Integer>> set = levTable.entrySet();
		do
		{
			for (Entry<String, Integer> entry : set)
			{
				if (entry.getValue().equals(target1) || entry.getValue().equals(target2))
				{
					shortlist.add(entry.getKey());
					//System.out.println(target1 +" "+ target2);
				}
			}
			target1++;
			target2--;
		}
		while (shortlist.size() == 0); // TODO check

		// Selecting the best name from the Levenshtien shortlist
		if (shortlist.size() == 1)
		{
			System.out.println("Only one choice:");
			return shortlist.get(0);
		}
		else
		{
			for (String name : shortlist)
			{
				if (name.charAt(0) == partner.charAt(0))
				{
					System.out.println("This one starts with the same letter:");
					return name;
				}
			}
			System.out.println("This one will do, I guess:");
			return shortlist.get(new Random().nextInt(shortlist.size()));
		}
	}

	/**
	 * @param a
	 * @param b
	 * @return The Levenshtien Distance between two names, a and b
	 */
	private static int lev(String a, String b)
	{
		a = a.toLowerCase();
		b = b.toLowerCase();
		// i == 0
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
		{
			costs[j] = j;
		}
		for (int i = 1; i <= a.length(); i++)
		{
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++)
			{
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	private ArrayList<String> readNames(String filename)
	{
		String line = "";
		String comment = "#";
		ArrayList<String> lines = new ArrayList<String>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));

			while ((line = br.readLine()) != null)
			{
				if (!line.startsWith(comment))
				{
					lines.add(line);
				}
			}
			return lines;
		}
		catch (IOException e)
		{
			System.out.println("Couldn't find file " + filename);
			return null;
		}

	}

	public static void main(String[] args)
	{
		NameMatcher nm = new NameMatcher();
		System.out.println(nm.pickName("Oliver", F)); // M to get male name, F to get female
	}
}
