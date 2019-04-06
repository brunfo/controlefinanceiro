package io.github.brunfo.apps.personalbudget.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Classe Model para uma Account.
 *
 * @author Bruno Rego
 */
public class Account {

    private final ObjectProperty<Integer> id;
    private final StringProperty name;

    /**
     * Construtor padrão
     */
    public Account() {
        this(0, null);
    }

    /**
     * Construtor com dados iniciais.
     *
     * @param id   ID da Account.
     * @param name Nome da conta
     */
    public Account(int id, String name) {
        this.id = new SimpleObjectProperty<>(id);
        this.name = new SimpleStringProperty(name);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public ObjectProperty<Integer> idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
