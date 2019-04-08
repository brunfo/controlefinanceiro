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

    private static int lastEditedAccount = -1;
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
    private int predefinedAccount;
    private DesktopApp desktopApp;


    /**
     * Inicializa a classe controlle. Este método é chamado automaticamente
     * após o arquivo fxml ter sido carregado.
     */
    @SuppressWarnings("EmptyMethod")
    @FXML
    private void initialize() {

    }


    /**
     * Define o palco deste dialog.
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
     * Define a pessoa a ser editada no dialog.
     *
     * @param transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        accountComboBox.setValue(desktopApp.getAccounts().get(transaction.getAccountId()));
        operationDatePicker.setValue(transaction.getOperationDate());
        transactionDatePicker.setValue(transaction.getTransactionDate());
        descriptionTextField.setText(transaction.getDescription());
        amountTextField.setText(String.valueOf(transaction.getAmount()));
    }

    /**
     * Retorna true se o usuário clicar OK,caso contrário false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Chamado quando o usuário clica OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            transaction.setAccountId(accountComboBox.getValue().getId());
            transaction.setOperationDate(operationDatePicker.getValue());
            transaction.setTransactionDate(transactionDatePicker.getValue());
            transaction.setDescription(descriptionTextField.getText());
            transaction.setAmount(Double.parseDouble(amountTextField.getText()));

            lastEditedAccount = accountComboBox.getSelectionModel().getSelectedIndex();
            okClicked = true;

            dialogStage.close();
        }
    }

    /**
     * Chamado quando o usuário clica Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valida a entrada do usuário nos campos de texto.
     *
     * @return true se a entrada é válida
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (accountComboBox.getValue() == null) {
            errorMessage += "Account inválida!\n";
        }
        if (operationDatePicker.getValue() == null) {
            errorMessage += "Data inválida!\n";
        } else {
            if (DateUtil.isValidDate(operationDatePicker.getValue().toString())) {
                errorMessage += "Data transaction inválida. Use o formato " + DateUtil.getDateFormat() + "!\n";
            }
        }
        if (transactionDatePicker.getValue() == null) {
            errorMessage += "Data inválida!\n";
        } else {
            if (DateUtil.isValidDate(transactionDatePicker.getValue().toString())) {
                errorMessage += "Data transaction inválida. Use o formato " + DateUtil.getDateFormat() + "!\n valor: " + transactionDatePicker.getPromptText();
            }
        }
        if (amountTextField.getText() == null || amountTextField.getText().length() == 0) {
            errorMessage += "Montante inválido!\n";
        } else {
            // tenta converter o amountTextField em um double.
            try {
                Double.parseDouble(amountTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Montante inválido (deve ser um numero)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostra a mensagem de erro.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, corrija os campos inválidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    public void setAvailableAccounts(ObservableList<Account> accounts) {
        accountComboBox.setItems(accounts);
        accountComboBox.setValue(accounts.get(predefinedAccount));
        //se já houve uma conta transaction anterior, coms eleção de conta, mantem a mesma
        if (lastEditedAccount != -1)
            accountComboBox.setValue((accounts.get(lastEditedAccount)));
    }

    public void setPredefinedAccount(int index) {
        predefinedAccount = index;
    }

    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;
    }
}
