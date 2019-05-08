package io.github.brunfo.apps.personalbudget.model;

import io.github.brunfo.apps.personalbudget.controller.Controller;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemTest {

    private Controller controller = Controller.getInstance();

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void itemsTests() {
        printBudgets();
        Item budgetRoot = new Item(0, "item 1", 10.);
        assertTrue(controller.addBudget(budgetRoot));
        printBudgets();

        Item item11 = new Item(1, "item1.1", 1.);
        budgetRoot.add(item11);

        Item item111 = new Item(1, "item1.1.1", 10.);
        item11.add(item111);

        Item item12 = new Item(1, "item1.2", 10.);
        budgetRoot.add(item12);

        Item item112 = new Item(1, "item1.1.2", 1.);
        item11.add(item112);

        Item item1121 = new Item(1, "item1.1.2.1", 10.);
        item112.add(item1121);
        printBudgets();

        assertFalse(budgetRoot.remove(item11));

        assertTrue(budgetRoot.remove(item12));

        printBudgets();
    }


    private void printBudgets() {
        System.out.println("\n");
        controller.getBudgets().forEach(System.out::println);
        System.out.println("\n");
    }
}