package io.github.brunfo.apps.personalbudget.dao;

import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonalBudgetDaoImplementationTest {
    PersonalBudgetDao dbHandler = PersonalBudgetDaoImplementation.getInstance();

    @Before
    public void setUp() throws Exception {
        //clear data
        // dbHandler.clearAllData();
    }

    @After
    public void tearDown() throws Exception {
        //clear data
        dbHandler.clearAllData();
    }

    @Test
    public void transactionsTests() {
        //prints all transactions in database
        printTransactions();

        //creates new transaction
        Account account1 = new Account("Account 1");
        Transaction transaction = new Transaction(1, account1.getId(), LocalDate.now(), LocalDate.now(), "movimento", 20.0);

        //adds it to database, gets the id and assign it to the object
        assertTrue(dbHandler.addTransaction(transaction));

        //prints all transactions in database
        printTransactions();

        //changes the amount
        transaction.setAmount(10.0);

        //submit changes to database
        assertTrue(dbHandler.editTransaction(transaction));

        //prints all transactions in database
        printTransactions();

        //gets the account id
        assert (dbHandler.getAccountIdFromTransaction(transaction.getId()) == account1.getId());

        //changes the account and amount
        Account account2 = new Account("Account 2");
        transaction.setAccount(account2);
        transaction.setAmount(30.0);
        assertTrue(dbHandler.editTransaction(transaction));
        assert (dbHandler.getAccountIdFromTransaction(transaction.getId()) == account2.getId());

        //prints all transactions in database
        printTransactions();

        //removes the transaction from database
        assertTrue(dbHandler.removeTransaction(transaction.getId()));
        printTransactions();
    }

    @Test
    public void accountTests() {
        printAccounts();
        //add account
        Account account = new Account("account 1");
        assertTrue(dbHandler.addAccount(account));
        printAccounts();

        //changes name
        account.setName("account 2");
        assertTrue(dbHandler.editAccount(account));
        printAccounts();

        //removes nonexistent account
        assertFalse(dbHandler.removeAccount(new Account(null).getId()));
        printAccounts();

        //removes existent account

        assertTrue(dbHandler.removeAccount(account.getId()));
        printAccounts();


    }

    private void printTransactions() {
        System.out.println("\n*\t*\t*\t*\t* PRINTING TRANSACTIONS *\t*\t*\t*\t*\n");
        //prints all transactions in database
        dbHandler.getTransactions().forEach(System.out::println);
        System.out.println("\n*\t*\t*\t*\tEND PRINTING TRANSACTIONS\t*\t*\t*\t*\n\n\n");
    }

    private void printAccounts() {
        System.out.println("\n*\t*\t*\t*\t* PRINTING ACCOUNTS *\t*\t*\t*\t*\n");
        //prints all transactions in database
        dbHandler.getAccounts().forEach(System.out::println);
        System.out.println("\n*\t*\t*\t*\tEND PRINTING ACCOUNTS\t*\t*\t*\t*\n\n\n");
    }
}