package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.List;

public class Controller {

    private final static Controller INSTANCE = new Controller();

    private Accounts accounts;

    private Controller() {
        accounts = new Accounts();
    }

    public static Controller getInstance() {
        return INSTANCE;
    }

    public List<Account> getAccounts() {
        return this.accounts.getAccounts();
    }

    public void addAccount(Account tempAccount) {
        accounts.addAccount(tempAccount);
    }

    public Account getAccount(String accountName) {
        return accounts.getAccount(accountName);
    }

    public Account getAccountById(int accountId) {
        return accounts.getAccountById(accountId);
    }

    public void removeAccount(Account account) {
        accounts.removeAccount(account);
    }

    public boolean addTransaction(Transaction tempTransaction) {
        return accounts.addTransaction(tempTransaction);
    }

    public boolean removeTransaction(Transaction transaction) {
        return accounts.removeTransaction(transaction);
    }

}
