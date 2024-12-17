package exception;

public class LimitLoginException extends Exception {

    static String errorMessage = "Error. Attempt limit exceeded.";

    public LimitLoginException() {
        super(errorMessage);
    }

}
