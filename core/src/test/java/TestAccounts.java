import io.github.brunfo.apps.personalbudget.controller.Controller;
import io.github.brunfo.apps.personalbudget.model.Account;
import io.github.brunfo.apps.personalbudget.model.Transaction;
import org.junit.Test;

class TestAccounts {
    private static Controller controller = Controller.getInstance();

    public static void main(String[] args) {

        printAccounts();


        printAccounts();

        //controller.addAccount(new Account(1, "Conta 1"));
        //controller.addAccount(new Account(2, "Conta 2"));
//
//        System.out.println("\n\n\tsome data");
//        Transaction transaction = new Transaction(
//                1,
//                1,
//                LocalDate.now(),
//                LocalDate.now(),
//                "movimento 1",
//                15.5);
//        addTransaction(transaction);
//
//        transaction = new Transaction(
//                2,
//                1,
//                LocalDate.now(),
//                LocalDate.now(),
//                "movimento 2",
//                10.);
//        addTransaction(transaction);
//
//        transaction = new Transaction(
//                1,
//                2,
//                LocalDate.now(),
//                LocalDate.now(),
//                "movimento 3",
//                20.5);
//        addTransaction(transaction);
//
//        printAccountTransactions(controller.getAccount("Account 1"));
//        printAccountTransactions(controller.getAccount("Account 2"));
//
//
//        System.out.println("\n\n\ttries to make a transaction width a value bigger them balance");
//        transaction = new Transaction(
//                1,
//                2,
//                LocalDate.now(),
//                LocalDate.now(),
//                "movimento 4",
//                -28.);
//        addTransaction(transaction);
//
//        System.out.println("\n\n\tremoves a transaction");
//        transaction = new Transaction(
//                1,
//                2,
//                LocalDate.now(),
//                LocalDate.now(),
//                "movimento 5",
//                28.0);
//        addTransaction(transaction);
//
//        printAccountTransactions(controller.getAccount("Account 2"));
//        removeTransaction(transaction);
//
//        printAccountTransactions(controller.getAccount("Account 1"));
//        printAccountTransactions(controller.getAccount("Account 2"));
//
//        System.out.println("\n\n\tchange transaction to a nonexistent account");
//        controller.changeTransactionAccount(transaction, controller.getAccount("Account 3"));
//
//
//        printAccountTransactions(controller.getAccount("Account 1"));
//        printAccountTransactions(controller.getAccount("Account 2"));
//
//        System.out.println("\n\n\ttransfer tests");
//        transaction = new Transaction(
//                1,
//                2,
//                LocalDate.now(),
//                LocalDate.now(),
//                "transferencia",
//                5);
//        controller.transferToAccount(transaction, controller.getAccount("Account 1"));
//
//
//        printAccountTransactions(controller.getAccount("Account 1"));
//        printAccountTransactions(controller.getAccount("Account 2"));
    }

    private static void removeTransaction(Transaction transaction) {
        if (!controller.removeTransaction(transaction))
            System.out.println(("Movimento \"" + transaction.getDescription() +
                    "\" não existe na conta : " + controller.getAccountById(transaction.getAccountId())));

    }

    private static void printAccounts() {
        for (Account account : controller.getAccounts()) {
            printAccountTransactions(account);
        }
    }

    private static void printAccountTransactions(Account account) {
        System.out.println("\n*************************************************************");
        System.out.println("Account : " + account.getId() + " : " + account.getName());
        System.out.println("-------------------------------------------------------------");
        for (Transaction transaction :
                account.getTransactions()) {
            System.out.println(transaction);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Saldo : " + account.getBalance());
        System.out.println("*************************************************************\n");
    }

    private static void addTransaction(Transaction transaction) {
        if (!controller.addTransaction(transaction))
            System.out.println("Não tem saldo suficiente! Saldo : " +
                    controller.getAccountById(transaction.getAccountId()).getBalance() +
                    " - montante a movimentar : " + transaction.getAmount());


    }

    @Test
    public void removeAccount() {
        controller.removeAccount(controller.getAccountById(302));

    }

}
