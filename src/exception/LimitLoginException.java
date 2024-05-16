package exception;

public class LimitLoginException extends Exception {

	static String errorMessage = "Error. Intentos superados.";

	public LimitLoginException() {
		super(errorMessage);
	}

}
