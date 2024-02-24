package server.api;
import commons.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseModel {

    private List<Expense> expensesList;

    /**
     * construct a ExpenseModel Object
     * initialize the expensesList
     */
    public ExpenseModel() {
        this.expensesList = new ArrayList<>();
    }
    /**
     * add method for adding new Expense Object
     * @param expense the Expense object need to be filled
     */

    public void addExpense(Expense expense) {
        expensesList.add(expense);
    }
    /**
     * show all Expense objects in the list
     * @return return expenseList
     */
    public List<Expense> getAllExpenses() {
        return expensesList;
    }
}



