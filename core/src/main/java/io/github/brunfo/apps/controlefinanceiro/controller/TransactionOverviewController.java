package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.MainApp;
import io.github.brunfo.apps.controlefinanceiro.model.Account;
import io.github.brunfo.apps.controlefinanceiro.model.Transaction;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class TransactionOverviewController implements OverviewController {


    private static int lastEditedAccount = -1;
    @FXML
    private TableView<Transaction> transactionTableView;
    @FXML
    private TableColumn<String, Double> balanceColumn;
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
    private int predefinedAccount;
    private int accountIdSelected = 0;
    private MainApp mainApp;

    /**
     * O construtor.
     * O construtor é chamado antes do método inicialize().
     */
    public TransactionOverviewController() {
    }


    /**
     * Inicializa a classe controller. Este método é chamado automaticamente
     * após o arquivo fxml ter sido carregado.
     */
    @FXML
    private void initialize() {

        // Inicializa a tabela de movimentos.
        operationDateColumn.setCellValueFactory(
                cellData -> cellData.getValue().operationDateProperty());
        transactionDateColumn.setCellValueFactory(
                cellData -> cellData.getValue().transactionDateProperty());
        descriptionColumn.setCellValueFactory(
                cellData -> cellData.getValue().descriptionProperty());
        amountColumn.setCellValueFactory(
                cellData -> cellData.getValue().amountProperty());

        //change listview observable list
        transactionTableView.getSelectionModel().getSelectedIndices().
                addListener((ListChangeListener<Integer>) c -> handleSelectTransaction());
    }


    /**
     * É chamado pela aplicação principal para dar uma referência de volta a si mesmo.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp =mainApp;
        updateData();
    }

    /**
     * Preenche todos os campos de texto para mostrar detalhes sobre a pessoa.
     * Se a pessoa especificada for null, todos os campos de texto são limpos.
     *
     * @param transaction a pessoa ou null
     */
    private void showTransactionDetails(Transaction transaction) {
        if (transaction != null) {
            // Preenche as labels com informações do objeto transaction.
            idLabel.setText((String.valueOf((transaction.getId()))));
            accountLabel.setText(transaction.getAccount().getName());
            operationDateLabel.setText(String.valueOf(transaction.getOperationDate()));
            transactionDateLabel.setText(String.valueOf(transaction.getTransactionDate()));
            descriptionLabel.setText(transaction.getDescription());
            amountLabel.setText(String.valueOf(transaction.getAmount()));
        } else {
            // Transaction é null, remove todo o texto.
            idLabel.setText("");
            accountLabel.setText("");
            operationDateLabel.setText("");
            transactionDateLabel.setText("");
            descriptionLabel.setText("");
            amountLabel.setText("");
        }

    }

    /**
     * Chamado quando o usuário clica no botão delete.
     */
    @FXML
    private void handleDeleteTransaction() {
        int selectedIndex = transactionTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Transaction transaction = transactionTableView.getSelectionModel().getSelectedItem();
            mainApp.deleteTransactionFromDataBase(transaction);
            //mainApp.getTransactions().remove(transaction);
            transactionTableView.getItems().remove(selectedIndex);
        } else {
            noSelection();
        }
    }

    /**
     * Chamado quando o usuário clica no botão novo. Abre uma janela para editar
     * detalhes do novo movimento.
     */
    @FXML
    private void handleNewMovimento() {
        Transaction tempTransaction = new Transaction(0,
                //TODO gerir criação de conta
                null,//transactionTableView.getItems().get(0).getAccount(),
                LocalDate.now(),
                LocalDate.now(),
                "",
                0.0);
        boolean okClicked = mainApp.showTransactionEditDialog(tempTransaction);
        if (okClicked) {
            mainApp.saveTransaction(tempTransaction);
        }
    }

    /**
     * Chamado quando o usuário clica no botão edit. Abre a janela para editar
     * detalhes de um movimento selecionado.
     */
    @FXML
    private void handleEditTransaction() {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            boolean okClicked = mainApp.showTransactionEditDialog(selectedTransaction);
            if (okClicked) {
                showTransactionDetails(selectedTransaction);
                mainApp.updateTransactionToDataBase(selectedTransaction);
            }

        } else {
            noSelection();

        }
    }

    @FXML
    private void handleSelectTransaction() {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        showTransactionDetails(selectedTransaction);
    }

    @FXML
    private void nextAccount() {
        accountIdSelected = accountComboBox.getSelectionModel().getSelectedItem().getId();
        updateData();
    }

    private boolean test(Transaction filter) {
        return filter.getAccount().getId() == accountIdSelected;
    }

    private void updateData() {
        // Adiciona os dados da observable list na tabela
        transactionTableView.setItems(mainApp.getTransactions().filtered(this::test));
    }


    /**
     * Mensagem de erro quando nada está selecionado.
     */
    private void noSelection() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nenhuma seleção");
        alert.setHeaderText("Nenhuma movimento selecionado");
        alert.setContentText("Por favor, selecione um movimento na tabela.");
        alert.showAndWait();
    }

    public void setAvailableAccounts(ObservableList<Account> accountsData) {
        accountComboBox.setItems(accountsData);
        accountComboBox.setValue(accountsData.get(predefinedAccount));
        //se já houve uma conta movimento anterior, coms eleção de conta, mantem a mesma
        if (lastEditedAccount != -1) {
            accountComboBox.setValue((accountsData.get(lastEditedAccount)));
        }
    }

    public void setPredefinedAccount(int index) {
        predefinedAccount = index;
    }

}