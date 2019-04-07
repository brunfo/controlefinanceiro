package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Budget;
import io.github.brunfo.apps.personalbudget.model.Family;
import io.github.brunfo.apps.personalbudget.model.Item;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Budgets {

    private final List<Budget> budgets = new ArrayList<>();

    boolean addBudget(Budget tempBudget) {
        return budgets.add(tempBudget);
    }

    boolean removeBudget(Budget budget) {
        return budgets.remove(budget);
    }

    Budget getBudget(String name) {
        for (Budget budget : budgets) {
            if (budget.getName().equals(name))
                return budget;
        }
        return null;
    }

    Budget getBudgetById(int id) {
        for (Budget budget : budgets) {
            if (budget.getId() == id)
                return budget;
        }
        return null;
    }

    List<Item> getFamilyItemsFromBuget(Family family, Budget budget) {
        List<Item> items = new ArrayList<>();
        for (Item item : budget.getItems()) {
            if (item.getFamily() == family)
                items.add(item);
        }
        return items;
    }

    /**
     * Splits a transaction in two different items from the budget.
     * If the item's value equals 0 after split, it will be removed, behaving like replace items.
     *
     * @param transaction transaction.
     * @param item        Item to split.
     * @param newItem     second item.
     * @param amount      Amount for second item.
     * @return true if success.
     */
    boolean splitTransactionByBudgetItems(Transaction transaction, Item item, Item newItem, double amount) {
        Map<Item, Double> itemsMap = transaction.getBudgetItems();
        double oldValue = itemsMap.get(item);
        if (amount <= oldValue) {
            double newValue = oldValue - amount;
            itemsMap.replace(item, oldValue, newValue);
            itemsMap.put(newItem, amount);
            if (newValue == 0)
                itemsMap.remove(item);
            return true;
        }
        return false;
    }
}
