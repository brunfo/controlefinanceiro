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

    public void loadDataFromDatabase() {
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
     * Removes a account from the list after removes it successfully
     * from database.
     *
     * @param account account to be removed.
     * @return true if success.
     */
    public boolean removeAccount(Account account) {
        if (account != null && dbHandler.removeAccount(account.getId()))
            return accounts.removeAccount(account);
        return false;
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
     * @param tempTransaction the transaction
     * @return true if success.
     */
    public boolean editTransaction(Transaction tempTransaction) {
        //checks if transaction's have change
        int id = dbHandler.getAccountIdFromTransaction(tempTransaction.getId());
        if (id == tempTransaction.getAccount().getId())
            return dbHandler.editTransaction(tempTransaction);
        else {
            //creates a clone of the transaction e sets it account to the original
            Transaction originalTransaction = tempTransaction.clone();
            originalTransaction.setAccount(accounts.getAccountById(id));
            //send changes to database
            if (dbHandler.editTransaction(tempTransaction))
                //update accounts list
                return accounts.editTransactionAccount(
                        originalTransaction,
                        tempTransaction.getAccount());
        }
        return false;
    }

    /**
     * Removes a transaction from a account.
     *
     * @param transaction transaction.
     * @return true if success.
     */
    public boolean removeTransaction(Transaction transaction) {
        if (dbHandler.removeTransaction(transaction.getId()))
            return accounts.removeTransaction(transaction);
        return false;
    }

    public boolean clearAllData() {
        if (dbHandler.clearAllData()) {
            loadDataFromDatabase();
            return true;
        }
        return false;
    }


    public boolean transferToAccount(Transaction tempTransaction, Account destinyAccount) {
        Account originAccount = accounts.getAccountById(tempTransaction.getAccountId());
        if (originAccount != null && destinyAccount != null) {
            //Creates two new transactions
            Transaction originTransaction = tempTransaction.clone();
            Transaction destinyTransaction = tempTransaction.clone();
            //Sets the amount to negative in the origin account
            originTransaction.setAmount(tempTransaction.getAmount() * -1);
            //adds description referring that is a transfer
            originTransaction.setDescription("trf from: " + tempTransaction.getDescription());
            destinyTransaction.setDescription("trf to: " + tempTransaction.getDescription());

            //sets destiny account
            destinyTransaction.setAccount(destinyAccount);

            //adds to the list of respective accounts
            if (addTransaction(originTransaction) &&
                    addTransaction(destinyTransaction))
                return true;

            //TODO change this method to a rollback
            //the transfer had failed if has reached this point,
            // it reverts  any entry in the database
            dbHandler.removeTransaction(originTransaction.getId());
            dbHandler.removeTransaction(destinyTransaction.getId());
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
        Transaction tempTransaction = transaction.clone();
        if (accounts.editTransactionAccount(transaction, account)) {
            return dbHandler.editTransaction(transaction);
        }
        return false;
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
