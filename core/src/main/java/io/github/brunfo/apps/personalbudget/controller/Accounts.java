package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;

class Accounts {

    private final List<Account> accounts = new ArrayList<>();

    List<Account> getAccounts() {
        return accounts;
    }

    boolean addAccount(Account tempAccount) {
        return accounts.add(tempAccount);
    }

    boolean removeAccount(Account account) {
        return accounts.remove(account);
    }

    Account getAccount(String accountName) {
        for (Account account : accounts) {
            if (account.getName().equalsIgnoreCase(accountName))
                return account;
        }
        return null;
    }

    Account getAccountById(int accountId) {
        for (Account account : accounts) {
            if (account.getId() == accountId)
                return account;
        }
        return null;
    }

    /**
     * Adds a transaction to a existing account
     *
     * @param tempTransaction
     * @return
     */
    boolean addTransaction(Transaction tempTransaction) throws Exception {
        Account account = getAccountById(tempTransaction.getAccountId());
        isAccountValid(account);
        //Verifies if is possible to make the transaction
        if (account.getBalance() + tempTransaction.getAmount() >= 0) {
            account.getTransactions().add(tempTransaction);
            //updates the account balance
            account.updateBalance(tempTransaction.getAmount());
            //update the transaction balance
            tempTransaction.setBalance(account.getBalance());
            return true;
        }
        return false;
    }


    /**
     * Removes a transaction from a account.
     *
     * @param transaction to be removed.
     * @return true or false. If transaction does not exists in the account, return false.
     */
    boolean removeTransaction(Transaction transaction) throws Exception {
        Account account = getAccountById(transaction.getAccountId());
        isAccountValid(account);
        if (account.getTransactions().remove(transaction)) {
            account.updateBalance(-1 * transaction.getAmount());
            return true;
        }
        return false;
    }

    boolean isAccountValid(Account account) throws Exception {
        if (account != null) {
            return true;
        }
        throw new Exception("Account does not exists!");
    }
}
