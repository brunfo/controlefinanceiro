package io.github.brunfo.apps.personalbudget.dao;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.List;

public interface PersonalBudgetDao {

    List<Transaction> getTransactions();

    boolean deleteTransaction(int id);

    boolean editTransaction(Transaction transaction);

    int saveTransaction(Transaction transaction);

    List<Account> getAccounts();

    boolean deleteAccount(int id);

    boolean editAccount(Account account);

    int saveAccount(Account account);

}
