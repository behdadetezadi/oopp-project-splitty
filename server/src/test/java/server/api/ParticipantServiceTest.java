package server.api;

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
}
