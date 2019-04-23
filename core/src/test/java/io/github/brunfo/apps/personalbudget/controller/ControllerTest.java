package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ControllerTest {
    private static Controller controller = Controller.getInstance();

    @BeforeClass
    public static void setUp() {
    }

    @After
    public void tearDown() throws Exception {
        //clear data
        controller.clearAllData();
    }


    @Test
    public void AccountsTests() {
        //adds account
        printAccounts();
        Account account = new Account("Account 1");
        assertTrue(controller.addAccount(account));
        printAccounts();

        //changes name
        account.setName("Account 1 renamed");
        assertTrue(controller.editAccount(account));
        printAccounts();

        //TODO this method shouldn't be allow, to avoid changing id, but is necessary to assign id by dbHandler
        account.setId(2);
        assertFalse(controller.editAccount(account));
        printAccounts();
    }

    @Test
    public void TransactionsTests() {
        printAccounts();

        //creates new transaction
        Account account1 = new Account("Account 1");
        assertTrue(controller.addAccount(account1));
        Transaction transaction = new Transaction(1, account1.getId(), LocalDate.now(), LocalDate.now(), "movimento", 20.0);

        //adds it to controller and database
        assertTrue(controller.addTransaction(transaction));

        printAccounts();

        //changes the amount
        transaction.setAmount(100.);
        assertTrue(controller.editTransaction(transaction));
        printAccounts();

        //changes the amount
        transaction.setAmount(-100.);
        assertFalse(controller.editTransaction(transaction));
        printAccounts();

        //changes the account
        Account account2 = new Account("Account 2");
        assertTrue(controller.addAccount(account2));
        transaction.setAccount(account2);
        transaction.setAmount(10.);
        assertTrue(controller.editTransaction(transaction));

        printAccounts();

    }


    private void printAccounts() {
        //prints all transactions in database
        for (Account account : controller.getAccounts()) {
            System.out.println("\n*************************************************************");
            System.out.println("Account : " + account.getId() + " : " + account.getName());
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
}


