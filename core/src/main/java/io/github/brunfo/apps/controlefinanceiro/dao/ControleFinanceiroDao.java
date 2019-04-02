package io.github.brunfo.apps.controlefinanceiro.dao;

import io.github.brunfo.apps.controlefinanceiro.model.Account;
import io.github.brunfo.apps.controlefinanceiro.model.Transaction;

import java.util.List;

public interface ControleFinanceiroDao {

    List<Transaction> getTransactions();

    boolean deleteTransaction(int id);

    boolean editTransaction(Transaction transaction);

    int saveTransaction(Transaction transaction);

    List<Account> getAccounts();

    boolean deleteAccount(int id);

    boolean editAccount(Account account);

    int saveAccount(Account account);

}
