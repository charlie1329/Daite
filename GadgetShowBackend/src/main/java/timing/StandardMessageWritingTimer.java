package timing;

import java.util.Random;

/**
 * An implementation of {@link MessageWritingTimer} to simulate the typing of a
 * response in a convincing manner.
 * 
 * @author Tom Galvin
 */
public class StandardMessageWritingTimer implements MessageWritingTimer {
	/**
	 * The maximum amount of time that the thread shout ever pretend to wait
	 * for. Just so a mistake in the code doesn't lock up the AI and server.
	 */
	public static final float MAX_WAIT_DURATION = 12f;
	private static Random r = new Random();

	@Override
	public void beginTyping(String[] messagesRead, String[] messagesToWrite, TypingDotsVisibilityCallback typingDotsCallback,
			WritingCompletedCallback messageCompletedCallback) {
		final TypingDotsVisibilityCallback typingDotsCallbackToUse =
				typingDotsCallback == null ? v -> {} : typingDotsCallback;
		new Thread(() -> {
			simulateReadMessages(messagesRead, typingDotsCallbackToUse);
			simulateWriteResponses(messagesToWrite, typingDotsCallbackToUse, messageCompletedCallback);
		}).start();
	}

	/**
	 * Simulate the reading of a set of messages sent by a human.
	 * 
	 * @param messagesRead The list of messages to pretend to read.
	 * @param typingDotsCallback The callback for setting the visibility of the typing dots.
	 */
	protected void simulateReadMessages(String[] messagesRead, TypingDotsVisibilityCallback typingDotsCallback) {
		boolean dotsAreVisible = false; // for safety in case thread is interrupted - return to original state
		try {
			for (String messageRead : messagesRead) {
				float readingTime = getReadingTime(messageRead);
				wait(readingTime);

				if (r.nextDouble() < 0.13) {
					dotsAreVisible = true;
					typingDotsCallback.setTypingDotsVisibility(true);
					wait(1.1f + 1.6f * r.nextFloat());
					dotsAreVisible = false;
					typingDotsCallback.setTypingDotsVisibility(false);
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Fake typing thread interrupted during pretend reading "
					+ "for some reason.. probably shouldn't have happened");
			e.printStackTrace();
		} finally {
			if(dotsAreVisible) {
				dotsAreVisible = false;
				typingDotsCallback.setTypingDotsVisibility(false);
			}
		}
	}

	/**
	 * Simulate the writing of a set of messages to send to the human.
	 * 
	 * @param messagesToWrite The list of messages to pretend to write.
	 * @param typingDotsCallback The callback for setting the visibility of the typing dots.
	 * @param messageCompletedCallback The callback for sending a message to the user.
	 */
	protected void simulateWriteResponses(String[] messagesToWrite, TypingDotsVisibilityCallback typingDotsCallback,
			WritingCompletedCallback messageCompletedCallback) {
		for (String messageToWrite : messagesToWrite) {
			simulateWriteResponse(messageToWrite, typingDotsCallback, messageCompletedCallback);
		}
	}

	/**
	 * Simulate the writing of a message to send to the human.
	 * 
	 * @param messageToWrite The message to pretend to write.
	 * @param typingDotsCallback The callback for setting the visibility of the typing dots.
	 * @param messageCompletedCallback The callback for sending a message to the user.
	 */
	protected void simulateWriteResponse(String messageToWrite, TypingDotsVisibilityCallback typingDotsCallback,
			WritingCompletedCallback messageCompletedCallback) {
		boolean dotsAreVisible = false; // for safety in case thread is interrupted - return to original state
		try {
			float thinkingTime = 0.41f + r.nextFloat() * 2.23f;
			wait(thinkingTime);
			
			dotsAreVisible = true;
			typingDotsCallback.setTypingDotsVisibility(true);
			
			wait(getTypingTime(messageToWrite));
			
			dotsAreVisible = false;
			typingDotsCallback.setTypingDotsVisibility(false);
			
			wait(0.1f); // so client gets gap between loading-dots-off and message itself
		} catch (InterruptedException e) {
			System.out.println("Fake typing thread interrupted during pretend writing "
					+ "for some reason.. probably shouldn't have happened");
			e.printStackTrace();
		} finally {
			if(dotsAreVisible) {
				dotsAreVisible = false;
				typingDotsCallback.setTypingDotsVisibility(false);
			}
			messageCompletedCallback.messageCompleted(messageToWrite);
		}
	}

	/**
	 * Waits the specified duration. This blocks the thread.
	 * 
	 * @param duration
	 *            The duration to wait for, in seconds. If this is larger than
	 *            {@link StandardMessageWritingTimer#MAX_WAIT_DURATION} then the
	 *            wait time will be capped to that.
	 * 
	 * @throws InterruptedException
	 */
	protected void wait(float duration) throws InterruptedException {
		long durationMillis = (long) (Math.min(duration, MAX_WAIT_DURATION) * 1000f);

		if (durationMillis > 0) {
			Thread.sleep(durationMillis);
		}
	}

	/**
	 * <p>
	 * Gets a value, in seconds, for which the AI should spend reading a user's
	 * input.
	 * </p>
	 * 
	 * @param message
	 *            The message to get the reading time for.
	 * @return A time, in seconds, for how long the AI should pretend to read
	 *         for.
	 */
	protected Float getReadingTime(String message) {
		return ((float) message.split(" ").length * 0.15f + 0.7f) * (r.nextFloat() * 0.3f + 0.85f);
	}

	/**
	 * <p>
	 * Gets a value, in seconds, for which the AI should spend typing a
	 * response.
	 * </p>
	 * 
	 * @param message
	 *            The message to get the typing time for.
	 * @return A time, in seconds, for how long the AI should pretend to type
	 *         for.
	 */
	protected Float getTypingTime(String message) {
		return (message.length() * 0.15f + 1.4f) * (r.nextFloat() * 0.3f + 0.85f);
	}
}
