package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;
import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import io.github.brunfo.apps.personalbudget.util.DateUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TransactionEditDialogController implements EditDialogController {

    @FXML // fx:id="descriptionTextField"
    private TextField descriptionTextField;
    @FXML // fx:id="accountComboBox"
    private ComboBox<Account> accountComboBox;
    @FXML // fx:id="operationDatePicker"
    private DatePicker operationDatePicker;
    @FXML // fx:id="transactionDatePicker"
    private DatePicker transactionDatePicker;
    @FXML // fx:id="amountTextField"
    private TextField amountTextField;

    private Stage dialogStage;
    private Transaction transaction;
    private boolean okClicked = false;
    private DesktopApp desktopApp;



    @SuppressWarnings("EmptyMethod")
    @FXML
    private void initialize() {

    }


    /**
     * Defines the stage to this dialog.
     *
     * @param dialogStage
     */
    @Override
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @Override
    public Stage getDialogStageStage() {
        return dialogStage;
    }


    /**
     * Defines the transaction
     *
     * @param transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        accountComboBox.setValue(desktopApp.getAccountById(transaction.getAccountId()));
        operationDatePicker.setValue(transaction.getOperationDate());
        transactionDatePicker.setValue(transaction.getTransactionDate());
        descriptionTextField.setText(transaction.getDescription());
        amountTextField.setText(String.valueOf(transaction.getAmount()));
    }

    public boolean isOkClicked() {
        return okClicked;
    }


    @FXML
    private void handleOk() {
        if (isInputValid()) {
            transaction.setAccount(accountComboBox.getValue());
            transaction.setAccount(accountComboBox.getValue());
            transaction.setOperationDate(operationDatePicker.getValue());
            transaction.setTransactionDate(transactionDatePicker.getValue());
            transaction.setDescription(descriptionTextField.getText());
            transaction.setAmount(Double.parseDouble(amountTextField.getText()));
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (accountComboBox.getValue() == null) {
            errorMessage += "Invalid account!\n";
        }
        if (operationDatePicker.getValue() == null) {
            errorMessage += "Invalid date!\n";
        } else {
            if (DateUtil.isValidDate(operationDatePicker.getValue().toString())) {
                errorMessage += "Invalid date format. Use the format " + DateUtil.getDateFormat() + "!\n";
            }
        }
        if (transactionDatePicker.getValue() == null) {
            errorMessage += "Invalid date!\n";
        } else {
            if (DateUtil.isValidDate(transactionDatePicker.getValue().toString())) {
                errorMessage += "Invalid date format. Use the format " + DateUtil.getDateFormat() + "!\n value: " + transactionDatePicker.getPromptText();
            }
        }
        if (amountTextField.getText() == null || amountTextField.getText().length() == 0) {
            errorMessage += "Invalid amount!\n";
        } else {
            // tries to convert amountTextField in a double.
            try {
                Double.parseDouble(amountTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid amount (must be a number)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid fields");
            alert.setHeaderText("Please, correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    public void setAvailableAccounts(ObservableList<Account> accounts) {
        accountComboBox.setItems(accounts);
        accountComboBox.setValue(accounts.get(accounts.indexOf(transaction.getAccount())));
    }

    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;
    }
}
