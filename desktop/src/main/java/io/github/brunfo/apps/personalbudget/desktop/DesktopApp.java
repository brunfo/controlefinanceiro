package io.github.brunfo.apps.personalbudget.desktop;

import io.github.brunfo.apps.personalbudget.controller.Controller;
import io.github.brunfo.apps.personalbudget.desktop.controller.*;
import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Main class. This class is the main class that works as entry point,
 * as well as graphic controller of the entire App.
 * All views management as data has to go through this class.
 */
public class DesktopApp extends Application {

    public final String TITLE = "Personal Budget";
    public final String VERSION = "1.1-SNAPSHOT";
    /**
     * The data as an observable list of Transactions.
     */
    private final ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();
    //the Main Controller
    private Controller mainController = Controller.getInstance();
    private Stage primaryStage;
    private BorderPane rootLayout;
    private String preferredAccount = "preferredAccount";

    /**
     * Constructor
     */
    public DesktopApp() {
    }

    //******* Generic ********//
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(this.TITLE);

        // Set the application icon.
        //noinspection SpellCheckingInspection
        this.primaryStage.getIcons().add(new Image("/img/iconfinder_Address_Book_86957.png"));

        initRootLayout();

        showTransactionsOverview();
    }

    // ********** Config ***********//

    /**
     * Gets the primary stage of the app.
     *
     * @return primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    //******** Controllers view *********//

    /**
     * Initializes the root layout and attempts to load the data.
     */
    private void initRootLayout() {
        try {
            // Loads the layout of the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DesktopApp.class
                    .getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Shows the scene of the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Gives access to this class to the layout controller.
            RootLayoutController controller = loader.getController();
            controller.setDesktopApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Loads data from data base
        accountsObservableList.clear();
        accountsObservableList.addAll(mainController.getAccounts());
        updateData();
    }


    private OverviewController showOverview(String overview) {
        try {
            //Load the setup overview
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DesktopApp.class.getResource(overview));
            AnchorPane setup = loader.load();

            //Sets setup overview in the center of the root layout.
            rootLayout.setCenter(setup);

            //Gives access to this class to the layout controller.
            OverviewController controller = loader.getController();
            controller.setDesktopApp(this);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private EditDialogController showEditDialog(String editDialog, String title) {
        try {
            // Load the show edit overview as pop up window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DesktopApp.class.getResource(editDialog));
            AnchorPane page = loader.load();

            // Creates new stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Gives access to this class to the layout controller.
            EditDialogController controller = loader.getController();
            controller.setDesktopApp(this);
            controller.setDialogStage(dialogStage);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shows o Dashboard
     */
    public void showDashBoard() {
        showOverview("/view/DashBoard.fxml");
    }

    /**
     * Show transactions over view
     */
    public void showTransactionsOverview() {
        //sets the controller
        TransactionOverviewController controller =
                (TransactionOverviewController) showOverview("/view/TransactionsOverview.fxml");

        if (controller != null) {
            if (accountsObservableList.size() > 0) {
                //sets preferred account to the view controller
                controller.setPreferredAccount(getPreferredAccountId());
                //sets the available account to the view controller
                controller.setAvailableAccounts(this.getAccounts());
            }
        }
    }

    /**
     * Opens a window to edit details of a specified transaction. If the user clicks
     * OK, the changes are saved in the provided object and returns true.
     *
     * @param selectedTransaction The transaction object to be edited
     * @return true ff the user clicked OK, otherwise false.
     */
    public boolean showTransactionEditDialog(Transaction selectedTransaction) {
        if (accountsObservableList.size() > 0) {
            TransactionEditDialogController controller = (TransactionEditDialogController)
                    showEditDialog(
                            "/view/TransactionEditDialog.fxml",
                            "Edit transaction");
            if (controller != null) {
                // Sets the transaction on the overviewController.
                controller.setTransaction(selectedTransaction);

                //sets the available accounts to select
                controller.setAvailableAccounts(accountsObservableList);

                // Shows the window and waits until the user closes.
                controller.getDialogStageStage().showAndWait();

                return controller.isOkClicked();
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No account");
        alert.setHeaderText("Error");
        alert.setContentText("There is no account defined!\nTo add a transaction you have to have at least one account!");
        alert.showAndWait();
        return false;
    }

    /**
     * Shows the Accounts overview
     */
    public void showAccountsOverview() {
        showOverview("/view/AccountsOverview.fxml");
    }

    /**
     * Opens a popup window to edit details of a account.
     * If user click OK, saves the changes..
     *
     * @param selectedAccount The selected account.
     * @return true If user clicks OK,  otherwise false.
     */
    public boolean showAccountEditDialog(Account selectedAccount) {
        EditDialogController controller =
                showEditDialog(
                        "/view/AccountEditDialog.fxml",
                        "Edit Account");
        if (controller != null) {
            ((AccountEditDialogController) controller).setAccount(selectedAccount);

            // Shoes.
            controller.getDialogStageStage().showAndWait();

            return ((AccountEditDialogController) controller).isOkClicked();
        }
        return false;
    }

    /**
     * Show Setup overview
     */
    public void showSetup() {
        showOverview("/view/Setup.fxml");
    }

    public void showBudget() {
        showOverview("/view/Budget.fxml");
    }

    //***** data management *****//

    /**
     * Gets data from database.
     */
    private void updateData() {
        try {
            accountsObservableList.clear();
            accountsObservableList.addAll(mainController.getAccounts());
        } catch (Exception error) {
            System.out.println("Update data: " + error.getCause() + " : " + error.getMessage());
        }
    }

    ///ACCOUNTS

    public void addAccount(Account tempAccount) {
        mainController.addAccount(tempAccount);
        updateData();
    }

    public void editAccount(Account selectedAccount) {
        mainController.editAccount(selectedAccount);
        updateData();
    }

    public void removeAccount(Account selectedAccount) {
        mainController.removeAccount(selectedAccount);
        updateData();
    }

    public Account getAccountById(Integer accountId) {
        return mainController.getAccountById(accountId);
    }

    ///TRANSACTIONS

    public void addTransaction(Transaction tempTransaction) {
        mainController.addTransaction(tempTransaction);
        updateData();
    }

    public void editTransaction(Transaction selectedTransaction) {
        mainController.editTransaction(selectedTransaction);
        updateData();
    }

    public void removeTransaction(Transaction selectedTransaction) {
        mainController.removeTransaction(selectedTransaction);
        updateData();
    }

    //********** Manage data **********//

    /**
     * Returns data as Transaction observable list.
     *
     * @return ObservableList.
     */
    public ObservableList<Transaction> getTransactions(int accountIdSelected) {
        Account account = mainController.getAccountById(accountIdSelected);
        if (account != null) {
            transactionsObservableList.clear();
            transactionsObservableList.addAll(account.getTransactions());
            return transactionsObservableList;
        }
        return null;
    }

    /**
     * Returns Accounts as observable list.
     *
     * @return ObservableList.
     */
    public ObservableList<Account> getAccounts() {
        return accountsObservableList;
    }


    //************* Others ****************//

    /**
     * Get the preferred account id.
     *
     * @return Int accountId or -1 if not defined.
     */
    private int getPreferredAccountId() {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        return prefs.getInt(preferredAccount, -1);
    }

    /**
     * Sets a preferred accountId
     *
     * @param accountId AccountID
     */
    private void setPreferredAccountId(int accountId) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        if (preferredAccount != null & accountId >= 0) {
            prefs.putInt(preferredAccount, accountId);
        } else {
            prefs.remove(preferredAccount);
        }
    }
}
