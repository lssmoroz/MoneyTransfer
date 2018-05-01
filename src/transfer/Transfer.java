package transfer;

import com.sun.xml.internal.bind.v2.model.core.ID;
import data.Account;
import exceptions.ExceptionDB;
import log.Logger;
import source.AccountService;
import source.implementation.MyAccountService;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

/**
 * Start working with AccountService interface. Now realised only working minimum.
 * @author mav
 * @version 1.0
 *
 */
public class Transfer {

    /**
     * For launch this program we can set next arguments
     * 1) Number of Accounts that we will use for test
     * 2) Start amount for all accounts
     * 3) Number of threads that we will use for test
     * 4) Number of transaction for one thread
     * 5) Need logging or want to test speed
     * Example for using all args: java testPrj accCount 10 startAmount 500 threadCount 10 transferCount 100 maxTransferAmount 1000 needLogging true
     * All args that you don't indicate will use as default (as in example)
     * @param args - arguments
     */

    public static void main(String[] args) {

        //Default args
        int accountsCount = 10;
        int startAmount = 500;
        int threadCount = 10;
        int transferCount = 100;
        int maxTransferAmount = 1000;
        boolean needLogging = true;

        //Parse input args
        for (int i=0;i<args.length;++i) {

            String argNow = args[i];
            if (args.length < i+1) {
                break;
            }

            int tmp;
            try {
                if (argNow.equalsIgnoreCase("accCount")) {
                    tmp = Integer.parseInt(args[++i]);
                    if (tmp > 0) {
                        accountsCount = tmp;
                    }
                } else if (argNow.equalsIgnoreCase("startAmount")) {
                    tmp = Integer.parseInt(args[++i]);
                    if (tmp > 0) {
                        startAmount = tmp;
                    }
                } else if (argNow.equalsIgnoreCase("threadCount")) {
                    tmp = Integer.parseInt(args[++i]);
                    if (tmp > 0) {
                        threadCount = tmp;
                    }
                } else if (argNow.equalsIgnoreCase("transferCount")) {
                    tmp = Integer.parseInt(args[++i]);
                    if (tmp > 0) {
                        transferCount = tmp;
                    }
                } else if (argNow.equalsIgnoreCase("maxTransferAmount")) {
                    tmp = Integer.parseInt(args[++i]);
                    if (tmp > 0) {
                        maxTransferAmount = tmp;
                    }
                } else if (argNow.equalsIgnoreCase("needLogging")) {
                    if (args[++i].equalsIgnoreCase("true")) {
                        needLogging = true;
                    } else {
                        needLogging = false;
                    }
                }
            } catch (NumberFormatException ex) {
            }
        }



        Logger logger = new Logger() {};

        AccountService accSer = new MyAccountService(logger);

        //Creating accs
        List<Integer> IDs = new ArrayList<>();
        for (int i=0;i<accountsCount;++i) {
            int IDNow = i+1;
            accSer.addAccount(IDNow, startAmount);
            IDs.add(IDNow);
        }

        //Creating threads
        List<TransferThread> threadList = new ArrayList<>();
        for (int i=0;i<threadCount;++i) {
            TransferThread threadNow = new TransferThread(i+1, accSer, transferCount, IDs, maxTransferAmount);
            threadNow.setLoger(logger);
            threadNow.setNeedLogging(needLogging);
            threadList.add(threadNow);
        }

        //Launch
        long timeToWork = System.currentTimeMillis();
        for (TransferThread thrNow : threadList) {
            thrNow.start();
            try {
                thrNow.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timeToWork = System.currentTimeMillis() - timeToWork;

        //Log all intresting



        logger.log("Time working: " + timeToWork + "ms");

        //Проверяем правильность всех сумм:
        long fullAmount = 0;
        for (Integer IDNow : IDs) {
            int amount = accSer.getAmount(IDNow);
            if (needLogging) {
                logger.log("ID" + Integer.toString(IDNow) + ": " + Integer.toString(amount));
            }

            fullAmount += amount;
        }

        logger.log("Full amount for start = " + accountsCount*startAmount);
        logger.log("Full amount for end = " + fullAmount);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



}
