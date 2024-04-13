package client.scenes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantOptionTest {

    @Test
    public void participantOptionTest() {
        ParticipantOption p = new ParticipantOption((long)21, "Eliot");
        assertEquals(21, p.getId());
        assertEquals("Eliot", p.toString());
    }


}