package server.api;
import commons.Expense;

import jakarta.persistence.*;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

public class ExpenseModel
{
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Add method for adding a new Expense Object
     * @param expense the Expense object to be persisted
     */
    @Transactional
    public void addExpense(Expense expense)
    {
        entityManager.persist(expense);
    }

    /**
     * Retrieve all Expense objects from the database
     * @return a list of Expense objects
     */
    public List<Expense> getAllExpenses()
    {
        return entityManager.createQuery("SELECT e FROM Expense e", Expense.class).getResultList();
    }
    /**
     * Retrieves an expense by its ID.
     * @param id The ID of the expense to retrieve.
     * @return The Expense object if found, or null otherwise.
     */
    public Expense getExpenseById(int id)
    {
        return entityManager.find(Expense.class, id);
    }
}




