package server.api;

import commons.Expense;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final Map<Long, Expense> expenses = new HashMap<>();
    private long nextId = 1;

    public Expense getExpenseById(long expenseId) {
        return expenses.get(expenseId);
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses.values());
    }

    public Expense createExpense(Expense expense) {
        expense.setId(nextId++);
        expenses.put(expense.getId(), expense);
        return expense;
    }

    public Expense updateExpense(long expenseId, Expense expense) {
        if (!expenses.containsKey(expenseId)) {
            throw new IllegalArgumentException("Expense not found with ID: " + expenseId);
        }
        expense.setId(expenseId);
        expenses.put(expenseId, expense);
        return expense;
    }

    public void deleteExpense(long expenseId) {
        expenses.remove(expenseId);
    }


}
