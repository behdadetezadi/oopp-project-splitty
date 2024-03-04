package server.database;


import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.sql.*;



public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
    /**
     * find by date javadoc
     * @param date String
     * @return array list of expenses
     */
    List<Expense> findAllByDate(String date);
    /**
     * find list of expenses by id of participant
     * @param participantId as a long
     * @return an array list of expenses
     */
    List<Expense> findAllByParticipantId(long participantId);

    /**
     * find expenses through splitting opiton
     * @param participantId long number
     * @return array list of expenses
     */
    @Query("SELECT e FROM Expense e JOIN e.splittingOption p WHERE p.id = :participantId")
    List<Expense> findAllBySplittingOptionContaining(long participantId);
}
