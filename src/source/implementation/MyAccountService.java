package source.implementation;

import data.Account;
import exceptions.ExceptionDB;
import data.AccountsDB;
import log.Logger;
import source.AccountService;

import java.util.HashMap;
import java.util.Map;

/**
 * Using for implement AccountService. Thread-safe.
 * @author mav
 * @version 1.0
 *
 */
public class MyAccountService implements AccountService {

    /** Accounts storage */
    private AccountsDB accounts = new AccountsDB();

    /** Logger */
    private Logger logger;

    /**
     * Reasons for return to user:
     * 0 - all is OK!
     * 1 - already have such account
     * 2 - don't have FROM acc
     * 3 - don't have TO acc
     * 4 - don't have money
     * 5 - same account
     * -10 - don't have acc for amount get
     * Another <0 - system errors
     * */
    protected Map<Integer, String> reasons;

    /**
     * Constructor. Fill reasons inside
     * @param logger - logger
     */
    public MyAccountService(Logger logger) {
        this.logger = logger;

        reasons = new HashMap<>();

        reasons.put(0, "Operation done!");

        reasons.put(1, "Already have such ID in accounts!");
        reasons.put(2, "Don't have account FROM!");
        reasons.put(3, "Don't have account TO!");
        reasons.put(4, "Don't have enough money at this account!");
        reasons.put(5, "Bad idea transfer money to the same account!");

        reasons.put(-1, "Failure operation to add account, please contact system administrator! Reason=" + "1");
        reasons.put(-2, "Failure operation to add account, please contact system administrator! Reason=" + "2");
        reasons.put(-3, "Failure operation to transfer money, please contact system administrator! Reason=" + "3");

        reasons.put(-10, "Don't have account for amount get!");

    }




    /**
     * Override for AccountService
     * @param ID - identifier
     * @param amount - start amount
     * @return error code
     */
    @Override
    public int addAccount(int ID, int amount) {
        try {
            Account accForAdd = new Account(ID, amount);

            try {
                accounts.addAccount(accForAdd);
            } catch (ExceptionDB ex) {
                if (ex.getReason() == 1) {
                    return 1;
                } else {
                    if (null != logger) {
                        logger.log(ex);
                    } else {
                        System.out.println("Loger is NULL!");
                        System.out.println(ex);
                    }
                    return -1;
                }
            }

            return 0;
        } catch (Exception ex) {
            if (null != logger) {
                logger.log(ex);
            } else {
                System.out.println("Loger is NULL!");
                System.out.println(ex);
            }
            return -2;
        }
    }

    /**
     * Override for AccountService
     * @param fromID - Account ID from
     * @param toID Account ID to
     * @param amount - amount of transfer
     * @return error code
     */
    @Override
    public int transferMoney(int fromID, int toID, int amount) {

        try {

            if (fromID == toID) {
                return 5;
            }

            Account accFrom = accounts.getAccount(fromID);

            if (null == accFrom) {
                return 2;
            }

            if (accFrom.getAmount() < amount) {
                return 4;
            }

            Account accTo = accounts.getAccount(toID);

            if (null == accTo) {
                return 3;
            }

            if (accFrom.getID() < accTo.getID()) {
                accFrom.getLocker().lock();
                accTo.getLocker().lock();
            } else {
                accTo.getLocker().lock();
                accFrom.getLocker().lock();
            }

            /*
             * Someone was faster then we...
             */
            if (accFrom.getAmount() < amount) {
                accFrom.getLocker().unlock();
                accTo.getLocker().unlock();
                return 4;
            }

            accFrom.setAmount(accFrom.getAmount() - amount);
            accTo.setAmount(accTo.getAmount() + amount);

            accFrom.getLocker().unlock();
            accTo.getLocker().unlock();

            return 0;

        } catch (Exception ex) {
            if (null != logger) {
                logger.log(ex);
            } else {
                System.out.println("Loger is NULL!");
                System.out.println(ex);
            }
            return -3;
        }

    }

    /**
     * Override for AccountService
     * @param ID - account ID for get ammount
     * @return amount. If return value is "-10" - it means no account
     */
    @Override
    public int getAmount(int ID) {
        Account accountNow = accounts.getAccount(ID);
        if (null == accountNow) {
            return -10;
        }

        return accountNow.getAmount();
    }

    /**
     * Override for AccountService
     * @param code - code that you need to know.
     * @return error message for user
     */
    @Override
    public String getErrorReason(int code) {
        if (reasons.containsKey(code)) {
            return reasons.get(code);
        }

        return "Unknown Error. If you get it, please contact system administrator.";

    }

}
