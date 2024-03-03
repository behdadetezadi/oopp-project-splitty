package server.database;


import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
    /**
     * find by date javadoc
     * @param date String
     * @return array list of expenses
     */
    List<Expense> findAllByDate(String date);
    List<Expense> findAllByParticipantId(long participantId);
    List<Expense> findAllBySplittingOptionContaining(long participantId);
}
