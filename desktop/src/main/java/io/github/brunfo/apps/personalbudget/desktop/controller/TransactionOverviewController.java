package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;
import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class TransactionOverviewController implements OverviewController {

    //preferences
    private static int preferredAccountId;
    //reference to the desktop app controller
    private DesktopApp desktopApp;
    //accounts
    private ObservableList<Account> accountObservableList;

    @FXML
    private TableView<Transaction> transactionTableView;
    @FXML
    private TableColumn<Transaction, Double> balanceColumn;
    @FXML
    private TableColumn<Transaction, LocalDate> operationDateColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    @FXML
    private TableColumn<Transaction, LocalDate> transactionDateColumn;
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label operationDateLabel;
    @FXML
    private Label transactionDateLabel;


    /**
     * The Constructor
     * The constructor is called before initialize() method.
     */
    public TransactionOverviewController() {
    }


    /**
     * Initializes the class of the controller.
     * This method is called after the FXML file is loaded.
     */
    @FXML
    private void initialize() {
        // Initializes the TableView columns.
        operationDateColumn.setCellValueFactory(
                cellData -> cellData.getValue().operationDateProperty());
        transactionDateColumn.setCellValueFactory(
                cellData -> cellData.getValue().transactionDateProperty());
        descriptionColumn.setCellValueFactory(
                cellData -> cellData.getValue().descriptionProperty());
        amountColumn.setCellValueFactory(
                cellData -> cellData.getValue().amountProperty());
        balanceColumn.setCellValueFactory(
                cellData -> cellData.getValue().balanceProperty());

        //adds a listener to the table content
        transactionTableView.getSelectionModel().getSelectedIndices().
                addListener((ListChangeListener<Integer>) c -> handleSelectTransaction());
    }

    /**
     * Sets a reference to the caller.
     *
     * @param controller caller.
     */
    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;
        //updateData();
    }

    /**
     * Fills all text fields to show details of a transaction.
     *
     * @param transaction a transaction
     */
    private void showTransactionDetails(Transaction transaction) {
        if (transaction != null) {
            // Fills the labels with transaction object information.
            idLabel.setText((String.valueOf((transaction.getId()))));
            String accountName = null;
            for (Account p : desktopApp.getAccounts()) {
                if (p.getId() == transaction.getAccountId()) {
                    accountName = p.getName();
                }
            }
            accountLabel.setText(accountName);
            operationDateLabel.setText(String.valueOf(transaction.getOperationDate()));
            transactionDateLabel.setText(String.valueOf(transaction.getTransactionDate()));
            descriptionLabel.setText(transaction.getDescription());
            amountLabel.setText(String.valueOf(transaction.getAmount()));
        } else {
            // Transaction is null, clear all text.
            idLabel.setText("");
            accountLabel.setText("");
            operationDateLabel.setText("");
            transactionDateLabel.setText("");
            descriptionLabel.setText("");
            amountLabel.setText("");
        }
    }

    /**
     * Called when the user clicks the delete button.
     */
    @FXML
    private void handleDeleteTransaction() {
        int selectedIndex = transactionTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Transaction transaction =
                    transactionTableView.getSelectionModel().getSelectedItem();
            desktopApp.removeTransaction(transaction);
            transactionTableView.getItems().remove(selectedIndex);
        } else {
            noSelection();
        }
    }

    /**
     * Called when the user clicks the new button.
     * Opens a window to edit details of the new transaction.
     */
    @FXML
    private void handleNewTransaction() {
        //creates new transaction
        Transaction tempTransaction = new Transaction();
        //sets the account id of the active value of the combo box
        tempTransaction.setAccountId(
                accountComboBox.getSelectionModel().getSelectedItem().getId());
        boolean okClicked = desktopApp.showTransactionEditDialog(tempTransaction);
        if (okClicked) {
            //adds transaction to the storage
            desktopApp.addTransaction(tempTransaction);
            //sets combo box width the account of the new transaction
            accountComboBox.setValue(tempTransaction.getAccount());
            //refresh the table view
            refreshTableView(
                    accountObservableList.indexOf(tempTransaction.getAccount()));
        }
    }

    /**
     * Called when the user clicks the edit button.
     * Opens the edit window details of a selected transaction.
     */
    @FXML
    private void handleEditTransaction() {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            //saves the old account
            Account oldAccount = selectedTransaction.getAccount();
            boolean okClicked = desktopApp.showTransactionEditDialog(selectedTransaction);
            if (okClicked) {
                //edits the transaction
                desktopApp.editTransaction(selectedTransaction);
                //removes the transaction on the old account if is different
                if (!oldAccount.equals(selectedTransaction.getAccount()))
                    oldAccount.getTransactions().remove(selectedTransaction);
                //sets combo box width the account of the new transaction
                accountComboBox.setValue(selectedTransaction.getAccount());
                //refresh the table view
                refreshTableView(
                        accountObservableList.indexOf(selectedTransaction.getAccount()));
            }
        } else {
            noSelection();
        }
    }

    /**
     * Called when user selects a transaction.
     * It shows more details of the selected transaction.
     */
    @FXML
    private void handleSelectTransaction() {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            showTransactionDetails(selectedTransaction);
        }
    }

    /**
     * This method is called when the user change the selection of the combo box.
     * Updates the content of the table view.
     */
    @FXML
    private void updateTableView() {
        //assigns the selected account
        int index = accountComboBox.getSelectionModel().getSelectedIndex();
        if (index >= 0)
            refreshTableView(index);
    }

    /**
     * Error message when nothing is selected.
     */
    private void noSelection() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No selection");
        alert.setHeaderText("No transaction selected");
        alert.setContentText("Please select a transaction in the table.");
        alert.showAndWait();
    }

    /**
     * Sets the available accounts.
     * Fills the combo box and shows the transactions of selected account.
     * Try to set the preferred account.
     *
     * @param accounts List of accounts.
     */
    public void setAvailableAccounts(ObservableList<Account> accounts) {
        this.accountObservableList = accounts;
        //sets combo box accountObservableList
        accountComboBox.setItems(accounts);

        //if preferred accountId is valid sets that account,
        // else sets the first in the list
        int accountIndex = accounts.indexOf(
                desktopApp.getAccountById(preferredAccountId));
        if (accountIndex < 0) {
            accountIndex = 0;
        }
        //sets the value to show
        accountComboBox.setValue(accounts.get(accountIndex));
        refreshTableView(accountIndex);
    }

    /**
     * Refresh data of TableView width the selected account
     *
     * @param accountIndex the account index
     */
    private void refreshTableView(int accountIndex) {
        transactionTableView.setItems(
                desktopApp.getTransactions(
                        accountObservableList.get(accountIndex).getId()));
        transactionTableView.getItems();
    }

    /**
     * Sets the preferred account.
     *
     * @param accountId the preferred account id.
     */
    public void setPreferredAccount(int accountId) {
        preferredAccountId = accountId;
    }

}
