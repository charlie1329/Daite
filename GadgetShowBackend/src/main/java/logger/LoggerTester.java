package logger;

/**quick class to test logger
 * 
 * @author Charlie Street
 *
 */
public class LoggerTester {

	public static void main(String[] args) {
		ConvoLogger logger = new ConvoLogger();
		logger.logMessage("Bot: Hi, how are you?");
		logger.logMessage("Charlie: Good thanks :)");
	}
}
