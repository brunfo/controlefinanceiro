package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;
import io.github.brunfo.apps.personalbudget.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class AccountsOverviewController implements OverviewController {

    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Account, String> nameColumn;
    @FXML
    private TableColumn<Account, Integer> idColumn;
    private DesktopApp desktopApp;

    @FXML
    void initialize() {
        //Initialize table view
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    /**
     * Called be desktop app to reference it self.
     *
     * @param controller
     */
    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;
        accountTableView.setItems(this.desktopApp.getAccounts());
    }

    @FXML
    private void handleNewAccount() {
        Account tempAccount = new Account(null);
        boolean okClicked = desktopApp.showAccountEditDialog(tempAccount);
        if (okClicked) {
            desktopApp.addAccount(tempAccount);
        }
    }

    @FXML
    private void handleEditAccount() {
        Account selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            boolean okClicked = desktopApp.showAccountEditDialog(selectedAccount);
            if (okClicked)
                desktopApp.editAccount(selectedAccount);
        } else {
            noSelection();
        }
    }

    @FXML
    private void handleDeleteAccount() {
        Account account = accountTableView.getSelectionModel().getSelectedItem();
        if (account != null) {
            desktopApp.removeAccount(account);
            accountTableView.getItems().remove(account);
        } else {
            noSelection();
        }
    }

    /**
     * Error message when nothing is selected..
     */
    private void noSelection() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No selection");
        alert.setHeaderText("No account selected");
        alert.setContentText("Please, select a account.");
        alert.showAndWait();
    }
}
