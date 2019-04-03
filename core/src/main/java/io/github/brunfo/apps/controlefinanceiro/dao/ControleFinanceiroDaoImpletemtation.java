package io.github.brunfo.apps.controlefinanceiro.dao;


import io.github.brunfo.apps.controlefinanceiro.MainApp;
import io.github.brunfo.apps.controlefinanceiro.model.Account;
import io.github.brunfo.apps.controlefinanceiro.model.Transaction;
import io.github.brunfo.apps.controlefinanceiro.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ControleFinanceiroDaoImpletemtation implements ControleFinanceiroDao {

    private static final ControleFinanceiroDaoImpletemtation INSTANCE = new ControleFinanceiroDaoImpletemtation();

    private Properties dbProperties;
    private Connection dbConnection = null;
    private MainApp mainApp = null;

    private ControleFinanceiroDaoImpletemtation() {
        setDBSystemDir();

        loadDBProperties(this.getClass().getClassLoader().
                getResourceAsStream("Configuration.properties"));

        String driverName = dbProperties.getProperty("derby.driver");

        String dbName = dbProperties.getProperty("databaseName");

        String protocol = dbProperties.getProperty("protocol");

        loadDatabaseDriver(driverName);

        getConnection(protocol + dbName);
    }

    public static ControleFinanceiroDaoImpletemtation getInstance(){
        return INSTANCE;
    }

    private void loadDBProperties(InputStream dbPropInputStream) {
        dbProperties = new Properties();
        try {
            dbProperties.load(dbPropInputStream);
            System.out.println("Properties file loaded successfully");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadDatabaseDriver(String driverName) {
        // Load the Java DB driver.
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void setDBSystemDir() {
        // Decide on the db system directory: <userhome>/.addressbook/
        String systemDir = "controlefinanceiro";

        // Set the db system directory.
        System.setProperty("derby.system.home", systemDir);
    }

    private void getConnection(String strUrl) {
        Properties props = new Properties();
        props.put("create", dbProperties.getProperty("create"));
        props.put("user", dbProperties.getProperty("user"));
        props.put("password", dbProperties.getProperty("password"));
        try {
            dbConnection = DriverManager.getConnection(strUrl, props);
            System.out.println("Connection established successfully");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private boolean createTable(String tableName, String strParameters) {
        boolean bCreatedTables = false;
        Statement statement;
        try {
            statement = dbConnection.createStatement();
            statement.execute("CREATE table " + tableName + strParameters);

            bCreatedTables = true;
        } catch (SQLException sqle) {
            if (sqle.getErrorCode() == 30000 &&
                    sqle.getSQLState().equalsIgnoreCase("X0Y32")) {
                deleteTable(tableName);
                bCreatedTables = createTable(tableName, strParameters);
            } else
                printSQLException(sqle);
        }

        return bCreatedTables;
    }

    private boolean deleteTable(String table) {
        boolean dbDeletedTable = false;
        Statement stmtDelete;
        try {
            stmtDelete = dbConnection.createStatement();
            stmtDelete.executeUpdate("DROP table " + table);
            dbDeletedTable = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbDeletedTable;
    }

    /**
     * Prints details of an SQLException chain to <code>System.err</code>.
     * Details included are SQL State, Error code, Exception message.
     *
     * @param sqle the SQLException from which to print details.
     */
    private void printSQLException(SQLException sqle) {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (sqle != null) {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + sqle.getSQLState());
            System.err.println("  Error Code: " + sqle.getErrorCode());
            System.err.println("  Message:    " + sqle.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            sqle = sqle.getNextException();
        }
    }

    private String getTableTransactionsStr() {
        return " (" +
                " id          INTEGER NOT NULL" +
                " PRIMARY KEY GENERATED ALWAYS AS IDENTITY" +
                "        (START WITH 1, INCREMENT BY 1)," +
                " idConta       INTEGER," +
                " dataOp        VARCHAR(30)," +
                " dataMov       VARCHAR(30)," +
                " descricao     VARCHAR(150)," +
                " montante      DOUBLE)";
    }

    private String getTableAccountsStr() {
        return " (" +
                " id          INTEGER NOT NULL" +
                " PRIMARY KEY GENERATED ALWAYS AS IDENTITY" +
                "        (START WITH 1, INCREMENT BY 1)," +
                " nome     VARCHAR(50))";
    }

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactionArrayList = new ArrayList<>();
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM APP.MOVIMENTOS");
            while (resultSet.next()){
                Transaction transaction =new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getInt("idconta"),
                        DateUtil.parse(resultSet.getString("dataOp")),
                        DateUtil.parse(resultSet.getString("dataMov")),
                        resultSet.getString("descricao"),
                        resultSet.getDouble("montante"));
                transactionArrayList.add(transaction);
                transaction.toString();
            }
        } catch (SQLException sqle) {
            if (sqle.getErrorCode() == 30000 &&
                    sqle.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.MOVIMENTOS", getTableTransactionsStr());
            }
            else
                printSQLException(sqle);
        }

        return transactionArrayList;
    }

    @Override
    public boolean deleteTransaction(int id) {
        PreparedStatement stmtDeleteTransaction;
        boolean bDeleted = false;

        try {
            stmtDeleteTransaction = dbConnection.prepareStatement(
                    "DELETE FROM APP.MOVIMENTOS " +
                            "WHERE id = ?");
            stmtDeleteTransaction.clearParameters();
            stmtDeleteTransaction.setInt(1, id);
            stmtDeleteTransaction.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
        return bDeleted;
    }

    @Override
    public boolean editTransaction(Transaction transaction) {
        boolean bEdited = false;
        PreparedStatement stmtUpdateExistingRecord;
        try {
            stmtUpdateExistingRecord = dbConnection.prepareStatement(
                    "UPDATE APP.MOVIMENTOS " +
                            "SET idConta = ?, " +
                            "    dataOp = ?, " +
                            "    dataMov = ?, " +
                            "    descricao = ?, " +
                            "    montante = ? " +
                            "WHERE ID = ?");

            stmtUpdateExistingRecord.clearParameters();
            stmtUpdateExistingRecord.setInt(1, transaction.getAccountId());
            stmtUpdateExistingRecord.setString(2, DateUtil.format(transaction.getOperationDate()));
            stmtUpdateExistingRecord.setString(3, DateUtil.format(transaction.getTransactionDate()));
            stmtUpdateExistingRecord.setString(4, transaction.getDescription());
            stmtUpdateExistingRecord.setDouble(5, transaction.getAmount());
            stmtUpdateExistingRecord.setInt(6, transaction.getId());

            int a= stmtUpdateExistingRecord.executeUpdate();
            bEdited = true;
            System.out.println("Record edited successfully, return " + transaction.getId() +" "+ a);
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
        return bEdited;
    }

    @Override
    public int saveTransaction(Transaction transaction) {
        int id = -1;
        PreparedStatement stmtSaveNewRecord;
        try {
            stmtSaveNewRecord = dbConnection.prepareStatement(
                    "INSERT INTO APP.MOVIMENTOS " +
                            "   (idConta, dataOp, dataMov, " +
                            "    descricao, montante) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmtSaveNewRecord.clearParameters();
            stmtSaveNewRecord.setInt(1, transaction.getAccountId());
            stmtSaveNewRecord.setString(2, DateUtil.format(transaction.getOperationDate()));
            stmtSaveNewRecord.setString(3, DateUtil.format(transaction.getTransactionDate()));
            stmtSaveNewRecord.setString(4, transaction.getDescription());
            stmtSaveNewRecord.setDouble(5, transaction.getAmount());

            int rowCount = stmtSaveNewRecord.executeUpdate();
            ResultSet results = stmtSaveNewRecord.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
        } catch (SQLException sqle) {
            //if table does not exits, creates it and then save the transaction
            if (sqle.getErrorCode() == 30000 &&
                    sqle.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.MOVIMENTOS", getTableTransactionsStr());
                id= saveTransaction(transaction);
            }
            else
                printSQLException(sqle);
        }
        return id;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accountArrayList = new ArrayList<>();
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM APP.CONTAS");
            while (resultSet.next()){
                accountArrayList.add(new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("nome")));
            }
        } catch (SQLException sqle) {
            if (sqle.getErrorCode() == 30000 &&
                    sqle.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.CONTAS", getTableAccountsStr());
            }
            else
                printSQLException(sqle);
        }

        return accountArrayList;
    }

    @Override
    public boolean deleteAccount(int id) {
        PreparedStatement stmtDeleteAccount;
        boolean bDeleted = false;

        try {
            stmtDeleteAccount = dbConnection.prepareStatement(
                    "DELETE FROM APP.CONTAS " +
                            "WHERE id = ?");
            stmtDeleteAccount.clearParameters();
            stmtDeleteAccount.setInt(1, id);
            stmtDeleteAccount.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
        return bDeleted;
    }

    @Override
    public boolean editAccount(Account account) {
        boolean bEdited = false;
        PreparedStatement stmtUpdateExistingRecord;
        try {
            stmtUpdateExistingRecord = dbConnection.prepareStatement(
                    "UPDATE APP.CONTAS " +
                            "SET nome = ? " +
                            "WHERE id = ?");

            stmtUpdateExistingRecord.clearParameters();
            stmtUpdateExistingRecord.setString(1, account.getName());
            stmtUpdateExistingRecord.setInt(2, account.getId());
            stmtUpdateExistingRecord.executeUpdate();
            bEdited = true;
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
        return bEdited;
    }

    @Override
    public int saveAccount(Account account) {
        int id = -1;
        PreparedStatement stmtSaveNewRecord;
        try {
            stmtSaveNewRecord = dbConnection.prepareStatement(
                    "INSERT INTO APP.CONTAS " +
                            "   (nome) " +
                            "VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmtSaveNewRecord.clearParameters();
            stmtSaveNewRecord.setString(1, account.getName());

            int rowCount = stmtSaveNewRecord.executeUpdate();
            ResultSet results = stmtSaveNewRecord.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
        } catch (SQLException sqle) {
            //if table does not exits, creates it and then save the account
            if (sqle.getErrorCode() == 30000 &&
                    sqle.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.CONTAS", getTableAccountsStr());
                id= saveAccount(account);
            }
            else
                printSQLException(sqle);
        }
        return id;
    }
}
