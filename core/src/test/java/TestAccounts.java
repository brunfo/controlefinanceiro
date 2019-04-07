import io.github.brunfo.apps.personalbudget.controller.Controller;
import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

public class TestAccounts {
    private static Controller controller = Controller.getInstance();

    public static void main(String[] args) {


        controller.addAccount(new Account(1, "Conta 1"));
        controller.addAccount(new Account(2, "Conta 2"));

        System.out.println("\n\n\tsome data");
        Transaction transaction = new Transaction();
        transaction.setAmount(15.5);
        transaction.setDescription("movimento 1");
        transaction.setAccountId(1);
        addTransaction(transaction);

        transaction = new Transaction();
        transaction.setAmount(15.5);
        transaction.setDescription("movimento 2");
        transaction.setAccountId(2);
        addTransaction(transaction);

        transaction = new Transaction();
        transaction.setAmount(-5.0);
        transaction.setDescription("movimento 3");
        transaction.setAccountId(2);
        addTransaction(transaction);

        printAccountTransactions(controller.getAccount("Conta 1"));
        printAccountTransactions(controller.getAccount("Conta 2"));


        System.out.println("\n\n\ttries to make a transaction width a value bigger them balance");
        transaction = new Transaction();
        transaction.setAmount(-28.0);
        transaction.setDescription("movimento 4");
        transaction.setAccountId(2);
        addTransaction(transaction);

        System.out.println("\n\n\tremoves a transaction");
        transaction = new Transaction();
        transaction.setAmount(28.0);
        transaction.setDescription("movimento 5");
        transaction.setAccountId(2);
        addTransaction(transaction);
        printAccountTransactions(controller.getAccount("Conta 2"));
        removeTransaction(transaction);

        printAccountTransactions(controller.getAccount("Conta 1"));
        printAccountTransactions(controller.getAccount("Conta 2"));

        System.out.println("\n\n\tchange transaction to a nonexistent account");
        controller.changeAccount(transaction, controller.getAccount("Conta 3"));


        printAccountTransactions(controller.getAccount("Conta 1"));
        printAccountTransactions(controller.getAccount("Conta 2"));

        System.out.println("\n\n\ttransfer tests");
        transaction = new Transaction();
        transaction.setAmount(5.);
        transaction.setDescription("transferencia");
        transaction.setAccountId(2);
        controller.transferToAcount(transaction, controller.getAccount("conta 1"));


        printAccountTransactions(controller.getAccount("Conta 1"));
        printAccountTransactions(controller.getAccount("Conta 2"));
    }

    static void removeTransaction(Transaction transaction) {
        if (!controller.removeTransaction(transaction))
            System.out.println(("Movimento \"" + transaction.getDescription() +
                    "\" não existe na conta : " + controller.getAccountById(transaction.getAccountId())));

    }

    static void printAccountTransactions(Account account) {
        System.out.println("\n*************************************************************");
        System.out.println("Account : " + account.getName());
        System.out.println("-------------------------------------------------------------");
        for (Transaction transaction :
                account.getTransactions()) {
            System.out.println(transaction);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Saldo : " + account.getBalance());
        System.out.println("*************************************************************\n");
    }

    static void addTransaction(Transaction transaction) {
        if (!controller.addTransaction(transaction))
            System.out.println("Não tem saldo suficiente! Saldo : " +
                    controller.getAccountById(transaction.getAccountId()).getBalance() +
                    " - montante a movimentar : " + transaction.getAmount());


    }

}
