package server.database;

import commons.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


//Due to us extending from JPA REPOSITORY, we are provided with the basic CRUD operations already.
//So these are some extra operations that might be useful further on into our implementation.
public interface DebtRepository extends JpaRepository<Debt, Long> {
    /**
     * This method allows us to find the list of all debts associated to a lender through his id.
     * @param lenderId is the long number (id) of the lender
     * @return an array list of all the debts.
     */
    @Query("SELECT d FROM Debt d WHERE d.lender.id = :lenderId")
    List<Debt> lenderDebts(Long lenderId);
    /**
     * This method allows us to find the list of all debts for a debtor through his id.
     * @param debtorId is the long number (id) of the debtor
     * @return an array list of all the debts.
     */
    @Query("SELECT d FROM Debt d WHERE d.debtor.id = :debtorId")
    List<Debt> debtorDebts(Long debtorId);

    /**
     * find all the debts that are collective (or not :) )
     * @param debtCollective boolean (true if collective debt; else false)
     * @return an array list of the debts
     */
    @Query("SELECT d FROM Debt d WHERE d.debtCollective = :debtCollective")
    List<Debt> debtCollectivity(boolean debtCollective);

    /**
     * finds all the debts related to a certain description
     * (may be handy if the descriptions are short and concise)
     * for each unique  event (for example), we could have the same description
     * e.g : restaurant called "BurgerFood" each debt
     * instance would have description "BurgerFood debt"
     * @param description a String
     * @return an array list of debts
     */
    @Query("SELECT d FROM Debt d WHERE LOWER(d.description) " +
            "LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Debt> debtsThroughDescription(String description);

    /**
     * finds all debts that are worth more money than a certain amount
     * @param amount double
     * @return an array list of debts
     */

    @Query("SELECT d FROM Debt d WHERE d.amountOfMoney >= :amount")
    List<Debt> costlierDebts(double amount);
    /**
     * finds all debts that are worth less money than a certain amount
     * @param amount double
     * @return an array list of debts
     */
    @Query("SELECT d FROM Debt d WHERE d.amountOfMoney <= :amount")
    List<Debt> cheaperDebts(double amount);
}