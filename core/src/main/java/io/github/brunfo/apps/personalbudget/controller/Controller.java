package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.List;

public class Controller {

    private final static Controller INSTANCE = new Controller();

    //controller of the accounts
    private Accounts accounts;

    private Controller() {
        accounts = new Accounts();
    }

    /**
     * Returns the unique instance of the main controller.
     *
     * @return the controller
     */
    public static Controller getInstance() {
        return INSTANCE;
    }

    /**
     * Return a list of accounts!
     * @return list of account.
     */
    public List<Account> getAccounts() {
        return this.accounts.getAccounts();
    }

    /**
     * Adds a account to the list.
     * @param tempAccount account.
     * @return true if success.
     */
    public boolean addAccount(Account tempAccount) {
        return accounts.addAccount(tempAccount);
    }

    /**
     * Return a account width the specified name.
     * @param accountName name of the account.
     * @return account. Null if does exists.
     */
    public Account getAccount(String accountName) {
        return accounts.getAccount(accountName);
    }

    /**
     * Return a account width the specified id.
     * @param accountId id of the account.
     * @return account. Null if does exists.
     */
    public Account getAccountById(int accountId) {
        return accounts.getAccountById(accountId);
    }

    /**
     * Removes a account from the list.
     * @param account account to be removed.
     * @return true if success.
     */
    public boolean removeAccount(Account account) {
        return accounts.removeAccount(account);
    }

    /**
     * Adds a transaction to a account.
     * @param tempTransaction transaction.
     * @return true if success.
     * @throws Exception Returns an error if the account does not exists in the list.
     */
    public boolean addTransaction(Transaction tempTransaction) throws Exception {
        return accounts.addTransaction(tempTransaction);
    }

    /**
     * Removes a transaction from a account.
     * @param transaction transaction.
     * @return true if success.
     * @throws Exception Returns an error if the account does not exists in the list.
     */
    public boolean removeTransaction(Transaction transaction) throws Exception {
        return accounts.removeTransaction(transaction);
    }

}
