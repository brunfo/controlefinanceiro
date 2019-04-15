package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;
import io.github.brunfo.apps.personalbudget.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class AccountEditDialogController implements EditDialogController {

    @FXML
    private TextField nameTextField;

    @FXML
    private Label idLabel;

    private Stage dialogStage;
    private Account account;
    private boolean okClicked = false;
    private DesktopApp desktopApp;


    /**
     * Initialize the controller class. This method is called automatically
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }


    /**
     * Defines the stage of this dialog.
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
     * Defines a account to be edited.
     *
     * @param account
     */
    public void setAccount(Account account) {
        this.account = account;
        idLabel.setText(String.valueOf(account.getId()));
        nameTextField.setText(account.getName());
    }

    /**
     * Return true if user click OK, otherwise false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when user click OK.
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
     * Called when user click Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validate user input in text fields.
     *
     * @return true if input is valid.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
            errorMessage += "Invalid name!\n";
        }
        //verifies if name is in use
        for (Account _account : desktopApp.getAccounts()) {
            if (Objects.requireNonNull(nameTextField.getText()).equalsIgnoreCase(_account.getName()))
                errorMessage = "Invalid name, already in use!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid fields!");
            alert.setHeaderText("Please, correct invalid input.");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;

    }
}
