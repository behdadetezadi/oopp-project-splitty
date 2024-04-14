package commons;

// THIS CLASS IS USED FOR WEBSOCKETS OF PARTICIPANT DELETION
public class ParticipantDeletionRequest {
    private long eventId;
    private long participantId;

    /**
     * Constructor for ParticipantDeletionRequest
     * @param eventId long
     * @param participantId long
     */
    public ParticipantDeletionRequest(long eventId, long participantId){
        this.eventId=eventId;
        this.participantId=participantId;
    }

    /**
     * empty constructor
     */
    public ParticipantDeletionRequest(){}

    /**
     * event ID getter
     * @return Event ID
     */
    public long getEventId() {
        return eventId;
    }

    /**
     * event ID setter
     * @param eventId long
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    /**
     * Participant ID getter
     * @return Participant ID
     */
    public long getParticipantId() {
        return participantId;
    }

    /**
     * Participant ID setter
     * @param participantId long
     */
    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }
}

