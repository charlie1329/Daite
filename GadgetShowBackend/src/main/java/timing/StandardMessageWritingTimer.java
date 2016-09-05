package timing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An implementation of {@link MessageWritingTimer} to simulate the typing of a response
 * in a convincing manner.
 * 
 * @author Tom Galvin
 */
public class StandardMessageWritingTimer implements MessageWritingTimer {
	/**
	 * The maximum amount of time that the thread shout ever pretend to wait for.
	 * Just so a mistake in the code doesn't lock up the AI and server.
	 */
	public static final float MAX_WAIT_DURATION = 12f;
	private static Random r = new Random();
	
	@Override
	public void beginTyping(String message, LoadingDotCallback loadingDotCallback,
			WritingCompletedCallback messageCompletedCallback) {
		new Thread(() -> {
			try {
				List<Float> thoughtTimes = getThoughtTimes(message);
				
				for(Float thoughtTime : thoughtTimes) {
					wait(thoughtTime);
					wait(1f + r.nextFloat() * 2.2f);  // AI is "backspacing" response
				}
				
				wait(getTypingTime(message));
			} catch(InterruptedException e) {
				System.out.println(
						"Fake typing thread interrupted for some " +
						"reason.. probably shouldn't have happened");
				e.printStackTrace();
			} finally {
				messageCompletedCallback.messageCompleted();
			}
		}).start();
	}
	
	/**
	 * Waits the specified duration. This blocks the thread.
	 * 
	 * @param duration The duration to wait for, in seconds. If this is larger than
	 * {@link StandardMessageWritingTimer#MAX_WAIT_DURATION} then the wait time will
	 * be capped to that.
	 * 
	 * @throws InterruptedException
	 */
	protected void wait(float duration) throws InterruptedException {
		long durationMillis = (long)(Math.min(duration, MAX_WAIT_DURATION) * 1000f);
		
		if(durationMillis > 0) {
			Thread.sleep(durationMillis);
		}
	}
	
	/**
	 * <p>
	 * Gets a value, in seconds, for which the AI should spend typing a response.
	 * </p>
	 * 
	 * @param message The message to get the typing time for.
	 * @return A time, in seconds, for how long the AI should pretend to type for.
	 */
	protected Float getTypingTime(String message) {
		return (message.length() * 0.25f + 1.7f)
				* (r.nextFloat() * 0.1f + 0.95f);
	}
	
	/**
	 * <p>
	 * Gets a list of {@link Float}s, where each value in the list represents a value
	 * in seconds for which the typing AI should (roughly) think for while typing
	 * the message. For example, if the user presents a fairly serious deep question
	 * to the AI which gives the AI two subjects to think about, then this will
	 * return two values - one number for the (rough) length of time for which the AI
	 * should pretend to "think" about each question for.
	 * </p>
	 * <p>
	 * All times are in seconds.
	 * </p>
	 * <p>
	 * This function can also return an empty list if the user's input provokes no deep
	 * thoughts for the AI - for example "where do you live?" is straightforward and
	 * the AI won't need to "think" about it. However, this function will never return
	 * {@code null}.
	 * </p>
	 * 
	 * @param message The message to get the thought times for.
	 * @return A list of positive real numbers as described above.
	 */
	protected List<Float> getThoughtTimes(String message) {
		return new ArrayList<Float>();
	}
}
