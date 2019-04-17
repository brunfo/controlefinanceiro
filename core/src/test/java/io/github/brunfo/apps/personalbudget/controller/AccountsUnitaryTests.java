package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("SpellCheckingInspection")
public class AccountsUnitaryTests {

    Account account = new Account(0, "Conta 1");
    private Controller controller = Controller.getInstance();

    public void printAccounts() {
        System.out.println("************ Printing accounts ************");
        for (Account account : controller.getAccounts()) {
            printAccountTransactions(account);
        }
        System.out.println("********** End printing accounts **********\n");
    }


    private boolean printAccountTransactions(Account account) {
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
        return true;
    }

    @Before
    public void setUp() {
    }

    /**
     * removes all remaining data
     */
    @After
    public void tearDown() {
        assertTrue(controller.clearAllData());
    }

    @Test
    public void getAccounts() {
        for (Account account : controller.getAccounts()) {
            assertTrue(printAccountTransactions(account));
        }
    }


    @Test
    public void addAndRemoveAccount() {
        Account account = new Account();
        assertTrue(controller.addAccount(account));
        assertTrue(controller.removeAccount(controller.getAccountById(account.getId())));
        assertFalse(controller.removeAccount(controller.getAccountById(account.getId())));
    }

    @Test
    public void addTransaction() {
        //adding account
        Account account = new Account();
        assertTrue(controller.addAccount(account));

        //create transacntion
        Transaction transaction = new Transaction(
                0,
                account.getId(),
                LocalDate.now(),
                LocalDate.now(),
                "movimento 1",
                -28
        );

        //tries to make a negative transaction widthout suficient balance
        assertFalse(controller.addTransaction(transaction));

        //makes a transaction
        transaction.setAmount(28.);
        assertTrue(controller.addTransaction(transaction));
    }

    @Test
    public void changeAccount() {
    }

    @Test
    public void copyTransaction() {
    }

    @Test
    public void removeTransaction() {
    }

    @Test
    public void transferToAccount() {
//        System.out.println("\n\n\ttransfer tests");
//        transaction = new Transaction();
//        transaction.setAmount(5.);
//        transaction.setDescription("transferencia");
//        // transaction.setAccountId(2);
//        try {
//            assertTrue(controller.transferToAccount(transaction, controller.getAccount("Conta 1")));
//            assertNull(controller.getAccount("Conta 6"));
//            assertFalse(controller.transferToAccount(transaction, controller.getAccount("Conta 6")));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}