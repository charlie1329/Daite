package analysis;

/**this class deals with all methods involving 
 * looking at likelihoods of messages being the same
 * based on finding keywords in the user entered message
 * @author Charlie Street
 *
 */
public class KeywordMagic {

	/**this method will return the proportion of keywords in keys
	 * that are present in message
	 * static because I need to use only this method in various locations
	 * there is no internal state so static is justified here
	 * @param keys the keywords to look through
	 * @param message the message to search through
	 * @return the proportion present in message
	 */
	public static double correctKeyWords(String[] keys, String message) {
		double total = keys.length;
		double correct = 0.0;
		
		for(int i = 0; i < keys.length; i++) {
			if(message.toLowerCase().contains(keys[i].toLowerCase())) {
				correct++;
			}
		}
		
		return (double)(correct);
	}
}
