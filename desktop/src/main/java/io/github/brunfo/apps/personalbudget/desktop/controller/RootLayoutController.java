package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.controller.MainController;
import io.github.brunfo.apps.personalbudget.controller.OverviewController;
import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;
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

    private DesktopApp desktopApp;

    /**
     * Abre o DashBoard
     */
    @FXML
    private void handleDashBoard() {
        desktopApp.showDashBoard();
    }

    /**
     * Abre o Accounts
     */
    @FXML
    private void handleAccounts() {
        desktopApp.showAccountsOverview();
    }

    /**
     * Abre o Transactions
     */
    @FXML
    private void handleTransactions() {
        desktopApp.showTransactionsOverview();
    }

    @FXML
    private void handleSetup() {
        desktopApp.showSetup();
    }

    @FXML
    private void handleBudget() {
        desktopApp.showBudget();
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
//        File file = fileChooser.showOpenDialog(desktopApp.getPrimaryStage());
//
//        if (file != null) {
//            desktopApp.loadTransactionDataFromFile(file);
//        }
    }

    /**
     * Salva o arquivo para o arquivo de pessoa aberto atualmente. Se não houver
     * arquivo aberto, a janela "salvar como" é mostrada.
     */
    @FXML
    private void handleSave() {
//        File personFile = desktopApp.getTransactionFilePath();
//        if (personFile != null) {
//            desktopApp.saveTransactionDataToFile(personFile);
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
//        File file = fileChooser.showSaveDialog(desktopApp.getPrimaryStage());
//
//        if (file != null) {
//            // Certifica de que esta é a extensão correta
//            if (!file.getPath().endsWith(".xml")) {
//                file = new File(file.getPath() + ".xml");
//            }
//            desktopApp.saveTransactionDataToFile(file);
//        }
    }

    /**
     * Abre uma janela Sobre.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(desktopApp.TITLE);
        alert.setHeaderText("Sobre");
        alert.setContentText("Version : " + desktopApp.VERSION +
                " \nAutor: Bruno Rego\nWebsite: http://brunfo.github.io\n");
        alert.showAndWait();
    }

    /**
     * Fecha a aplicação.
     */
    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(desktopApp.TITLE);
        alert.setHeaderText("Sair");
        alert.setContentText("Tem a certeza que quer sair?");
        if (alert.showAndWait().get().getButtonData().isDefaultButton()) {
            System.exit(0);
        }
    }

    @Override
    public void setMainController(MainController controller) {
        this.desktopApp = (DesktopApp) controller;
    }
}