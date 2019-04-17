package io.github.brunfo.apps.personalbudget.dao;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.List;

public interface PersonalBudgetDao {

    List<Transaction> getTransactions();

    boolean removeTransaction(int id);

    boolean editTransaction(Transaction transaction);

    int addTransaction(Transaction transaction);

    List<Account> getAccounts();

    boolean clearAllData();

    boolean removeAccount(int id);

    boolean editAccount(Account account);

    int addAccount(Account account);

}
