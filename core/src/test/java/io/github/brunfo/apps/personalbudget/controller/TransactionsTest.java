package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TransactionsTest {
    private static Controller controller = Controller.getInstance();

    @BeforeClass
    public static void setUp() {
        controller.loadDataFromDatabase();
        //printAccounts();
    }

    @Test
    public void loadDataFromDatabase() {

    }

    @Test
    public void addAccount() {
        Account account = new Account();
        account.setName("test account");
        assertTrue(controller.addAccount(account));
    }

    @Test
    public void addTransaction() {

        Transaction transaction = new Transaction();
        //transaction.setAccountId(1);
        transaction.setDescription("Teste description");
        transaction.setAmount(200.);
        assertTrue(controller.addTransaction(transaction));
    }

}
