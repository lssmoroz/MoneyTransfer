package source;

/**
 * Interface for working with Accounts. Now realised only working minimum.
 * @author mav
 * @version 1.0
 *
 */
public interface AccountService {

    /**
     * Method for adding accounts in system
     * @param ID - identifier
     * @param amount - start amount
     * @return error code
     */
    public int addAccount(int ID, int amount);

    /**
     * Method that transfer money
     * @param fromID - Account ID from
     * @param toID Account ID to
     * @param amount - amount of transfer
     * @return error code
     */
    public int transferMoney(int fromID, int toID, int amount);

    /**
     * Method for getting amount from account
     * @param ID - account ID for get ammount
     * @return amount. If return value is "-10" - it means no account
     */
    public int getAmount(int ID);

    /**
     * Method for getting exception message for user
     * @param code - code that you need to know.
     * @return error message for user
     */
    public String getErrorReason(int code);

}
