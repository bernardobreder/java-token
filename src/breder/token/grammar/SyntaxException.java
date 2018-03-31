package breder.token.grammar;

public class SyntaxException extends Exception {
	
	/**
	 * @param message
	 * @param cause
	 */
	public SyntaxException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message
	 */
	public SyntaxException(String message) {
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public SyntaxException(Throwable cause) {
		super(cause);
	}
}
