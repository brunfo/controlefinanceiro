package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.MainApp;
import io.github.brunfo.apps.controlefinanceiro.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class AccountEditDialogController implements EditDialogController{

    @FXML
    private TextField nameTextField;

    @FXML
    private Label idLabel;

    private Stage dialogStage;
    private Account account;
    private boolean okClicked = false;
    private MainApp mainApp;


    /**
     * Inicializa a classe controlle. Este método é chamado automaticamente
     * após o arquivo fxml ter sido carregado.
     */
    @FXML
    private void initialize() {
    }


    /**
     * Define o palco deste dialog.
     *
     * @param dialogStage
     */
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
     * @param account
     */
    public void setAccount(Account account) {
        this.account = account;
        idLabel.setText(String.valueOf(account.getId()));
        nameTextField.setText(account.getName());
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
            account.setName(nameTextField.getText());

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

        if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        //verifica se nome já está em uso
        for (Account _account : mainApp.getAccounts()) {
            if (Objects.requireNonNull(nameTextField.getText()).equalsIgnoreCase(_account.getName()))
                errorMessage = "Nome inválido, já em uso!\n";
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

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }
}
