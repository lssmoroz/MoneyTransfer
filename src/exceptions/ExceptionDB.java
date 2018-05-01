package exceptions;

/**
 * Using for generate exceptions when working with data. Extends of RuntimeException.
 * @author mav
 * @version 1.0
 *
 */
public class ExceptionDB extends RuntimeException {

    /** Exception reason. For getting description */
    private final int reason;

    /**
     * Reason getter.
     * @return reason
     */
    public int getReason() {
        return reason;
    }

    /**
     *
     * @param message - message for Runtime exception.
     * @param reason - exception reason
     */
    public ExceptionDB(String message, int reason) {
        super(message);

        this.reason = reason;
    }
}
