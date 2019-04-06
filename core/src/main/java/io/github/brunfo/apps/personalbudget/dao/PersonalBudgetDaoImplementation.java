package io.github.brunfo.apps.personalbudget.dao;


import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import io.github.brunfo.apps.personalbudget.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PersonalBudgetDaoImplementation implements PersonalBudgetDao {

    private static final PersonalBudgetDaoImplementation INSTANCE = new PersonalBudgetDaoImplementation();

    private Properties dbProperties;
    private Connection dbConnection = null;

    private PersonalBudgetDaoImplementation() {
        setDBSystemDir();

        loadDBProperties(this.getClass().getClassLoader().
                getResourceAsStream("Configuration.properties"));

        String driverName = dbProperties.getProperty("derby.driver");

        String dbName = dbProperties.getProperty("databaseName");

        String protocol = dbProperties.getProperty("protocol");

        loadDatabaseDriver(driverName);

        getConnection(protocol + dbName);
    }

    public static PersonalBudgetDaoImplementation getInstance() {
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
        @SuppressWarnings("SpellCheckingInspection") String systemDir = "personalbudget";

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
        } catch (SQLException sqlError) {
            sqlError.printStackTrace();
        }
    }

    private boolean createTable(String tableName, String strParameters) {
        boolean bCreatedTables = false;
        Statement statement;
        try {
            statement = dbConnection.createStatement();
            statement.execute("CREATE table " + tableName + strParameters);

            bCreatedTables = true;
        } catch (SQLException sqlError) {
            if (sqlError.getErrorCode() == 30000 &&
                    sqlError.getSQLState().equalsIgnoreCase("X0Y32")) {
                deleteTable(tableName);
                bCreatedTables = createTable(tableName, strParameters);
            } else
                printSQLException(sqlError);
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
        } catch (SQLException sqlError) {
            sqlError.printStackTrace();
        }
        return dbDeletedTable;
    }

    /**
     * Prints details of an SQLException chain to <code>System.err</code>.
     * Details included are SQL State, Error code, Exception message.
     *
     * @param sqlError the SQLException from which to print details.
     */
    private void printSQLException(SQLException sqlError) {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (sqlError != null) {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + sqlError.getSQLState());
            System.err.println("  Error Code: " + sqlError.getErrorCode());
            System.err.println("  Message:    " + sqlError.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            sqlError = sqlError.getNextException();
        }
    }

    private String getTableTransactionsStr() {
        return " (" +
                " id          INTEGER NOT NULL" +
                " PRIMARY KEY GENERATED ALWAYS AS IDENTITY" +
                "        (START WITH 1, INCREMENT BY 1)," +
                " idAccount             INTEGER," +
                " dateOperation                VARCHAR(30)," +
                " dateTransaction       VARCHAR(30)," +
                " description           VARCHAR(150)," +
                " amount                DOUBLE)";
    }

    private String getTableAccountsStr() {
        return " (" +
                " id          INTEGER NOT NULL" +
                " PRIMARY KEY GENERATED ALWAYS AS IDENTITY" +
                "        (START WITH 1, INCREMENT BY 1)," +
                " name     VARCHAR(50))";
    }

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactionArrayList = new ArrayList<>();
        Statement statement;
        ResultSet resultSet;
        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM APP.TRANSACTIONS");
            while (resultSet.next()){
                Transaction transaction =new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getInt("idAccount"),
                        DateUtil.parse(resultSet.getString("dateOperation")),
                        DateUtil.parse(resultSet.getString("dateTransaction")),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"));
                transactionArrayList.add(transaction);
            }
        } catch (SQLException sqlError) {
            if (sqlError.getErrorCode() == 30000 &&
                    sqlError.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.TRANSACTIONS", getTableTransactionsStr());
            }
            else
                printSQLException(sqlError);
        }

        return transactionArrayList;
    }

    @Override
    public boolean deleteTransaction(int id) {
        PreparedStatement stmtDeleteTransaction;
        boolean bDeleted = false;

        try {
            stmtDeleteTransaction = dbConnection.prepareStatement(
                    "DELETE FROM APP.TRANSACTIONS " +
                            "WHERE id = ?");
            stmtDeleteTransaction.clearParameters();
            stmtDeleteTransaction.setInt(1, id);
            stmtDeleteTransaction.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqlError) {
            printSQLException(sqlError);
        }
        return bDeleted;
    }

    @Override
    public boolean editTransaction(Transaction transaction) {
        boolean bEdited = false;
        PreparedStatement stmtUpdateExistingRecord;
        try {
            stmtUpdateExistingRecord = dbConnection.prepareStatement(
                    "UPDATE APP.TRANSACTIONS " +
                            "SET idAccount = ?, " +
                            "    dateOperation = ?, " +
                            "    dateTransaction = ?, " +
                            "    description = ?, " +
                            "    amount = ? " +
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
        } catch (SQLException sqlError) {
            printSQLException(sqlError);
        }
        return bEdited;
    }

    @Override
    public int saveTransaction(Transaction transaction) {
        int id = -1;
        PreparedStatement stmtSaveNewRecord;
        try {
            stmtSaveNewRecord = dbConnection.prepareStatement(
                    "INSERT INTO APP.TRANSACTIONS " +
                            "   (idAccount, dateOperation, dateTransaction, " +
                            "    description, amount) " +
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
        } catch (SQLException sqlError) {
            //if table does not exits, creates it and then save the transaction
            if (sqlError.getErrorCode() == 30000 &&
                    sqlError.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.TRANSACTIONS", getTableTransactionsStr());
                id= saveTransaction(transaction);
            }
            else
                printSQLException(sqlError);
        }
        return id;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accountArrayList = new ArrayList<>();
        Statement statement;
        ResultSet resultSet;
        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM APP.ACCOUNTS");
            while (resultSet.next()){
                accountArrayList.add(new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("name")));
            }
        } catch (SQLException sqlError) {
            if (sqlError.getErrorCode() == 30000 &&
                    sqlError.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.ACCOUNTS", getTableAccountsStr());
            }
            else
                printSQLException(sqlError);
        }

        return accountArrayList;
    }

    @Override
    public boolean deleteAccount(int id) {
        PreparedStatement stmtDeleteAccount;
        boolean bDeleted = false;

        try {
            stmtDeleteAccount = dbConnection.prepareStatement(
                    "DELETE FROM APP.ACCOUNTS " +
                            "WHERE id = ?");
            stmtDeleteAccount.clearParameters();
            stmtDeleteAccount.setInt(1, id);
            stmtDeleteAccount.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqlError) {
            printSQLException(sqlError);
        }
        return bDeleted;
    }

    @Override
    public boolean editAccount(Account account) {
        boolean bEdited = false;
        PreparedStatement stmtUpdateExistingRecord;
        try {
            stmtUpdateExistingRecord = dbConnection.prepareStatement(
                    "UPDATE APP.ACCOUNTS " +
                            "SET name = ? " +
                            "WHERE id = ?");

            stmtUpdateExistingRecord.clearParameters();
            stmtUpdateExistingRecord.setString(1, account.getName());
            stmtUpdateExistingRecord.setInt(2, account.getId());
            stmtUpdateExistingRecord.executeUpdate();
            bEdited = true;
        } catch (SQLException sqlError) {
            printSQLException(sqlError);
        }
        return bEdited;
    }

    @Override
    public int saveAccount(Account account) {
        int id = -1;
        PreparedStatement stmtSaveNewRecord;
        try {
            stmtSaveNewRecord = dbConnection.prepareStatement(
                    "INSERT INTO APP.ACCOUNTS " +
                            "   (name) " +
                            "VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmtSaveNewRecord.clearParameters();
            stmtSaveNewRecord.setString(1, account.getName());


            int rowCount = stmtSaveNewRecord.executeUpdate();
            ResultSet results = stmtSaveNewRecord.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
        } catch (SQLException sqlError) {
            //if table does not exits, creates it and then save the account
            if (sqlError.getErrorCode() == 30000 &&
                    sqlError.getSQLState().equalsIgnoreCase("42X05")) {
                createTable("APP.ACCOUNTS", getTableAccountsStr());
                id= saveAccount(account);
            }
            else
                printSQLException(sqlError);
        }
        return id;
    }
}
