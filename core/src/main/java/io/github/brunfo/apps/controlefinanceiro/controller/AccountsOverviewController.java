package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.MainApp;
import io.github.brunfo.apps.controlefinanceiro.model.Account;
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
    private MainApp mainApp;

    @FXML
    void initialize() {
        //Inicializa a tabela de contas
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    /**
     * É chamado pela aplicação principal para referenciar a si mesma.
     *
     * @param mainApp
     */
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp=mainApp;
        accountTableView.setItems(mainApp.getAccounts());
    }

    @FXML
    private void handleNewConta() {
        Account tempAccount = new Account();
        boolean okClicked = mainApp.showAccountEditDialog(tempAccount);
        if (okClicked) {
            mainApp.saveAccount(tempAccount);
        }

    }

    @FXML
    private void handleEditConta() {
        Account selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
        boolean okClicked = mainApp.showAccountEditDialog(selectedAccount);
        if (okClicked)
            mainApp.updateAccount(selectedAccount);
    }

    @FXML
    private void handleDeleteConta() {
        Account account = accountTableView.getSelectionModel().getSelectedItem();
        if (account != null) {
            mainApp.deleteAccountFromDataBase(account);
            accountTableView.getItems().remove(account);
        } else {
            nadaSelecionado();
        }

    }

    /**
     * Mensagem de erro quando nada está selecionado.
     */
    private void nadaSelecionado() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nenhuma seleção");
        alert.setHeaderText("Nenhuma movimento selecionado");
        alert.setContentText("Por favor, selecione uma conta na tabela.");
        alert.showAndWait();
    }
}
