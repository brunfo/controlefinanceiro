package io.github.brunfo.apps.personalbudget.desktop;

import io.github.brunfo.apps.personalbudget.controller.Controller;
import io.github.brunfo.apps.personalbudget.dao.PersonalBudgetDao;
import io.github.brunfo.apps.personalbudget.dao.PersonalBudgetDaoImplementation;
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
 * Classe principal. Esta classe é a classe principal que funciona de entrada,
 * como também de mainController de toda a App.
 * Toda a gestão de vistas como de dados tem de passar por esta classe.
 */
public class DesktopApp extends Application {

    //database handler
    private static final PersonalBudgetDao dbHandler = PersonalBudgetDaoImplementation.getInstance();
    public final String TITLE = "Personal Budget";
    public final String VERSION = "1.1-SNAPSHOT";
    /**
     * Os dados como uma observable list de Transactions.
     */
    private final ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();
    //the Main mainController
    private Controller mainController = Controller.getInstance();
    private Stage primaryStage;
    private BorderPane rootLayout;
    private String preferredAccount = "preferredAccount";

    /**
     * Construtor
     */
    public DesktopApp() {
    }

    //******* Genérico ********//
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(this.TITLE);

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("/img/iconfinder_Address_Book_86957.png"));

        initRootLayout();

        showTransactionsOverview();
    }

    // ********** Config ***********//

    /**
     * Retorna o palco principal.
     *
     * @return Devolve o Stage principal
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    //******** Controllers view *********//

    /**
     * Inicializa o root layout e tenta carregar o último arquivo
     * de pessoa aberto.
     */
    private void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DesktopApp.class
                    .getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Mostra a scene (cena) contendo o root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Dá ao mainController o acesso ao main app.
            RootLayoutController controller = loader.getController();
            controller.setDesktopApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Carega os dados da base dados

        accountsObservableList.clear();
        accountsObservableList.addAll(mainController.getAccounts());
        updateData();
    }


    private OverviewController showOverview(String overview) {
        try {
            //Carrega o setup
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DesktopApp.class.getResource(overview));
            AnchorPane setup = loader.load();

            //Define setup no centro do root layout
            rootLayout.setCenter(setup);

            //Dá ao controlador acesso à main app
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
            // Carrega o arquivo fxml e cria um novo stage para a janela popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DesktopApp.class.getResource(editDialog));
            AnchorPane page = loader.load();

            // Cria o palco dialogStage.

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Define a pessoa no mainController.
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
     * Mostra o Dashboard
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
     * Abre uma janela para editar detalhes para a pessoa especificada. Se o usuário clicar
     * OK, as mudanças são salvasno objeto movimento fornecido e retorna true.
     *
     * @param selectedTransaction O objeto movimento a ser editado
     * @return true Se o usuário clicou OK,  caso contrário false.
     */
    public boolean showTransactionEditDialog(Transaction selectedTransaction) {
        if (accountsObservableList.size() > 0) {
            EditDialogController controller =
                    showEditDialog(
                            "/view/TransactionEditDialog.fxml",
                            "Editar movimento");
            if (controller != null) {
                // Define a pessoa no overviewController.
                ((TransactionEditDialogController) controller).setTransaction(selectedTransaction);

                //TODO cria ficheiro con preferencias de conta predefinida
                //envia preferencias de conta predefinida
                ((TransactionEditDialogController) controller).setPredefinedAccount(0);
                //envias as contas disponiveis para selecionar
                ((TransactionEditDialogController) controller).setAvailableAccounts(accountsObservableList);

                // Mostra a janela e espera até o usuário fechar.
                controller.getDialogStageStage().showAndWait();

                return ((TransactionEditDialogController) controller).isOkClicked();
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nenhuma conta");
        alert.setHeaderText("Erro");
        alert.setContentText("Não existe ainda nenhuma conta criada!\nPara adicionar movimentos tem de ter pelo menos 1 conta!");
        alert.showAndWait();
        return false;
    }

    /**
     * Mostra o Accounts
     */
    public void showAccountsOverview() {
        showOverview("/view/AccountsOverview.fxml");
    }

    /**
     * Abre uma janela para editar detalhes para a pessoa especificada. Se o usuário clicar
     * OK, as mudanças são salvasno objeto movimento fornecido e retorna true.
     *
     * @param selectedAccount O objeto movimento a ser editado
     * @return true Se o usuário clicou OK,  caso contrário false.
     */
    public boolean showAccountEditDialog(Account selectedAccount) {
        EditDialogController controller =
                showEditDialog(
                        "/view/AccountEditDialog.fxml",
                        "Editar conta");
        if (controller != null) {
            ((AccountEditDialogController) controller).setAccount(selectedAccount);

            // Mostra a janela e espera até o usuário fechar.
            controller.getDialogStageStage().showAndWait();

            return ((AccountEditDialogController) controller).isOkClicked();
        }
        return false;
    }

    /**
     * Mostra o Setup
     */
    public void showSetup() {
        showOverview("/view/Setup.fxml");
    }

    public void showBudget() {
        showOverview("/view/Budget.fxml");
    }

    //***** Controlo de base de dados *****//

    /**
     * Carrega dados da base de dados.
     */
    private void updateData() {
        //passa os dados para a ObservableList
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

    //********** Gestão de dados **********//

    /**
     * Retorna os dados como uma observable list de Transaction.
     *
     * @return Devolve ObservableList do dados de movimentos.
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
     * Retorna os dados como uma observable list de Transaction.
     *
     * @return Devolve ObservableList do dados de movimentos.
     */
    public ObservableList<Account> getAccounts() {
        return accountsObservableList;
    }


    //************* Outros ****************//

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
    private void setPreferedAccountId(int accountId) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        if (preferredAccount != null & accountId >= 0) {
            prefs.putInt(preferredAccount, accountId);
        } else {
            prefs.remove(preferredAccount);
        }
    }
//
//    /**
//     * Carrega os dados da pessoa do arquivo especificado. A pessoa atual
//     * será substituída.
//     *
//     * @param file
//     */
//    public void loadTransactionDataFromFile(File file) {
//        try {
//            JAXBContext context = JAXBContext
//                    .newInstance(Transactions.class);
//            Unmarshaller um = context.createUnmarshaller();
//
//            // Reading XML from the file and unmarshalling.
//            Transactions wrapper = (Transactions) um.unmarshal(file);
//
//            transactionsObservableList.clear();
//            transactionsObservableList.addAll(Transactions.get());
//
//            // Save the file path to the registry.
//            setTransactionFilePath(file);
//
//        } catch (Exception e) { // catches ANY exception
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Ficheiro não encontrado");
//            alert.setHeaderText("Erro");
//            alert.setContentText("Não foi possível carregar dados do arquivo:\n"
//                    + file.getPath());
//            alert.showAndWait();
//        }
//    }
//
//    /**
//     * Salva os dados da pessoa atual no arquivo especificado.
//     *
//     * @param file
//     */
//    public void saveTransactionDataToFile(File file) {
//        try {
//            JAXBContext context = JAXBContext
//                    .newInstance(Transactions.class);
//            Marshaller m = context.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            // Envolvendo nossos dados de um movimento.
//            Transactions wrapper = new Transactions();
//            Transactions.set(transactionsObservableList);
//
//            // Enpacotando e salvando XML  no arquivo.
//            m.marshal(wrapper, file);
//
//            // Saalva o caminho do arquivo no registro.
//            setTransactionFilePath(file);
//        } catch (Exception e) { // catches ANY exception
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Não é possível salvar");
//            alert.setHeaderText("Erro");
//            alert.setContentText("Não foi possível salvar os dados do arquivo:\n"
//                    + file.getPath());
//            alert.showAndWait();
//        }
//    }
//
//
}
