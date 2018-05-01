package log;

/**
 * Using for log all information
 * default implementation uses for log in System.out
 * @author mav
 * @version 1.0
 *
 */
public interface Logger {

    /**
     * Log exception
     * @param ex - exception that we need to log
     */
    default public void log(Exception ex) {
        System.out.println(ex);
    }

    /**
     * Log message
     * @param mes - message that we need to log
     */
    default public void log(String mes) {
        System.out.println(mes);
    }
}
