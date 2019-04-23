package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;

class Accounts {

    private List<Account> accounts = null;

    private boolean allowNegativeBalance = false;

    List<Account> getAccounts() {
        return accounts;
    }

    void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    boolean addAccount(Account tempAccount) {
        return accounts.add(tempAccount);
    }

    boolean removeAccount(Account account) {
        return accounts.remove(account);
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


    void setTransactionsToAccounts(List<Transaction> transactionList) {
        allowNegativeBalance = true;
        for (Transaction transaction : transactionList) {
            addTransaction(transaction);
        }
        allowNegativeBalance = false;
    }

    /**
     * Adds a transaction to a existing account
     *
     * @param tempTransaction transaction to add.
     * @return true if success, false is not.
     */
    boolean addTransaction(Transaction tempTransaction) {
        Account account = getAccountById(tempTransaction.getAccountId());
        //add this account a reference to the transaction;
        tempTransaction.setAccount(account);
        //Verifies if is possible to make the transaction
        if (isAccountValid(account) &&
                (account.getBalance() + tempTransaction.getAmount() >= 0 ||
                        allowNegativeBalance)) {
            account.getTransactions().add(tempTransaction);
            //updates the account balance
            account.updateBalance();

            return true;
        }
        return false;
    }

    boolean editTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
        //creates a virtual list of transaction, adding all except the transaction to be edited
        List<Transaction> virtualTransactions = new ArrayList<>();
        account.getTransactions().forEach(t -> {
            if (t != transaction) virtualTransactions.add(t);
        });
        //gets the projected balance
        double virtualBalance = virtualTransactions.stream().mapToDouble(Transaction::getAmount).sum();
        if (virtualBalance + transaction.getAmount() >= 0 ||
                allowNegativeBalance) {
            //updates account balance
            transaction.getAccount().updateBalance();
            return true;
        }
        return false;
    }

    boolean editAccountTransaction(Transaction transaction, Account account) {
        if (isAccountValid(account)) {
            Account oldAccount = getAccountById(transaction.getAccountId());
            removeTransaction(transaction);
            transaction.setAccount(account);
            addTransaction(transaction);
            oldAccount.updateBalance();
            account.updateBalance();
            return true;
        }
        return false;
    }


    Transaction copyTransaction(Transaction transaction) {
        return new Transaction(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getOperationDate(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getBudgetItems());
    }

    /**
     * Removes a transaction from a account.
     *
     * @param transaction to be removed.
     * @return true or false. If transaction does not exists in the account, return false.
     */
    boolean removeTransaction(Transaction transaction) {
        Account account = getAccountById(transaction.getAccountId());
        if (isAccountValid(account) && account.getTransactions().remove(transaction)) {
            account.updateBalance();
            return true;
        }
        return false;
    }

    /**
     * Checks if a account exists.
     *
     * @param account account to check.
     * @return true or false.
     */
    private boolean isAccountValid(Account account) {
        return account != null;
    }


}
