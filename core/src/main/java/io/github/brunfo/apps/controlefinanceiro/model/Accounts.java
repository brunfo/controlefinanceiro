package io.github.brunfo.apps.controlefinanceiro.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Classe auxiliar para envolver uma lista de accountsList. Esta é usada para salvar a
 * lista de accountsList em XML.
 *
 * @author Bruno Rego
 */
@XmlRootElement(name = "accountsList")
public class Accounts {

    private static List<Account> accountsList;

    public static void set(List<Account> accounts) {
        Accounts.accountsList = accounts;
    }

    /**
     * Metodo para retornar lista de accountsList.
     * @return Lista de accountsList.
     */
    @XmlElement(name = "conta")
    public static List<Account> get() {
        return accountsList;
    }

    /**
     * Retorna um elemento da lista de accountsList pelo index definido.
     * @param index Indexante.
     * @return Account.
     */
    public static Account get(int index) {
        return accountsList.get(index);
    }

    /**
     * Registra uma account na lista.
     *
     * @param account Account a efetuar.
     * @return retrona um valor boleano de verdadeiro ou falso.
     */
    public static boolean add(Account account) {
        //TODO efetuar controle de erro
        accountsList.add(account);
        return true;
    }

    /**
     * Remove um account na lista.
     *
     * @param account Account a remover.
     * @return retrona um valor boleano de verdadeiro ou falso, após controle de erro.
     */
    public static boolean remove(Account account) {
        //TODO efetuar controle de erro
        accountsList.remove(account);
        return true;
    }

}
