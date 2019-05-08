package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Item;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Budgets {

    private final List<Item> budgets = new ArrayList<>();

    List<Item> get() {
        return budgets;
    }

    boolean addBudget(Item tempBudget) {
        return budgets.add(tempBudget);
    }

    boolean removeBudget(String budget) {
        return budgets.remove(getBudget(budget));
    }

    Item getBudget(String name) {
        for (Item budget : budgets) {
            if (budget.getName().equals(name))
                return budget;
        }
        return null;
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

    boolean addItemToBudget(Item item, String budget) {
        return getBudget(budget).getItems().add(item);
    }

    boolean removeItemFromBudget(Item item, String budget) {
        return getBudget(budget).getItems().remove(item);
    }
}
