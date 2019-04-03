package io.github.brunfo.apps.controlefinanceiro;

import io.github.brunfo.apps.controlefinanceiro.controller.*;
import io.github.brunfo.apps.controlefinanceiro.dao.ControleFinanceiroDao;
import io.github.brunfo.apps.controlefinanceiro.dao.ControleFinanceiroDaoImpletemtation;
import io.github.brunfo.apps.controlefinanceiro.model.Account;
import io.github.brunfo.apps.controlefinanceiro.model.Transaction;
import io.github.brunfo.apps.controlefinanceiro.model.Transactions;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Classe principal. Esta classe é a classe principal que funciona de entrada,
 * como também de controller de toda a App.
 * Toda a gestão de vistas como de dados tem de passar por esta classe.
 */
public class MainApp extends Application {

    //database handler
    private static ControleFinanceiroDao dbHandler = ControleFinanceiroDaoImpletemtation.getInstance();
    public final String TITLE = "Controle Financeiro";
    public final String VERSION = "1.1-SNAPSHOT";
    private Stage primaryStage;
    private BorderPane rootLayout;
    /**
     * Os dados como uma observable list de Transactions.
     */
    private ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();
    private ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();

    /**
     * Construtor
     */
    public MainApp() {

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

    // ********** Configurações ***********//

    /**
     * Retorna o palco principal.
     *
     * @return Devolve o Stage principal
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    //******** Controlo de vistas *********//

    /**
     * Inicializa o root layout e tenta carregar o último arquivo
     * de pessoa aberto.
     */
    private void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Mostra a scene (cena) contendo o root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Dá ao controller o acesso ao main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Carega os dados da base dados
        loadFromDataBase();
    }


    private OverviewController showOverview(String overview) {
        try {
            //Carrega o setup
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(overview));
            AnchorPane setup = loader.load();

            //Define setup no centro do root layout
            rootLayout.setCenter(setup);

            //Dá ao controlador acesso à main app
            OverviewController controller = loader.getController();
            controller.setMainApp(this);
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
            loader.setLocation(MainApp.class.getResource(editDialog));
            AnchorPane page = loader.load();

            // Cria o palco dialogStage.

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Define a pessoa no controller.
            EditDialogController controller = loader.getController();
            controller.setMainApp(this);
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
     * Mostra lista de movimentos
     */
    public void showTransactionsOverview() {
        TransactionOverviewController controller =
                (TransactionOverviewController) showOverview("/view/TransactionsOverview.fxml");

        if (controller != null) {
            //TODO cria ficheiro con preferencias de conta predefinida
            //envia preferencias de conta predefinida
            controller.setPredefinedAccount(0);

            if (accountsObservableList.size() > 0) {
                //envias as contas disponiveis para selecionar
                controller.setAvailableAccounts(accountsObservableList);
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
     * Mostra o Accounts
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
    private void loadFromDataBase() {
        // dbHandler.saveAccount(new Account(0,"Habitação"));
        //Carrega da base de dados a lista de Accounts

//        Accounts.set(dbHandler.getAccounts());
        //Carrega da base de dados a lista de Transactions
//        Transactions.set(dbHandler.getTransactions());

        //passa os dados para a ObservableList
        transactionsObservableList.addAll(dbHandler.getTransactions());
        accountsObservableList.addAll(dbHandler.getAccounts());
    }

    public void saveTransaction(Transaction transaction) {
        int id = dbHandler.saveTransaction(transaction);
        transaction.setId(id);
        getTransactions().add(transaction);
    }

    public void saveAccount(Account tempAccount) {
        //save to database and get id
        int id = dbHandler.saveAccount(tempAccount);
        //TODO verificar se foi salva pelo retorno do id
        tempAccount.setId(id);
        getAccounts().add(tempAccount); //adiciona à observableList
        // Accounts.add(tempAccount); //adiciona às contas //TODO rever dupllicação de dados
    }

    public void updateTransactionToDataBase(Transaction selectedTransaction) {
        dbHandler.editTransaction(selectedTransaction);
    }

    public void updateAccount(Account selectedAccount) {
        dbHandler.editAccount(selectedAccount);
    }

    public void deleteTransactionFromDataBase(Transaction selectedTransaction) {
        dbHandler.deleteTransaction(selectedTransaction.getId());
    }

    public void deleteAccountFromDataBase(Account selectedAccount) {
        dbHandler.deleteAccount(selectedAccount.getId());
    }

    //********** Gestão de dados **********//

    /**
     * Retorna os dados como uma observable list de Transaction.
     *
     * @return Devolve ObservableList do dados de movimentos.
     */
    public ObservableList<Transaction> getTransactions() {
        return transactionsObservableList;
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
     * Retorna o arquivo de preferências do movimento, o último arquivo que foi aberto.
     * As preferências são lidas do registro específico do SO (Sistema Operacional).
     * Se tais prefêrencias não puderem  ser encontradas, ele retorna null.
     *
     * @return
     */
    public File getTransactionFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Define o caminho do arquivo do arquivo carregado atual. O caminho é persistido no
     * registro específico do SO (Sistema Operacional).
     *
     * @param file O arquivo ou null para remover o caminho
     */
    private void setTransactionFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

    /**
     * Carrega os dados da pessoa do arquivo especificado. A pessoa atual
     * será substituída.
     *
     * @param file
     */
    public void loadTransactionDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(Transactions.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            Transactions wrapper = (Transactions) um.unmarshal(file);

            transactionsObservableList.clear();
            transactionsObservableList.addAll(Transactions.get());

            // Save the file path to the registry.
            setTransactionFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ficheiro não encontrado");
            alert.setHeaderText("Erro");
            alert.setContentText("Não foi possível carregar dados do arquivo:\n"
                    + file.getPath());
            alert.showAndWait();
        }
    }

    /**
     * Salva os dados da pessoa atual no arquivo especificado.
     *
     * @param file
     */
    public void saveTransactionDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(Transactions.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Envolvendo nossos dados de um movimento.
            Transactions wrapper = new Transactions();
            Transactions.set(transactionsObservableList);

            // Enpacotando e salvando XML  no arquivo.
            m.marshal(wrapper, file);

            // Saalva o caminho do arquivo no registro.
            setTransactionFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Não é possível salvar");
            alert.setHeaderText("Erro");
            alert.setContentText("Não foi possível salvar os dados do arquivo:\n"
                    + file.getPath());
            alert.showAndWait();
        }
    }


}
