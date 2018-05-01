package data;

import exceptions.ExceptionDB;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Using for storage accounts. Thread-safe.
 * @author mav
 * @version 1.0
 *
 */
public class AccountsDB {

    /*
     * For real situation we need to use some special tools for storage data in memory.
     * But add something like hazelcast here is not a good idea for project simplicity.
     */
    /** Field for accounts */
    private final Map<Integer, Account> accounts = new HashMap<>();
    /** Field for storage locker. Using for adding account. */
    private ReentrantLock addAccountLocker = new ReentrantLock();

    /**
     * Method for adding account. Thread-safe.
     * @param accForAdd - account for add.
     * @throws ExceptionDB - exception when we have trouble with data
     */
    public void addAccount(Account accForAdd) {

        if (null == accForAdd) {
            throw new ExceptionDB("Can't add null account!", 0);
        }

        addAccountLocker.lock();
        try {
            if (accounts.containsKey(accForAdd.getID())) {
                throw new ExceptionDB("We already have account with this ID = " + Integer.toString(accForAdd.getID()) + "!", 1);
            }
        } finally {
            addAccountLocker.unlock();
        }

        accounts.put(accForAdd.getID(), accForAdd);
    }

    public Account getAccount(int ID) {
        if (accounts.containsKey(ID)) {
            return accounts.get(ID);
        }

        return null;
    }
}
