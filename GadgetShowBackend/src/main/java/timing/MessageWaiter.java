package timing;

/**this class is used to give clever ways of waiting for certain amounts of time
 * when sending a message, thus giving the appearance of making us look like a human
 * @author Charlie Street
 *
 */
public class MessageWaiter {

	private static final int CHARS_PER_MIN = 195;//Source for this value: 
	//http://smallbusiness.chron.com/good-typing-speed-per-minute-71789.html
	
	private static final int MILIS_PER_MIN = 60000;//miliseconds in a minute
	
	/**this method will take a message to be sent by our bot and
	 * state how long this message should take to type based on a
	 * typer of average speed
	 * @param msg the message to be sent by our bot
	 * @return how long an average person would take to type this message, in ms
	 */
	public static int howLongToType(String msg) {
		double charsOverExpected = msg.length()/CHARS_PER_MIN;
		int waitingTime = (int)Math.floor(charsOverExpected/MILIS_PER_MIN);
		return waitingTime;
	}
	
}
