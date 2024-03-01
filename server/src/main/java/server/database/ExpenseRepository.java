package server.database;


import commons.Expense;
import commons.Participant;
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

    /**
     * find participants
     * @param participant Participant
     * @return an array list of expenses
     */
    List<Expense> findAllByParticipant(Participant participant);

    /**
     * findAllBySplittingOptionContaining --
     * I don't know what this method does yet, but someone fix it
     * @param participant Participant
     * @return array list of expenses
     */
    List<Expense> findAllBySplittingOptionContaining(Participant participant);
}
