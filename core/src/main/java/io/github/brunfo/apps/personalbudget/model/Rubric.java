package io.github.brunfo.apps.personalbudget.model;

public class Rubric {
    private int id;
    private String name;


    public Rubric(int id, String name) {
        this(name);
        setId(id);
    }

    public Rubric(String name) {
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
