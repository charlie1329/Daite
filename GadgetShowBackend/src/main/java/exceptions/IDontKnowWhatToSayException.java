package exceptions;

/**Exception class for when we may not know what to say during conversation
 * through the use of this conversation we can move back up levels of try catch to reach our final fail-safe tactic
 * whatever that may be
 * @author Charlie Street
 *
 */
public class IDontKnowWhatToSayException extends Exception {

	private static final long serialVersionUID = 1L;

	/**constructor extends from super constructor
	 * 
	 * @param message exception message
	 */
	public IDontKnowWhatToSayException(String message) {
		super(message);
	}
}
