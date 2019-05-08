package io.github.brunfo.apps.personalbudget.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class item design width Composite Pattern (GOF).
 * It holds budget items, that can hold child budget items.
 */
public class Item {

    private List<Item> items;
    private int id;
    private String name;
    private double amount;

    public Item(int id, String name, double amount) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the list only if the item has no child items.
     *
     * @param item Item to remove.
     * @return true if removes it successfully.
     */
    public boolean remove(Item item) {
        if (item.getItems().isEmpty()) {
            items.remove(item);
            return true;
        }
        return false;
    }

    /**
     * List of children.
     *
     * @return ArrayList of children items.
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Return the amount. If it has child item, them return the sum of the amount value of the children.
     *
     * @return Double value of the amount.
     */
    public double getAmount() {
        if (!items.isEmpty()) {
            amount = items.stream().mapToDouble(Item::getAmount).sum();
        }
        return amount;
    }

    @Override
    public String toString() {
        return getId() + " : " + getName() +
                " Items: " +
                items +
                " Amount: " + getAmount();
    }
}
