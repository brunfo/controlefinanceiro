package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountsUnitaryTests {

    Controller controller;
    Transaction transaction;
    Account conta3;

    @Before
    public void setUp() throws Exception {
        controller = Controller.getInstance();


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAccounts() {
    }

    @Test
    public void addAccount() {
        controller.addAccount(new Account(1, "Conta 1"));
        controller.addAccount(new Account(2, "Conta 2"));
        assertTrue(controller.addAccount(new Account(4, "Conta 4")));
        conta3 = new Account(3, "Conta 3");
        assertTrue(controller.addAccount(conta3));
    }

    @Test
    public void removeAccount() {
        assertFalse(controller.removeAccount(controller.getAccount("conta 10")));
    }

    @Test
    public void getAccount() {

        assertSame(controller.getAccount("Conta 3"), conta3);
    }

    @Test
    public void getAccountById() {
        assertSame(controller.getAccountById(3), controller.getAccount("conta 3"));
    }

    @Test
    public void addTransaction() {
        try {
            transaction = new Transaction();
            transaction.setAmount(-28.0);
            transaction.setDescription("movimento 4");
            transaction.setAccountId(4);
            assertFalse(controller.addTransaction(transaction));
            transaction = new Transaction();
            transaction.setAmount(28.0);
            transaction.setDescription("movimento 5");
            transaction.setAccountId(5);
            assertTrue(controller.addTransaction(transaction));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        System.out.println("\n\n\ttransfer tests");
        transaction = new Transaction();
        transaction.setAmount(5.);
        transaction.setDescription("transferencia");
        transaction.setAccountId(2);
        try {
            assertTrue(controller.transferToAcount(transaction, controller.getAccount("Conta 1")));
            assertNull(controller.getAccount("Conta 6"));
            assertFalse(controller.transferToAcount(transaction, controller.getAccount("Conta 6")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}