package io.github.brunfo.apps.personalbudget.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Model for a Transaction.
 *
 * @author Bruno Rego
 */

public class Transaction {

    private final IntegerProperty id;
    private final IntegerProperty accountId;
    private final ObjectProperty<LocalDate> operationDate;
    private final ObjectProperty<LocalDate> transactionDate;
    private final StringProperty description;
    private final ObjectProperty<Double> amount;
    private final ObjectProperty<Double> balance;
    private Map<Item, Double> budgetItems;

    /**
     * Constructor
     */
    public Transaction() {
        this(0, 0, LocalDate.now(), LocalDate.now(), null, 0);
    }

    /**
     * Constructor width initial data.
     *
     * @param id              ID of transaction.
     * @param operationDate   Date of register transaction.
     * @param transactionDate Date of transaction.
     * @param description     Description.
     * @param amount          Amount.
     */
    public Transaction(Integer id,
                       Integer accountId,
                       LocalDate operationDate,
                       LocalDate transactionDate,
                       String description,
                       double amount) {
        this(id, accountId, operationDate, transactionDate, description, amount, new HashMap<>());
    }

    /**
     * Constructor width initial data.
     *
     * @param id              ID of transaction.
     * @param operationDate   Date of register transaction.
     * @param transactionDate Date of transaction.
     * @param description     Description.
     * @param amount          Amount.
     * @param budgetItems     HashMap of budget items.
     */
    @SuppressWarnings("unchecked")
    public Transaction(Integer id,
                       Integer accountId,
                       LocalDate operationDate,
                       LocalDate transactionDate,
                       String description,
                       double amount, Map budgetItems) {
        this.id = new SimpleIntegerProperty(id);
        this.accountId = new SimpleIntegerProperty(accountId);
        this.operationDate = new SimpleObjectProperty<>(operationDate);
        this.transactionDate = new SimpleObjectProperty<>(transactionDate);
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleObjectProperty<>(amount);
        this.balance = new SimpleObjectProperty<>(0.0);
        this.budgetItems = budgetItems;
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public Integer getAccountId() {
        return accountId.get();
    }

    public void setAccountId(Integer accountId) {
        this.accountId.set(accountId);
    }

    public IntegerProperty accountIdProperty() {
        return accountId;
    }

    public LocalDate getOperationDate() {
        return operationDate.get();
    }

    public void setOperationDate(LocalDate operationDate) {
        this.operationDate.set(operationDate);
    }

    public ObjectProperty<LocalDate> operationDateProperty() {
        return operationDate;
    }

    public LocalDate getTransactionDate() {
        return transactionDate.get();
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate.set(transactionDate);
    }

    public ObjectProperty<LocalDate> transactionDateProperty() {
        return transactionDate;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public Double getAmount() {
        return amount.get();
    }

    public void setAmount(Double amount) {
        this.amount.set(amount);
    }

    public ObjectProperty<Double> amountProperty() {
        return amount;
    }

    public void setBalance(double balance) {
        this.balance.setValue(balance);
    }

    public Map<Item, Double> getBudgetItems() {
        return budgetItems;
    }

    @SuppressWarnings("unused")
    public void setBudgetItems(Map<Item, Double> budgetItems) {
        this.budgetItems = budgetItems;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                " " + operationDate.get() +
                ", " + transactionDate.get() +
                ", " + description.get() +
                ", " + amount.get() +
                ", " + balance.get() +
                '}';
    }
}
