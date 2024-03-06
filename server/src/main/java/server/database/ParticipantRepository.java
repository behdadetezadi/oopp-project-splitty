package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


// We extend from JPA repository, so we get some CRUD operations, the rest are some useful
// operations we might need
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    /**
     * Find a participant by username.
     * @param username the username of the participant
     * @return a participant with the specified username
     */
    Participant findByUsername(String username);

    /**
     * Find a participant by email.
     * @param email the email of the participant
     * @return a participant with the specified email
     */
    Participant findByEmail(String email);

    /**
     * Find participants by their first name.
     * @param firstName the first name of the participant
     * @return a list of participants with the specified first name
     */
    List<Participant> findByFirstName(String firstName);

    /**
     * Find participants by their last name.
     * @param lastName the last name of the participant
     * @return a list of participants with the specified last name
     */
    List<Participant> findByLastName(String lastName);

    /**
     * Find a participant by IBAN.
     * @param iban the IBAN of the participant
     * @return a participant with the specified IBAN
     */
    Participant findByIban(String iban);

    /**
     * Find all participants who owe money for a specific event.
     * @param eventId the ID of the event
     * @return a list of participants who owe money for the specified event
     */
    @Query("SELECT p " +
            "FROM Participant p " +
            "JOIN p.owedAmount o " +
            "WHERE KEY(o) = :eventId AND VALUE(o) > 0")
    List<Participant> findParticipantsOwingForEvent(Long eventId);

    /**
     * Find all participants who have paid for a specific event.
     * @param eventId the ID of the event
     * @return a list of participants who have paid for the specified event
     */
    @Query("SELECT p " +
            "FROM Participant p " +
            "JOIN p.payedAmount pa " +
            "WHERE KEY(pa) = :eventId AND VALUE(pa) > 0")
    List<Participant> findParticipantsPaidForEvent(Long eventId);

    /**
     * Find participants by language preference. This is probably useless but let's be extensive.
     * @param languageChoice the language preference of the participant
     * @return a list of participants with the specified language preference
     */
    List<Participant> findByLanguageChoice(String languageChoice);
}
