import io.github.brunfo.apps.personalbudget.controller.Controller;
import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;

public class TestAccounts {
    private static Controller controller = Controller.getInstance();

    public static void main(String[] args) {


        controller.addAccount(new Account(1, "Conta 1"));
        controller.addAccount(new Account(2, "Conta 2"));

        Transaction transaction = new Transaction();
        transaction.setAmount(15.5);
        transaction.setDescription("movimento 1");
        transaction.setAccountId(2);
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

        transaction = new Transaction();
        transaction.setAmount(-28.0);
        transaction.setDescription("movimento 4");
        transaction.setAccountId(3);
        addTransaction(transaction);

        transaction = new Transaction();
        transaction.setAmount(28.0);
        transaction.setDescription("movimento 5");
        transaction.setAccountId(3);
        removeTransaction(transaction);

        printAccountTransactions(controller.getAccount("Conta 2"));

    }

    static void removeTransaction(Transaction transaction) {
        try {
            if (!controller.removeTransaction(transaction))
                System.out.println(("Movimento \"" + transaction.getDescription() +
                        "\" não existe na conta : " + controller.getAccountById(transaction.getAccountId())));
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
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
        try {
            if (!controller.addTransaction(transaction))
                System.out.println("Não tem saldo suficiente! Saldo : " +
                        controller.getAccountById(transaction.getAccountId()).getBalance() +
                        " - montante a movimentar : " + transaction.getAmount());
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }

    }

}
