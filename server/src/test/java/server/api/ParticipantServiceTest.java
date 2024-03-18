package server.api;

import org.hibernate.service.spi.ServiceException;
import server.ParticipantService;
import server.database.ParticipantRepository;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveParticipant() {
        Participant participant = new Participant();

        when(participantRepository.save(participant)).thenReturn(participant);
        Participant result = participantService.saveParticipant(participant);
        assertEquals(participant, result);
        verify(participantRepository).save(participant);
    }

    @Test
    public void testFindParticipantById() {
        Long id = 1L;
        Optional<Participant> expectedParticipant = Optional.of(new Participant());

        when(participantRepository.findById(id)).thenReturn(expectedParticipant);
        Optional<Participant> result = participantService.findParticipantById(id);
        assertEquals(expectedParticipant, result);
        verify(participantRepository).findById(id);
    }

    @Test
    public void testFindAllParticipants() {
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());
        when(participantRepository.findAll()).thenReturn(expectedParticipants);

        List<Participant> result = participantService.findAllParticipants();
        assertEquals(expectedParticipants, result);
        verify(participantRepository).findAll();
    }

    @Test
    public void testDeleteParticipantById() {
        Long id = 1L;

        doNothing().when(participantRepository).deleteById(id);
        participantService.deleteParticipantById(id);
        verify(participantRepository).deleteById(id);
    }

    @Test
    public void testFindParticipantByUsername() {
        String username = "user";
        Participant expectedParticipant = new Participant();

        when(participantRepository.findByUsername(username)).thenReturn(expectedParticipant);
        Participant result = participantService.findParticipantByUsername(username);
        assertEquals(expectedParticipant, result);
        verify(participantRepository).findByUsername(username);
    }

    @Test
    public void testFindParticipantByEmail() {
        String email = "user@example.com";
        Participant expectedParticipant = new Participant();

        when(participantRepository.findByEmail(email)).thenReturn(expectedParticipant);
        Participant result = participantService.findParticipantByEmail(email);
        assertEquals(expectedParticipant, result);
        verify(participantRepository).findByEmail(email);
    }

    @Test
    public void testFindParticipantsByFirstName() {
        String firstName = "John";
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());

        when(participantRepository.findByFirstName(firstName)).thenReturn(expectedParticipants);
        List<Participant> result = participantService.findParticipantsByFirstName(firstName);
        assertEquals(expectedParticipants, result);
        verify(participantRepository).findByFirstName(firstName);
    }

    @Test
    public void testFindParticipantsByLastName() {
        String lastName = "Doe";
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());

        when(participantRepository.findByLastName(lastName)).thenReturn(expectedParticipants);
        List<Participant> result = participantService.findParticipantsByLastName(lastName);
        assertEquals(expectedParticipants, result);
        verify(participantRepository).findByLastName(lastName);
    }

    @Test
    public void testFindParticipantByIban() {
        String iban = "DE89 3704 0044 0532 0130 00";
        Participant expectedParticipant = new Participant();

        when(participantRepository.findByIban(iban)).thenReturn(expectedParticipant);
        Participant result = participantService.findParticipantByIban(iban);
        assertEquals(expectedParticipant, result);
        verify(participantRepository).findByIban(iban);
    }

    @Test
    public void testFindParticipantsOwingForEvent() {
        Long eventId = 1L;
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());

        when(participantRepository.findParticipantsOwingForEvent(eventId)).thenReturn(expectedParticipants);
        List<Participant> result = participantService.findParticipantsOwingForEvent(eventId);
        assertEquals(expectedParticipants, result);
        verify(participantRepository).findParticipantsOwingForEvent(eventId);
    }

    @Test
    public void testFindParticipantsPaidForEvent() {
        Long eventId = 1L;
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());

        when(participantRepository.findParticipantsPaidForEvent(eventId)).thenReturn(expectedParticipants);
        List<Participant> result = participantService.findParticipantsPaidForEvent(eventId);
        assertEquals(expectedParticipants, result);
        verify(participantRepository).findParticipantsPaidForEvent(eventId);
    }

    @Test
    public void testFindParticipantsByLanguageChoice() {
        String languageChoice = "English";
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());

        when(participantRepository.findByLanguageChoice(languageChoice)).thenReturn(expectedParticipants);
        List<Participant> result = participantService.findParticipantsByLanguageChoice(languageChoice);
        assertEquals(expectedParticipants, result);
        verify(participantRepository).findByLanguageChoice(languageChoice);
    }

    @Test
    public void testSaveParticipantWithNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> participantService.saveParticipant(null));
        assertEquals("Participant is not allowed to be null", exception.getMessage());
    }

    @Test
    public void testFindParticipantByIdWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> participantService.findParticipantById(null));
        assertThrows(IllegalArgumentException.class, () -> participantService.findParticipantById(-1L));
    }

    @Test
    public void testFindAllParticipantsWithRepositoryException() {
        when(participantRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findAllParticipants());
        assertEquals("Error retrieving all the participants", exception.getMessage());
    }

        @Test
        public void testDeleteParticipantByIdWithInvalidId() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> participantService.deleteParticipantById(-1L));
            assertEquals("ID must be positive and not null", exception.getMessage());
        }

    @Test
    public void testFindParticipantByUsernameWithRepositoryException() {
        when(participantRepository.findByUsername("username")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantByUsername("username"));
        assertEquals("Error finding the participant by username", exception.getMessage());
    }

    @Test
    public void testFindParticipantByFirstnameWithRepositoryException() {
        when(participantRepository.findByFirstName("firstname")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantsByFirstName("firstname"));
        assertEquals("Error finding the participant by first name", exception.getMessage());
    }
    @Test
    public void testFindParticipantByLastnameWithRepositoryException() {
        when(participantRepository.findByLastName("lastname")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantsByLastName("lastname"));
        assertEquals("Error finding the participant by last name", exception.getMessage());
    }
    @Test
    public void testFindParticipantByEmailWithRepositoryException() {
        when(participantRepository.findByEmail("email")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantByEmail("email"));
        assertEquals("Error finding the participant by email", exception.getMessage());
    }
    @Test
    public void testFindParticipantByIbanWithRepositoryException() {
        when(participantRepository.findByIban("iban")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantByIban("iban"));
        assertEquals("Error finding the participant by iban", exception.getMessage());
    }

    @Test
    public void testFindParticipantsOwingForEventWithInvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> participantService.findParticipantsOwingForEvent(-1L));
        assertEquals("eventId must be positive and not null", exception.getMessage());
        assertThrows(IllegalArgumentException.class, () -> participantService.findParticipantsOwingForEvent(null));
    }

    @Test
    public void testFindParticipantsOwingForEventWithRepositoryException() {
        when(participantRepository.findParticipantsOwingForEvent(1L)).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantsOwingForEvent(1L));
        assertEquals("Error finding the participant by eventId", exception.getMessage());
    }

    @Test
    public void testFindParticipantsPaidForEventWithInvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> participantService.findParticipantsPaidForEvent(-1L));
        assertEquals("eventId must be positive and not null", exception.getMessage());
        assertThrows(IllegalArgumentException.class, () -> participantService.findParticipantsPaidForEvent(null));
    }

    @Test
    public void testFindParticipantsPaidForEventWithRepositoryException() {
        when(participantRepository.findParticipantsPaidForEvent(1L)).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantsPaidForEvent(1L));
        assertEquals("Error finding the participants who have paid for a specific event", exception.getMessage());
    }

    @Test
    public void testFindParticipantsByLanguageChoiceWithRepositoryException() {
        when(participantRepository.findByLanguageChoice("ABC")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ServiceException.class, () -> participantService.findParticipantsByLanguageChoice("ABC"));
        assertEquals("Error finding the participant by language choice", exception.getMessage());
    }
}
