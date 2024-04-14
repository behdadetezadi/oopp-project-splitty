package server;

import commons.Participant;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;


    /**
     * Dependency Injection through the constructor
     * @param participantRepository of type ParticipantRepository
     */
    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    /**
     * Saves or updates a participant in the repository.
     * @param participant the participant to save or update
     * @return the saved or updated participant
     * @throws IllegalArgumentException if the participant is null.
     */
    public Participant saveParticipant(Participant participant) {
        if(participant==null){
            throw new IllegalArgumentException("Participant is not allowed to be null");
        }
        try{
            return participantRepository.save(participant);
        } catch (Exception e){
            throw new ServiceException("Error saving the participant", e);
        }
    }

    /**
     * Finds a participant by their ID.
     * @param id the ID of the participant
     * @return an Optional containing the participant if found, or an empty Optional if not
     * @throws IllegalArgumentException if the id is null or negative.
     */
    public Optional<Participant> findParticipantById(Long id) {
        if(id==null || id<0){
            throw new IllegalArgumentException("ID must be positive and not null");
        }
        try {
            return participantRepository.findById(id);
        } catch (Exception e){
            throw new ServiceException("Error finding the participant by id", e);
        }
    }

    /**
     * Retrieves all participants from the repository.
     * @return a list of all participants
     */
    public List<Participant> findAllParticipants() {
        try{
            return participantRepository.findAll();
        } catch (Exception e){
            throw new ServiceException("Error retrieving all the participants", e);
        }
    }

    /**
     * Deletes a participant by their ID.
     * @param id the ID of the participant to delete
     * @throws IllegalArgumentException if the id is null or negative.
     */
    public void deleteParticipantById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be positive and not null");
        }
        try {
            participantRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException("Error deleting the participant", e);
        }
    }

    /**
     * Finds a participant by their username.
     * @param username the username of the participant
     * @return the participant with the specified username
     */
    public Participant findParticipantByUsername(String username) {
        try{
            return participantRepository.findByUsername(username);
        } catch (Exception e){
            throw new ServiceException("Error finding the participant by username", e);
        }
    }

    /**
     * Finds a participant by their email.
     * @param email the email of the participant
     * @return the participant with the specified email
     */
    public Participant findParticipantByEmail(String email) {
        try {
            return participantRepository.findByEmail(email);
        } catch (Exception e){
            throw new ServiceException("Error finding the participant by email", e);
        }
    }

    /**
     * Finds participants by their first name.
     * @param firstName the first name of the participants to find
     * @return a list of participants with the specified first name
     */
    public List<Participant> findParticipantsByFirstName(String firstName) {
        try {
            return participantRepository.findByFirstName(firstName);
        } catch (Exception e){
            throw new ServiceException("Error finding the participant by first name", e);
        }
    }

    /**
     * Finds participants by their last name.
     * @param lastName the last name of the participants to find
     * @return a list of participants with the specified last name
     */
    public List<Participant> findParticipantsByLastName(String lastName) {
        try {
            return participantRepository.findByLastName(lastName);
        }catch (Exception e){
            throw new ServiceException("Error finding the participant by last name", e);
        }
    }

    /**
     * Finds a participant by their IBAN.
     * @param iban the IBAN of the participant
     * @return the participant with the specified IBAN
     */
    public Participant findParticipantByIban(String iban) {
        try {
            return participantRepository.findByIban(iban);
        }catch (Exception e){
            throw new ServiceException("Error finding the participant by iban", e);
        }
    }

    /**
     * Finds participants who owe money for a specific event.
     * @param eventId the ID of the event
     * @return a list of participants who owe money for the specified event
     * @throws IllegalArgumentException if the id is null or negative.
     */
    public List<Participant> findParticipantsOwingForEvent(Long eventId) {
        if (eventId == null || eventId < 0) {
            throw new IllegalArgumentException("eventId must be positive and not null");
        }
        try {
            return participantRepository.findParticipantsOwingForEvent(eventId);
        }catch (Exception e){
            throw new ServiceException("Error finding the participant by eventId", e);
        }
    }

    /**
     * Finds participants who have paid for a specific event.
     * @param eventId the ID of the event
     * @return a list of participants who have paid for the specified event
     * @throws IllegalArgumentException if the id is null or negative.
     */
    public List<Participant> findParticipantsPaidForEvent(Long eventId) {
        if (eventId == null || eventId < 0) {
            throw new IllegalArgumentException("eventId must be positive and not null");
        }
        try {
            return participantRepository.findParticipantsPaidForEvent(eventId);
        }catch (Exception e){
            throw new ServiceException("Error finding the participants who have paid for a specific event", e);
        }
    }

    /**
     * Finds participants by their language preference.
     * @param languageChoice the language preference of the participants
     * @return a list of participants with the specified language preference
     */
    public List<Participant> findParticipantsByLanguageChoice(String languageChoice) {
        try {
            return participantRepository.findByLanguageChoice(languageChoice);
        }catch (Exception e){
            throw new ServiceException("Error finding the participant by language choice", e);
        }
    }

    /**
     * Updated participant
     * @param participantId participant ID
     * @param updatedParticipantInfo Information for updated participant
     * @return Participant
     */
    public Participant updateParticipant(Long participantId, Participant updatedParticipantInfo) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        participant.setFirstName(updatedParticipantInfo.getFirstName());
        participant.setLastName(updatedParticipantInfo.getLastName());
        participant.setUsername(updatedParticipantInfo.getUsername());
        participant.setEmail(updatedParticipantInfo.getEmail());
        participant.setIban(updatedParticipantInfo.getIban());
        participant.setBic(updatedParticipantInfo.getBic());
        participant.setLanguageChoice(updatedParticipantInfo.getLanguageChoice());


        return participantRepository.save(participant);
    }
}
