package io.github.brunfo.apps.controlefinanceiro.model;

import javafx.beans.property.*;

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

    public static Account get(int idAccount) {
        if (Accounts.get().size() < 1) {
            Account account = Accounts.get(idAccount);
            if (account == null)
                account = new Account(idAccount,  "Account: " + idAccount);
            Accounts.add(account);
        }
        return Accounts.get(idAccount);
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
