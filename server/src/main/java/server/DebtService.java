package server;

import org.hibernate.service.spi.ServiceException;
import server.database.DebtRepository;
import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DebtService {
    //The debt repository interface that we created (with all CRUD + extra operations)
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
     * Saves a debt.
     * @param debt Debt
     * @return Saved Debt
     * @throws IllegalArgumentException if invalid debt entered
     */
    public Debt saveDebt(Debt debt) {
        if (debt == null) {
            throw new IllegalArgumentException("Debt not allowed to be null");
        }
        try {
            return debtRepository.save(debt);
        } catch (Exception ex) {
            throw new ServiceException("Error saving debt", ex);
        }
    }


    /**
     * Finds an optional debt by its id.
     * @param id as a long number
     * @return an Optional value returning debt if possible, else emoty
     * @throws IllegalArgumentException if the id is null/negative
     */
    public Optional<Debt> findDebtById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be positive and not null");
        }
        return debtRepository.findById(id);
    }


    /**
     * query that returns all debts with no parameters required
     * @return an array list of debts
     */
    public List<Debt> findAllDebts() {
        try {
            return debtRepository.findAll();
        } catch (Exception ex) {
            throw new ServiceException("Error retrieving all debts", ex);
        }
    }

    /**
     * Query that deleted a debt when a specific id is provided
     * @param id in format of a long number
     * @throws IllegalArgumentException if the id is null or negative.
     */
    public void deleteDebtById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be positive and not null");
        }
        try {
            debtRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error deleting debt", ex);
        }
    }
    /**
     * The query was already annotated basically through the
     * interface so look there if you need more info.
     * @param lenderId Long id/number.
     * @return an array list of debts.
     * @throws IllegalArgumentException if the id is null or negative.
     */
    public List<Debt> findDebtsByLender(Long lenderId) {
        if (lenderId == null || lenderId < 0) {
            throw new IllegalArgumentException("Lender ID must be positive and not null");
        }
        try {
            return debtRepository.lenderDebts(lenderId);
        } catch (Exception ex) {
            throw new ServiceException("Error finding debts by lender ID", ex);
        }
    }

    /**
     * The query was already annotated basically through
     * the interface so look there if you need more info.
     * @param debtorId Long id/number.
     * @return an array list of debts for the debtor.
     * @throws IllegalArgumentException if the id is null/negative
     */
    public List<Debt> findDebtsByDebtor(Long debtorId) {
        if (debtorId == null || debtorId < 0) {
            throw new IllegalArgumentException("Debtor ID must be positive and not null");
        }
        try {
            return debtRepository.debtorDebts(debtorId);
        } catch (Exception ex) {
            throw new ServiceException("Error finding debts by debtor ID", ex);
        }
    }

    /**
     * The query was already annotated basically
     * through the interface so look there if you need more info.
     * @param debtCollective is a boolean indicating whether the debt is (or not ) collective.
     * @return an array list of debts.
     * @throws IllegalArgumentException if the id is null/negative
     */
    public List<Debt> findDebtsByCollectivity(boolean debtCollective) {
        try {
            return debtRepository.debtCollectivity(debtCollective);
        } catch (Exception ex) {
            throw new ServiceException("Error finding debts by collectivity", ex);
        }
    }
    /**
     * The query was already annotated basically through the
     * interface so look there if you need more info.
     * @param description as a String
     * @return array list of debts that match the same description
     * @throws IllegalArgumentException if the id is null/negative
     */
    public List<Debt> findDebtsThroughDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description must not be null");
        }
        try {
            return debtRepository.debtsThroughDescription(description);
        } catch (Exception ex) {
            throw new ServiceException("Error finding debts through description", ex);
        }
    }

    /**
     * The query was already annotated basically
     * through the interface so look there if you need more info.
     * @param amount as a double
     * @return an array list of debts costlier than the amount you entered.
     * @throws IllegalArgumentException if the id is null/negative
     */
    public List<Debt> findCostlierDebts(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        try {
            return debtRepository.costlierDebts(amount);
        } catch (Exception ex) {
            throw new ServiceException("Error finding costlier debts", ex);
        }
    }

    /**
     * The query was already annotated basically
     * through the interface so look there if you need more info.
     * @param amount as a double
     * @return an array list of debts cheaper than the amount you entered.
     * @throws IllegalArgumentException if the id is null/negative
     */
    public List<Debt> findCheaperDebts(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        try {
            return debtRepository.cheaperDebts(amount);
        } catch (Exception ex) {
            throw new ServiceException("Error finding cheaper debts", ex);
        }
    }
}
