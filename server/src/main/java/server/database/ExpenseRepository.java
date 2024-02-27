package server.database;


import commons.Expense;
import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
    List<Expense> findAllByDate(String date);
    List<Expense> findAllByPerson(Participant participant);
    List<Expense> findAllBySplittingOptionContaining(Participant participant);
}
