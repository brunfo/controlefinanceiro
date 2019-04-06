package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;

class Accounts {

    private final List<Account> accounts = new ArrayList<>();

    List<Account> getAccounts() {
        return accounts;
    }

    void addAccount(Account tempAccount) {
        accounts.add(tempAccount);
    }

    void removeAccount(Account account) {
        accounts.remove(account);
    }

    Account getAccount(String accountName) {
        for (Account account : accounts) {
            if (account.getName().equalsIgnoreCase(accountName))
                return account;
        }
        return null;
    }

    Account getAccountById(int accountId) {
        for (Account account : accounts) {
            if (account.getId() == accountId)
                return account;
        }
        return null;
    }

    boolean addTransaction(Transaction tempTransaction) {
        Account account = getAccountById(tempTransaction.getAccountId());
        //Verifies if is possible to make the transaction
        if (account.getBalance() + tempTransaction.getAmount() >= 0) {
            account.getTransactions().add(tempTransaction);
            //updates the account balance
            account.updateBalance(tempTransaction.getAmount());
            //update the transaction balance
            tempTransaction.setBalance(account.getBalance());
            return true;
        }
        return false;
    }


    boolean removeTransaction(Transaction transaction) {
        Account account = getAccountById(transaction.getAccountId());
        if (account.getTransactions().remove(transaction)) {
            account.updateBalance(-1 * transaction.getAmount());
            return true;
        }
        return false;
    }
}
