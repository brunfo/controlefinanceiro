package io.github.brunfo.apps.controlefinanceiro.model;

import io.github.brunfo.apps.controlefinanceiro.util.LocalDateAdapter;
import javafx.beans.property.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Classe Model para um Transaction.
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

    /**
     * Contrutor padrão
     */
    public Transaction() {
        this(0, null, null, null, null, 0);
    }

    /**
     * Contrutor com dados iniciais.
     *
     * @param id            ID da Operação.
     * @param operationDate  Data do registro da operação.
     * @param transactionDate Data do movimento.
     * @param description     Descrição da operação.
     * @param amount      Montante movimentado.
     */
    public Transaction(Integer id,
                       Integer accountId,
                       LocalDate operationDate,
                       LocalDate transactionDate,
                       String description,
                       double amount) {

        this.id = new SimpleIntegerProperty(id);
        this.accountId = new SimpleIntegerProperty(accountId);
        this.operationDate = new SimpleObjectProperty<>(operationDate);
        this.transactionDate = new SimpleObjectProperty<>(transactionDate);
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleObjectProperty<>(amount);
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


    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getOperationDate() {
        return operationDate.get();
    }

    public void setOperationDate(LocalDate operationDate) {
        this.operationDate.set(operationDate);
    }

    public ObjectProperty<LocalDate> operationDateProperty() {
        return operationDate;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
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


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", operationDate=" + operationDate +
                ", transactionDate=" + transactionDate +
                ", description=" + description +
                ", amount=" + amount +
                '}';
    }
}
