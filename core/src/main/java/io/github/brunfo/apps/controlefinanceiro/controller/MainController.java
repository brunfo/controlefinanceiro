package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.model.Account;
import io.github.brunfo.apps.controlefinanceiro.model.Transaction;

public interface MainController {
    void getData();

    void saveTransaction(Transaction tempTransaction);

    void saveAccount(Account tempAccount);

    void updateTransaction(Transaction selectedTransaction);

    void updateAccount(Account selectedAccount);

    void deleteTransaction(Transaction selectedTransaction);

    void deleteAccount(Account selectedAccount);
}
