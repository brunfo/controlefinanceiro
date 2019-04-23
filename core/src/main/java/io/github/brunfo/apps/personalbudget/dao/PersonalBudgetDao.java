package io.github.brunfo.apps.personalbudget.dao;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.List;

public interface PersonalBudgetDao {

    List<Transaction> getTransactions();

    int getAccountIdFromTransaction(int transactionId);

    boolean removeTransaction(int id);

    boolean editTransaction(Transaction transaction);

    boolean addTransaction(Transaction transaction);

    List<Account> getAccounts();

    boolean clearAllData();

    boolean removeAccount(int id);

    boolean editAccount(Account account);

    boolean addAccount(Account account);

}
