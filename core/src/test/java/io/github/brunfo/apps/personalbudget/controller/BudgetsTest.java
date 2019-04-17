package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Budget;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("SpellCheckingInspection")
public class BudgetsTest {

    private static Controller controller;
    private static Budget personalBudget;

    @Before
    public void setUp() {
        controller = Controller.getInstance();
        personalBudget = new Budget(1, "pessoal");
    }

    @Test
    public void addBudget() {

    }

    @Test
    public void removeBudget() {
    }

    @Test
    public void getBudget() {
        assertTrue(controller.addBudget(personalBudget));
        assertSame(personalBudget, controller.getBudget("pessoal"));
    }

    @Test
    public void getBudgetById() {
        assertSame(personalBudget, controller.getAccountById(1));
    }

    @Test
    public void getItemsByFamily() {
    }

    @Test
    public void splitTransactionByBudgetItems() {
    }

    @Test
    public void addItemToBudget() {
    }

    @Test
    public void removeItemFromBudget() {
    }
}