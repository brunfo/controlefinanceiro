package io.github.brunfo.apps.controlefinanceiro.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Classe auxiliar para envolver uma lista de transactionsList. Esta é usada para salvar a
 * lista de transactionsList em XML.
 *
 * @author Bruno Rego
 */
@XmlRootElement(name = "transactionsList")
public class Transactions {

    private static List<Transaction> transactionsList;

    @XmlElement(name = "movimento")
    public static List<Transaction> get() {
        return transactionsList;
    }

    public static void set(List<Transaction> transactions) {
        Transactions.transactionsList = transactions;
    }

    public static Transaction get(int index) {
        return transactionsList.get(index);
    }

    /**
     * Registra um transaction na lista.
     *
     * @param transaction Transaction a efetuar.
     * @return retrona um valor boleano de verdadeiro ou falso.
     */
    public static boolean add(Transaction transaction) {
        //TODO efetuar controle de erro
        Transactions.transactionsList.add(transaction);
        return true;
    }

    /**
     * Remove um transaction na lista.
     *
     * @param transaction Transaction a remover.
     * @return retrona um valor boleano de verdadeiro ou falso, após controle de erro.
     */
    public static boolean remove(Transaction transaction) {
        //TODO efetuar controle de erro
        Transactions.transactionsList.remove(transaction);
        return true;
    }

    public static int size() {
        return transactionsList.size();
    }
}
