package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;


/**
 * O controlador para o root layout. O root layout provê um layout básico
 * para a aplicação contendo uma barra de menu e um espaço onde outros elementos
 * JavaFX podem ser colocados.
 *
 * @author Bruno Rego
 */
public class RootLayoutController implements OverviewController {

    private MainApp mainApp;

    /**
     * Abre o DashBoard
     */
    @FXML
    private void handleDashBoard() {
        mainApp.showDashBoard();
    }

    /**
     * Abre o Accounts
     */
    @FXML
    private void handleAccounts() {
        mainApp.showAccountsOverview();
    }

    /**
     * Abre o Transactions
     */
    @FXML
    private void handleTransactions() {
        mainApp.showTransactionsOverview();
    }

    @FXML
    private void handleSetup() {
        mainApp.showSetup();
    }

    @FXML
    private void handleBudget() {
        mainApp.showBudget();
    }


    /**
     * Abre o FileChooser para permitir o usuário selecionar uma agenda
     * para carregar.
     */
    @FXML
    private void handleOpen() {
//        FileChooser fileChooser = new FileChooser();
//
//        // Define um filtro de extensão
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        // Mostra a janela de salvar arquivo
//        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
//
//        if (file != null) {
//            mainApp.loadTransactionDataFromFile(file);
//        }
    }

    /**
     * Salva o arquivo para o arquivo de pessoa aberto atualmente. Se não houver
     * arquivo aberto, a janela "salvar como" é mostrada.
     */
    @FXML
    private void handleSave() {
//        File personFile = mainApp.getTransactionFilePath();
//        if (personFile != null) {
//            mainApp.saveTransactionDataToFile(personFile);
//        } else {
//            handleSaveAs();
//        }
    }

    /**
     * Abre um FileChooser para permitir o usuário selecionar um arquivo
     * para salvar.
     */
    @FXML
    private void handleSaveAs() {
//        FileChooser fileChooser = new FileChooser();
//
//        // Define o filtro de extensão
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        // Mostra a janela para salvar arquivo
//        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
//
//        if (file != null) {
//            // Certifica de que esta é a extensão correta
//            if (!file.getPath().endsWith(".xml")) {
//                file = new File(file.getPath() + ".xml");
//            }
//            mainApp.saveTransactionDataToFile(file);
//        }
    }

    /**
     * Abre uma janela Sobre.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(mainApp.TITLE);
        alert.setHeaderText("Sobre");
        alert.setContentText("Version : " + mainApp.VERSION +
                " \nAutor: Bruno Rego\nWebsite: http://brunfo.github.io\n");
        alert.showAndWait();
    }

    /**
     * Fecha a aplicação.
     */
    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(mainApp.TITLE);
        alert.setHeaderText("Sair");
        alert.setContentText("Tem a certeza que quer sair?");
        if (alert.showAndWait().get().getButtonData().isDefaultButton()) {
            System.exit(0);
        }
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}