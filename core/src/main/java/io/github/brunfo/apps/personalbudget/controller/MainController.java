package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

public interface MainController {
    void getData();

    void saveTransaction(Transaction tempTransaction);

    void saveAccount(Account tempAccount);

    void updateTransaction(Transaction selectedTransaction);

    void updateAccount(Account selectedAccount);

    void deleteTransaction(Transaction selectedTransaction);

    void deleteAccount(Account selectedAccount);
}
