package io.github.brunfo.apps.personalbudget.dao;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

public class PersonalBudgetDaoImplementationTest {
    PersonalBudgetDao dbHandler = PersonalBudgetDaoImplementation.getInstance();

    @Before
    public void setUp() throws Exception {
        //clear data
        dbHandler.clearAllData();
    }

    @After
    public void tearDown() throws Exception {
        //clear data
        dbHandler.clearAllData();
    }


    @Test
    public void transactionsTests() {
        //prints all transactions in database
        dbHandler.getTransactions().forEach(System.out::println);

        //creates new transaction
        Account account1 = new Account(1, "Account 1");
        Transaction transaction = new Transaction(1, account1.getId(), LocalDate.now(), LocalDate.now(), "movimento", 20.0);

        //adds it to database, gets the id and assign it to the object
        transaction.setId(dbHandler.addTransaction(transaction));
        System.out.println(transaction);

        //test if was successfully added to database, by getting a id superior to -1
        assert (transaction.getId() >= 0);

        //prints all transactions in database
        dbHandler.getTransactions().forEach(System.out::println);

        //changes the amount
        transaction.setAmount(10.0);

        //submit changes to database
        assertTrue(dbHandler.editTransaction(transaction));

        //prints all transactions in database
        dbHandler.getTransactions().forEach(System.out::println);

        //gets the account id
        assert (dbHandler.getAccountIdFromTransaction(transaction.getId()) == account1.getId());

        //changes the account and amount
        Account account2 = new Account(2, "Account 2");
        transaction.setAccount(account2);
        transaction.setAmount(30.0);
        assertTrue(dbHandler.editTransaction(transaction));
        assert (dbHandler.getAccountIdFromTransaction(transaction.getId()) == account2.getId());

        //prints all transactions in database
        dbHandler.getTransactions().forEach(System.out::println);

        //removes the transaction from database
        System.out.println("\n***** remove *****");
        assertTrue(dbHandler.removeTransaction(transaction.getId()));

        //prints all transactions in database
        dbHandler.getTransactions().forEach(System.out::println);
        System.out.println("*** end remove ***");
    }

    @Test
    public void getAccounts() {
    }

    @Test
    public void clearAllData() {
    }

    @Test
    public void removeAccount() {
    }

    @Test
    public void editAccount() {
    }

    @Test
    public void addAccount() {
    }
}