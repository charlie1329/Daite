package timing;

/**
 * Functional interface exposing a method to be called when the AI's response to
 * input should be sent back to the human.
 * 
 * @author Tom Galvin
 */
public interface LoadingDotCallback {
	public void setLoadingDotVisibility(boolean visible);
}
