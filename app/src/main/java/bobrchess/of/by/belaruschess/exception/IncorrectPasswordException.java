package bobrchess.of.by.belaruschess.exception;

/**
 * Created by Igor on 12.04.2018.
 */

public class IncorrectPasswordException extends Exception {
    private static final long serialVersionUID = 1L;

    public IncorrectPasswordException() {
    }

    public IncorrectPasswordException(Exception e) {
        super(e);
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(String message, Exception e) {
        super(message, e);
    }
}
