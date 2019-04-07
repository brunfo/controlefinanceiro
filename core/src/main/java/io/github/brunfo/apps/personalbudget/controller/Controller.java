package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Item;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.List;

public class Controller {

    private final static Controller INSTANCE = new Controller();

    //controller of the accounts
    private Accounts accounts;
    //controller of the budgets
    private Budgets budgets;

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

    //********************* Accounts ***********************//

    /**
     * Return a list of accounts!
     *
     * @return list of account.
     */
    public List<Account> getAccounts() {
        return this.accounts.getAccounts();
    }

    /**
     * Adds a account to the list.
     *
     * @param tempAccount account.
     * @return true if success.
     */
    public boolean addAccount(Account tempAccount) {
        return accounts.addAccount(tempAccount);
    }

    /**
     * Return a account width the specified name.
     *
     * @param accountName name of the account.
     * @return account. Null if does exists.
     */
    public Account getAccount(String accountName) {
        return accounts.getAccount(accountName);
    }

    /**
     * Return a account width the specified id.
     *
     * @param accountId id of the account.
     * @return account. Null if does exists.
     */
    public Account getAccountById(int accountId) {
        return accounts.getAccountById(accountId);
    }

    /**
     * Removes a account from the list.
     *
     * @param account account to be removed.
     * @return true if success.
     */
    public boolean removeAccount(Account account) {
        return accounts.removeAccount(account);
    }

    /**
     * Adds a transaction to a account.
     *
     * @param tempTransaction transaction.
     * @return true if success.
     */
    public boolean addTransaction(Transaction tempTransaction) {
        return accounts.addTransaction(tempTransaction);
    }

    public boolean transferToAcount(Transaction transaction, Account account) {
        return accounts.transferToAccount(transaction, account);
    }

    /**
     * Change the transaction's account
     *
     * @param transaction the transaction.
     * @param account     the new account.
     * @return true if success.
     */
    public boolean changeAccount(Transaction transaction, Account account) {
        return accounts.changeAccount(transaction, account);
    }

    /**
     * Return a duplicate copy of a transaction.
     *
     * @param transaction transaction to be copied.
     * @return copy of transaction.
     */
    public Transaction copyTransaction(Transaction transaction) {
        return accounts.copyTransaction(transaction);
    }

    /**
     * Removes a transaction from a account.
     *
     * @param transaction transaction.
     * @return true if success.
     */
    public boolean removeTransaction(Transaction transaction)  {
        return accounts.removeTransaction(transaction);
    }


    //********************* Budgets ***********************//

    /**
     * Splits a transaction in two different items from the budget.
     * If an item1 equals 0 after split, it will be removed, behaving like replace items.
     *
     * @param transaction transaction.
     * @param item        Item to split.
     * @param newItem     second item.
     * @param amount      Amount for second item.
     * @return true if success.
     */
    public boolean splitTransactionByBudgetItems(Transaction transaction, Item item, Item newItem, double amount) {
        return budgets.splitTransactionByBudgetItems(transaction, item, newItem, amount);
    }

}
