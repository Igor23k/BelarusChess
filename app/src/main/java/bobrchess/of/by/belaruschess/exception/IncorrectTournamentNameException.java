package bobrchess.of.by.belaruschess.exception;

/**
 * Created by Igor on 12.04.2018.
 */

public class IncorrectTournamentNameException extends Exception {
    private static final long serialVersionUID = 1L;

    public IncorrectTournamentNameException() {
    }

    public IncorrectTournamentNameException(Exception e) {
        super(e);
    }

    public IncorrectTournamentNameException(String message) {
        super(message);
    }

    public IncorrectTournamentNameException(String message, Exception e) {
        super(message, e);
    }
}
