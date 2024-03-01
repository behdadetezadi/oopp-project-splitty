package server;

import server.database.DebtRepository;
import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DebtService {
    //The debt repository interface that we created
    private final DebtRepository debtRepository;

    /**
     * Dependency Injection through the constructor
     * @param debtRepository of type DebtRepository
     */
    @Autowired
    public DebtService(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
    }

    /**
     * The query was already annotated basically through the interface so look there if you need more info.
     * @param lenderId Long id/number.
     * @return an array list of debts.
     */
    public List<Debt> findDebtsByLender(Long lenderId) {
        return debtRepository.lenderDebts(lenderId);
    }

    /**
     * The query was already annotated basically through the interface so look there if you need more info.
     * @param debtorId Long id/number.
     * @return an array list of debts for the debtor.
     */
    public List<Debt> findDebtsByDebtor(Long debtorId) {
        return debtRepository.debtorDebts(debtorId);
    }

    /**
     * The query was already annotated basically through the interface so look there if you need more info.
     * @param debtCollective is a boolean indicating whether the debt is (or not ) collective.
     * @return an array list of debts.
     */
    public List<Debt> findDebtsByCollectivity(boolean debtCollective) {
        return debtRepository.debtCollectivity(debtCollective);
    }

    /**
     * The query was already annotated basically through the interface so look there if you need more info.
     * @param description as a String
     * @return array list of debts that match the same description
     */
    public List<Debt> findDebtsThroughDescription(String description) {
        return debtRepository.debtsThroughDescription(description);
    }

    /**
     * The query was already annotated basically through the interface so look there if you need more info.
     * @param amount as a double
     * @return an array list of debts costlier than the amount you entered.
     */
    public List<Debt> findCostlierDebts(double amount) {
        return debtRepository.costlierDebts(amount);
    }

    /**
     * The query was already annotated basically through the interface so look there if you need more info.
     * @param amount as a double
     * @return an array list of debts cheaper than the amount you entered.
     */    public List<Debt> findCheaperDebts(double amount) {
        return debtRepository.cheaperDebts(amount);
    }
}
