package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantDeletionRequestTest {

    @Test
    public void testConstructorWithParameters() {
        long eventId = 1;
        long participantId = 2;
        ParticipantDeletionRequest request = new ParticipantDeletionRequest(eventId, participantId);

        assertEquals(eventId, request.getEventId());
        assertEquals(participantId, request.getParticipantId());
    }

    @Test
    public void testDefaultConstructor() {
        ParticipantDeletionRequest request = new ParticipantDeletionRequest();

        assertEquals(0, request.getEventId());
        assertEquals( 0, request.getParticipantId());
    }

    @Test
    public void testSetEventId() {
        long eventId = 3;
        ParticipantDeletionRequest request = new ParticipantDeletionRequest();
        request.setEventId(eventId);

        assertEquals(eventId, request.getEventId());
    }

    @Test
    public void testSetParticipantId() {
        long participantId = 4;
        ParticipantDeletionRequest request = new ParticipantDeletionRequest();
        request.setParticipantId(participantId);

        assertEquals(participantId, request.getParticipantId());
    }
}
