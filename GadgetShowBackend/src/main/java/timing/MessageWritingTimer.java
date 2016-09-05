package timing;

/**
 * Provides methods to control the timing of the simulated typing of a response
 * message, and also of showing and hiding the typing ellipsis, according to some
 * algorithm to make it look realistic.
 * 
 * @author Tom Galvin
 */
public interface MessageWritingTimer {
	/**
	 * <p>
	 * Begins the process of automating the fake "typing" of a response by the
	 * AI. The implementation will call {@code loadingDotCallback} any number of
	 * times, on a new thread, to show and hide the typing dots at different times
	 * as appropriate. Eventually {@code messageCompletedCallback} will be called
	 * (from the same thread) to indicate that the AI has finished typing a response
	 * and that the message should be sent and displayed to the user - ie. when the
	 * AI presses their "send button".
	 * </p>
	 * <p>
	 * The reason for implementing this asynchronously is to allow the AI to perform
	 * more complex sequences of stopping and starting. For example, if the user
	 * presents the AI with a deep meaningful question, we might want to give the
	 * impression that the AI is mulling over a response. To simulate this, we might
	 * pretend that the AI starts typing a response, waits a second, deletes it, and
	 * then starts typing a new response. This just an added touch of realism.
	 * </p>
	 * <p>
	 * Once {@code messageCompletedCallback} has been called, {@code loadingDotCallback}
	 * will not be called again. The implementation will always call {@code loadingDotCallback}
	 * to <b>hide</b> the loading dots before calling {@code messageCompletedCallback}.
	 * Each call to {@link MessageWritingTimer#beginTyping(String, LoadingDotCallback,
	 * WritingCompletedCallback) beginTyping}
	 * will spawn a new thread, and all calls to the callbacks passed to that method
	 * call will be called from the same thread.
	 * </p>
	 * 
	 * @param message The message the AI is typing as a response.
	 * @param loadingDotCallback A callback accepting a boolean, to either show ({@code true})
	 * or hide ({@code false}) the loading dots.
	 * @param messageCompletedCallback A callback accepting no parameters, which is
	 * called when the AI has finished "typing" and the response should be sent to the
	 * user.
	 */
	public void beginTyping(
			String message,
			LoadingDotCallback loadingDotCallback,
			WritingCompletedCallback messageCompletedCallback);
}
