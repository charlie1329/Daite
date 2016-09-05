package timing;

/**
 * Functional interface exposing a method to be called to hide or show the typing
 * ellipsis to indicate when the AI is "typing" a response.
 * 
 * @author Tom Galvin
 */
public interface WritingCompletedCallback {
	public void messageCompleted();
}
