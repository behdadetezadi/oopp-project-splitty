package server.database;

import commons.Expense;
import commons.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseRepository {

    private final List<Expense> expenses;

    public ExpenseRepository() {
        this.expenses = new ArrayList<>();
    }

    /**
     * Save an expense to the repository.
     * @param expense The expense to be saved.
     */
    public void save(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Retrieve all expenses from the repository.
     * @return A list of all expenses.
     */
    public List<Expense> findAll() {
        return expenses;
    }

    /**
     * Retrieve expenses filtered by date from the repository.
     * @param date The date to filter expenses.
     * @return A list of expenses on the specified date.
     */
    public List<Expense> findAllByDate(String date) {
        return expenses.stream()
                .filter(expense -> expense.getDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve expenses filtered by person from the repository.
     * @param person The person to filter expenses.
     * @return A list of expenses associated with the specified person.
     */
    public List<Expense> findAllByPerson(Person person) {
        return expenses.stream()
                .filter(expense -> expense.getPerson().equals(person))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve expenses involving a specific person from the repository.
     * @param person The person to filter expenses.
     * @return A list of expenses involving the specified person.
     */
    public List<Expense> findAllBySplittingOptionContaining(Person person) {
        return expenses.stream()
                .filter(expense -> expense.getSplittingOption().contains(person))
                .collect(Collectors.toList());
    }

    /**
     * Find an expense by its ID.
     * @param id The ID of the expense to find.
     * @return The expense with the specified ID, or null if not found.
     */
    public Expense findById(long id) {
        return expenses.stream()
                .filter(expense -> expense.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
