package data;

import java.util.concurrent.locks.ReentrantLock;


/**
 * Using for storage account variables
 * @author mav
 * @version 1.0
 *
 */
public class Account {

    /** Field for storage ID */
    private final int ID;
    /** Field for storage amount */
    private int amount;

    /** Final field for storage locker. */
    private final ReentrantLock locker = new ReentrantLock();

    /**
     * Constructor
     * @param ID - identifier of account
     * @param amount - first amount for account
     */
    public Account(int ID, int amount) {
        this.ID = ID;
        this.amount = amount;
    }

    /**
     * ID getter
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Amount getter
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Amount setter. NOT thread-safe.
     * @param amount - account amount for this moment
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Locker getter
     * @return locker
     */
    public ReentrantLock getLocker() {
        return locker;
    }

}
