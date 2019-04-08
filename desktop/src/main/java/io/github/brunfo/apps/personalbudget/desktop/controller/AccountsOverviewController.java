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
        //Inicializa a tabela de contas
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    /**
     * É chamado pela aplicação principal para referenciar a si mesma.
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
        Account tempAccount = new Account();
        boolean okClicked = desktopApp.showAccountEditDialog(tempAccount);
        if (okClicked) {
            desktopApp.saveAccount(tempAccount);
        }
    }

    @FXML
    private void handleEditAccount() {
        Account selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            boolean okClicked = desktopApp.showAccountEditDialog(selectedAccount);
            if (okClicked)
                desktopApp.updateAccount(selectedAccount);
        } else {
            noSelection();
        }
    }

    @FXML
    private void handleDeleteAccount() {
        Account account = accountTableView.getSelectionModel().getSelectedItem();
        if (account != null) {
            desktopApp.deleteAccount(account);
            accountTableView.getItems().remove(account);
        } else {
            noSelection();
        }
    }

    /**
     * Mensagem de erro quando nada está selecionado.
     */
    private void noSelection() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nenhuma seleção");
        alert.setHeaderText("Nenhuma conta selecionada");
        alert.setContentText("Por favor, selecione uma conta na tabela.");
        alert.showAndWait();
    }
}
