package transfer;

import data.Account;
import exceptions.ExceptionDB;
import log.Logger;
import source.AccountService;

import java.util.List;

/**
 * Thread for transfer test.
 * @author mav
 * @version 1.0
 *
 */
class TransferThread extends Thread {

    private final int threadNumber;

    private final AccountService accountService;

    private final int transferCount;
    private final List<Integer> IDs;
    private final int maxTransferAmount;

    private final int IDsCount;

    private Logger loger;

    public void setLoger(Logger loger) {
        this.loger = loger;
    }

    private boolean needLogging = true;

    public void setNeedLogging(boolean needLogging) {
        this.needLogging = needLogging;
    }

    public TransferThread(int threadNumber, AccountService accountService, int transferCount, List<Integer> IDs, int maxTransferAmount) {
        this.threadNumber = threadNumber;
        this.accountService = accountService;
        this.transferCount = transferCount;
        this.IDs = IDs;
        this.maxTransferAmount = maxTransferAmount;

        if (IDs != null) {
            IDsCount = IDs.size();
        } else {
            IDsCount = 0;
        }

    }

    public void run() {

        if (null == accountService) {
            return;
        }

        if (maxTransferAmount < 0) {
            return;
        }



        for (int i=0;i<transferCount;++i) {
            int fromID = (int)(Math.random() * (IDsCount));
            fromID = IDs.get(fromID);

            int toID = (int)(Math.random() * (IDsCount));
            toID = IDs.get(toID);

            int amount = (int)(Math.random() * (maxTransferAmount + 1));

            int amountFrom = 0;
            int amountTo = 0;
            if (needLogging) {
                amountFrom = accountService.getAmount(fromID);
                amountTo = accountService.getAmount(toID);
            }
            int errCode = accountService.transferMoney(fromID, toID, amount);

            if (needLogging) {
                int amountFromNext = accountService.getAmount(fromID);
                int amountToNext = accountService.getAmount(toID);

                StringBuilder strForLog = new StringBuilder("Trans" + Integer.toString(i+1) + ":");
                strForLog.append(" IDs: " + Integer.toString(fromID) + "(" + Integer.toString(amountFrom) + ")" + "->"
                        + Integer.toString(toID) + "(" + Integer.toString(amountTo) + ")" + " \\ amount to transfer: " + Integer.toString(amount) + " \\ ");
                if (errCode < 0) {
                    strForLog.append("Something went wrong! ");
                    strForLog.append(accountService.getErrorReason(errCode) + " ");
                    strForLog.append("i'm out!");
                    log(strForLog.toString());
                    return;
                } else if ((errCode == 2) || (errCode == 3)) {
                    strForLog.append("Strange start settings! ");
                    strForLog.append(accountService.getErrorReason(errCode));
                } else if (errCode == 5) {
                    strForLog.append(accountService.getErrorReason(errCode));
                } else if (errCode == 4) {
                    strForLog.append(accountService.getErrorReason(errCode) + " ");
//                    strForLog.append(" IDs: " + Integer.toString(fromID) + "(" + Integer.toString(amountFromNext) + ")" + "->"
//                            + Integer.toString(toID) + "(" + Integer.toString(amountToNext) + ")" + " \\");
                } else if (errCode == 0) {
                    strForLog.append("IDs: " + Integer.toString(fromID) + "(" + Integer.toString(amountFromNext) + ")" + "->"
                            + Integer.toString(toID) + "(" + Integer.toString(amountToNext) + ")" + " \\");
                } else {
                    strForLog.append("Something went wrong! Unknown code for specification");
                    strForLog.append(accountService.getErrorReason(errCode));
                    strForLog.append("i'm out!");
                    log(strForLog.toString());
                    return;
                }

                log(strForLog.toString());
            } else {
                if (errCode < 0) {
                    StringBuilder strForLog = new StringBuilder("Trans" + i + ")");
                    strForLog.append(" IDs: " + Integer.toString(fromID) + "->" + Integer.toString(toID) + " Am=" + Integer.toString(amount));
                    strForLog.append(" Before: From: " + Integer.toString(amountFrom) + " To: " + Integer.toString(amountTo) + " ");

                    strForLog.append("Something went wrong! ");
                    strForLog.append(accountService.getErrorReason(errCode) + " ");
                    strForLog.append("i'm out!");
                    log(strForLog.toString());
                    return;
                }
            }
        }
    }

    /**
     * Decorator for logging messages
     * @param mes - message for logging
     */
    public void log(String mes) {
        String startMes = "ThrNum" + Integer.toString(threadNumber) + ": ";
        if (null == loger) {
            System.out.println(startMes + "NO LOGGER! " + mes + "\n");
        } else {
            loger.log(startMes + mes + "\n");
        }
    }

}
