package server;

import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    /**
     * Saves or updates a participant in the repository.
     * @param participant the participant to save or update
     * @return the saved or updated participant
     */
    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    /**
     * Finds a participant by their ID.
     * @param id the ID of the participant
     * @return an Optional containing the participant if found, or an empty Optional if not
     */
    public Optional<Participant> findParticipantById(Long id) {
        return participantRepository.findById(id);
    }

    /**
     * Retrieves all participants from the repository.
     * @return a list of all participants
     */
    public List<Participant> findAllParticipants() {
        return participantRepository.findAll();
    }

    /**
     * Deletes a participant by their ID.
     * @param id the ID of the participant to delete
     */
    public void deleteParticipantById(Long id) {
        participantRepository.deleteById(id);
    }

    /**
     * Finds a participant by their username.
     * @param username the username of the participant
     * @return the participant with the specified username
     */
    public Participant findParticipantByUsername(String username) {
        return participantRepository.findByUsername(username);
    }

    /**
     * Finds a participant by their email.
     * @param email the email of the participant
     * @return the participant with the specified email
     */
    public Participant findParticipantByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    /**
     * Finds participants by their first name.
     * @param firstName the first name of the participants to find
     * @return a list of participants with the specified first name
     */
    public List<Participant> findParticipantsByFirstName(String firstName) {
        return participantRepository.findByFirstName(firstName);
    }

    /**
     * Finds participants by their last name.
     * @param lastName the last name of the participants to find
     * @return a list of participants with the specified last name
     */
    public List<Participant> findParticipantsByLastName(String lastName) {
        return participantRepository.findByLastName(lastName);
    }

    /**
     * Finds a participant by their IBAN.
     * @param iban the IBAN of the participant
     * @return the participant with the specified IBAN
     */
    public Participant findParticipantByIban(String iban) {
        return participantRepository.findByIban(iban);
    }

    /**
     * Finds participants who owe money for a specific event.
     * @param eventId the ID of the event
     * @return a list of participants who owe money for the specified event
     */
    public List<Participant> findParticipantsOwingForEvent(Long eventId) {
        return participantRepository.findParticipantsOwingForEvent(eventId);
    }

    /**
     * Finds participants who have paid for a specific event.
     * @param eventId the ID of the event
     * @return a list of participants who have paid for the specified event
     */
    public List<Participant> findParticipantsPaidForEvent(Long eventId) {
        return participantRepository.findParticipantsPaidForEvent(eventId);
    }

    /**
     * Finds participants by their language preference.
     * @param languageChoice the language preference of the participants
     * @return a list of participants with the specified language preference
     */
    public List<Participant> findParticipantsByLanguageChoice(String languageChoice) {
        return participantRepository.findByLanguageChoice(languageChoice);
    }
}
