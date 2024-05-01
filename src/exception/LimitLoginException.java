package exception;

public class LimitLoginException extends Exception {
	
	static String errorMessage = "Error";

	public LimitLoginException() {
		super(errorMessage);
	}
	
}
