package commons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantDeletionRequestTest {


    private ParticipantDeletionRequest request;

    @BeforeEach
    void setUp() {
        request = new ParticipantDeletionRequest();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(request);
        assertEquals(0, request.getEventId());
        assertEquals(0, request.getParticipantId());
    }

    @Test
    void testParametrizedConstructor() {
        ParticipantDeletionRequest paramRequest = new ParticipantDeletionRequest(1L, 2L);
        assertNotNull(paramRequest);
        assertEquals(1L, paramRequest.getEventId());
        assertEquals(2L, paramRequest.getParticipantId());
    }

    @Test
    void testSetEventId() {
        long expectedEventId = 10L;
        request.setEventId(expectedEventId);
        assertEquals(expectedEventId, request.getEventId());
    }

    @Test
    void testSetParticipantId() {
        long expectedParticipantId = 20L;
        request.setParticipantId(expectedParticipantId);
        assertEquals(expectedParticipantId, request.getParticipantId());
    }

    @Test
    void testGetEventId() {
        long expectedEventId = 30L;
        request.setEventId(expectedEventId);
        assertEquals(expectedEventId, request.getEventId());
    }

    @Test
    void testGetParticipantId() {
        long expectedParticipantId = 40L;
        request.setParticipantId(expectedParticipantId);
        assertEquals(expectedParticipantId, request.getParticipantId());
    }

}
