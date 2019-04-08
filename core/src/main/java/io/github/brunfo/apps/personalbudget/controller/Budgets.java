package io.github.brunfo.apps.personalbudget.controller;

import io.github.brunfo.apps.personalbudget.model.Budget;
import io.github.brunfo.apps.personalbudget.model.Family;
import io.github.brunfo.apps.personalbudget.model.Item;
import io.github.brunfo.apps.personalbudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Budgets {

    private final List<Budget> budgets = new ArrayList<>();

    boolean addBudget(Budget tempBudget) {
        return budgets.add(tempBudget);
    }

    boolean removeBudget(String budget) {
        return budgets.remove(getBudget(budget));
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

    List<Item> getItemsByFamily(Family family, String budget) {
        List<Item> items = new ArrayList<>();
        for (Item item : getBudget(budget).getItems()) {
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

    boolean addItemToBudget(Item item, String budget) {
        return getBudget(budget).getItems().add(item);
    }

    boolean removeItemFromBudget(Item item, String budget) {
        return getBudget(budget).getItems().remove(item);
    }
}
