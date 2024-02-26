package server.database;

import commons.Expense;
import commons.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
    List<Expense> findAllByDate(String date);
    List<Expense> findAllByPerson(Person person);
    List<Expense> findAllBySplittingOptionContaining(Person person);
}
