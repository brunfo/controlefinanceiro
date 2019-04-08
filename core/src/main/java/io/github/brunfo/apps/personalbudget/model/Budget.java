package io.github.brunfo.apps.personalbudget.model;

import java.util.ArrayList;
import java.util.List;

public class Budget {

    List<Item> items;
    private int id;
    private String name;

    public Budget(int id, String name) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
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

    public List<Item> getItems() {
        return items;
    }
}
