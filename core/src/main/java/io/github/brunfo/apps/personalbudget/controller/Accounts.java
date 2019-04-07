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

    /**
     * Adds a transaction to a existing account
     *
     * @param tempTransaction transaction to add.
     * @return true if success, false is not.
     */
    boolean addTransaction(Transaction tempTransaction) {
        Account account = getAccountById(tempTransaction.getAccountId());
        //Verifies if is possible to make the transaction
        if (isAccountValid(account) && account.getBalance() + tempTransaction.getAmount() >= 0) {
            account.getTransactions().add(tempTransaction);
            //updates the account balance
            account.updateBalance(tempTransaction.getAmount());
            //update the transaction balance
            tempTransaction.setBalance(account.getBalance());
            return true;
        }
        return false;
    }

    boolean changeAccount(Transaction transaction, Account account) {
        if (isAccountValid(account)) {
            Account oldAccount = getAccountById(transaction.getAccountId());
            removeTransaction(transaction);
            transaction.setAccountId(account.getId());
            addTransaction(transaction);
            updateTransactionsBalance(oldAccount);
            updateTransactionsBalance(account);
            return true;
        }
        return false;
    }

    private void updateTransactionsBalance(Account account) {
        double balance = 0.0;
        for (Transaction transaction : account.getTransactions()) {
            balance += transaction.getAmount();
            transaction.setBalance(balance);
        }
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
            account.updateBalance(-1 * transaction.getAmount());
            return true;
        }
        return false;
    }

    @SuppressWarnings({"SpellCheckingInspection"})
    boolean transferToAccount(Transaction tempTransaction, Account account) {
        if (isAccountValid(account)) {
            Transaction originTransaction = copyTransaction(tempTransaction);
            Transaction destinyTransaction = copyTransaction(tempTransaction);
            originTransaction.setAmount(tempTransaction.getAmount() * -1);
            originTransaction.setDescription("trfd from: " + tempTransaction.getDescription());
            destinyTransaction.setDescription("trfd to: " + tempTransaction.getDescription());
            destinyTransaction.setAccountId(account.getId());
            if (addTransaction(originTransaction))
                addTransaction(destinyTransaction);
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
