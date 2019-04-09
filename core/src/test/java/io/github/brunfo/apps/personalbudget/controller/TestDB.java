package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;


public class TestDB {
    private static Controller controller = Controller.getInstance();

    public static void main(String[] args) {

        // printAccounts();

        controller.loadDataFromDatabase();

        // printAccounts();

        Transaction transaction = new Transaction();
        transaction.setDescription("transferencia");
        transaction.setAmount(200.);
        transaction.setAccountId(1);
        //controller.addTransaction(transaction);

        //printAccounts();

        //controller.changeTransactionAccount(transaction,controller.getAccountById(5));

        controller.transferToAccount(transaction,
                controller.getAccountById(5));
        //printAccounts();

    }


    void printAccounts() {
        if (controller.getAccounts() != null)
            for (Account account : controller.getAccounts()) {
                printAccountTransactions(account);
            }
    }

    void printAccountTransactions(Account account) {
        System.out.println("\n*************************************************************");
        System.out.println("Account : " + account.getName());
        System.out.println("-------------------------------------------------------------");
        for (Transaction transaction :
                account.getTransactions()) {
            System.out.println(transaction);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Saldo : " + account.getBalance());
        System.out.println("*************************************************************\n");
    }

}
