package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;


public class RootLayoutController implements OverviewController {

    private DesktopApp desktopApp;

    /**
     * Open DashBoard overview
     */
    @FXML
    private void handleDashBoard() {
        desktopApp.showDashBoard();
    }

    /**
     * Opens Accounts overview
     */
    @FXML
    private void handleAccounts() {
        desktopApp.showAccountsOverview();
    }

    /**
     * Opens Transactions overview
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
     * Opens file chooser.
     */
    @SuppressWarnings("SpellCheckingInspection")
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
     * Saves a file.
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
     * opens file chooser to select a file to save.
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
     * Opens about window.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(desktopApp.TITLE);
        alert.setHeaderText("About");
        alert.setContentText("Version : " + desktopApp.VERSION +
                " \nAuthor: Bruno Rego\nWebsite: http://brunfo.github.io\n");
        alert.showAndWait();
    }

    /**
     * Closes the app.
     */
    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(desktopApp.TITLE);
        alert.setHeaderText("Exit");
        alert.setContentText("Are you sure that you want to exit?");
        if (alert.showAndWait().get().getButtonData().isDefaultButton()) {
            System.exit(0);
        }
    }

    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;
    }
}