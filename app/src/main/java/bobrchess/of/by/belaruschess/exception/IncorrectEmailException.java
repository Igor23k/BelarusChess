package bobrchess.of.by.belaruschess.exception;

/**
 * Created by Igor on 12.04.2018.
 */

public class IncorrectEmailException extends Exception {
    private static final long serialVersionUID = 1L;

    public IncorrectEmailException() {
    }

    public IncorrectEmailException(Exception e) {
        super(e);
    }

    public IncorrectEmailException(String message) {
        super(message);
    }

    public IncorrectEmailException(String message, Exception e) {
        super(message, e);
    }
}
