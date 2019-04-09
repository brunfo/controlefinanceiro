package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.dao.PersonalBudgetDao;
import io.github.brunfo.apps.personalbudget.dao.PersonalBudgetDaoImplementation;
import io.github.brunfo.apps.personalbudget.model.*;

import java.util.List;

public class Controller {

    private final static Controller INSTANCE = new Controller();

    //Database handler
    private PersonalBudgetDao dbHandler;
    //controller of the accounts
    private Accounts accounts;
    //controller of the budgets
    private Budgets budgets;

    private Controller() {
        loadDataFromDatabase();
    }

    /**
     * Returns the unique instance of the main controller.
     *
     * @return the controller
     */
    public static Controller getInstance() {
        return INSTANCE;
    }

    //********************* Database ***********************//

    void loadDataFromDatabase() {
        dbHandler = PersonalBudgetDaoImplementation.getInstance();
        accounts = new Accounts();
        budgets = new Budgets();
        accounts.setAccounts(dbHandler.getAccounts());
        accounts.setTransactionsToAccounts(dbHandler.getTransactions());
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
     * Adds a account to the list.
     *
     * @param tempAccount account.
     * @return true if success.
     */
    public boolean addAccount(Account tempAccount) {
        //saves the transaction to db
        int id = dbHandler.addAccount(tempAccount);
        if (id >= 0) {
            tempAccount.setId(id);
            return accounts.addAccount(tempAccount);
        }
        return false;
    }

    /**
     * Edits the name of a account
     */
    public boolean editAccount(Account account) {
        return dbHandler.editAccount(account);
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

    //********************* Transactions ***********************//

    /**
     * Adds a transaction to a account.
     *
     * @param tempTransaction transaction.
     * @return true if success.
     */
    public boolean addTransaction(Transaction tempTransaction) {
        //saves transactions to database and gets the id
        int id = dbHandler.addTransaction(tempTransaction);
        if (id >= 0) {
            tempTransaction.setId(id);
            return accounts.addTransaction(tempTransaction);
        }
        return false;
    }

    /**
     * Edits a transaction.
     *
     * @param transaction the transaction
     * @return true if success.
     */
    public boolean editTransaction(Transaction transaction) {
        return changeTransactionAccount(transaction,
                accounts.getAccountById(transaction.getAccountId()));
    }

    /**
     * Removes a transaction from a account.
     *
     * @param transaction transaction.
     * @return true if success.
     */
    public boolean removeTransaction(Transaction transaction) {
        if (dbHandler.removeTrasaction(transaction.getId()))
            return accounts.removeTransaction(transaction);
        return false;
    }


    public boolean transferToAccount(Transaction tempTransaction, Account destinyAccount) {
        Account originAccount = accounts.getAccountById(tempTransaction.getAccountId());
        if (originAccount != null && destinyAccount != null) {
            //Creates two new transactions
            Transaction originTransaction = copyTransaction(tempTransaction);
            Transaction destinyTransaction = copyTransaction(tempTransaction);
            //Sets the amount to negative in the origin account
            originTransaction.setAmount(tempTransaction.getAmount() * -1);
            //adds description referring that is a transfer
            originTransaction.setDescription("trfd from: " + tempTransaction.getDescription());
            destinyTransaction.setDescription("trfd to: " + tempTransaction.getDescription());

            //sets destiny account
            destinyTransaction.setAccountId(destinyAccount.getId());

            //adds to the list of respective accounts
            if (addTransaction(originTransaction) &&
                    addTransaction(destinyTransaction))
                return true;

            //TODO change this method to a rollback
            //the transfer had failed if has reached this point,
            // it reverts  any entry in the database
            dbHandler.removeTrasaction(originTransaction.getId());
            dbHandler.removeTrasaction(destinyTransaction.getId());
        }
        return false;
    }

    /**
     * Change the transaction's account
     *
     * @param transaction the transaction.
     * @param account     the new account.
     * @return true if success.
     */
    public boolean changeTransactionAccount(Transaction transaction, Account account) {
        Transaction tempTransaction = copyTransaction(transaction);
        if (accounts.editTransactionAccount(transaction, account)) {
            if (!dbHandler.editTransaction(transaction))
                transaction = tempTransaction;
            else
                return true;
        }
        return false;
    }

    /**
     * Return a duplicate copy of a transaction.
     *
     * @param transaction transaction to be copied.
     * @return copy of transaction.
     */
    Transaction copyTransaction(Transaction transaction) {
        return accounts.copyTransaction(transaction);
    }




    //********************* Budgets ***********************//

    public boolean addBudget(Budget tempBudget) {
        return budgets.addBudget(tempBudget);
    }

    public boolean removeBudget(String budget) {
        return budgets.removeBudget(budget);
    }

    public Budget getBudget(String budgetName) {
        return budgets.getBudget(budgetName);
    }

    public boolean addItemToBudget(Item item, String budget) {
        return budgets.addItemToBudget(item, budget);
    }

    public boolean removeItemFromBudget(Item item, String budget) {
        return budgets.removeItemFromBudget(item, budget);
    }

    public List<Item> getItemsByFamily(Family family, String budget) {
        return budgets.getItemsByFamily(family, budget);
    }

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
