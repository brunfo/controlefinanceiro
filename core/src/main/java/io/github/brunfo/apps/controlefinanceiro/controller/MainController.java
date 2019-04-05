package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.model.Account;
import io.github.brunfo.apps.controlefinanceiro.model.Transaction;

public interface MainController {
    void loadFromDataBase();

    void saveTransaction(Transaction tempTransaction);

    void saveAccount(Account tempAccount);

    void updateTransactionToDataBase(Transaction selectedTransaction);

    void updateAccount(Account selectedAccount);

    void deleteTransactionFromDataBase(Transaction selectedTransaction);

    void deleteAccountFromDataBase(Account selectedAccount);
}
