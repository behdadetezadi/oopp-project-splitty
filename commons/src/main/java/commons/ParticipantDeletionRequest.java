package commons;

public class ParticipantDeletionRequest {
    private long eventId;
    private long participantId;

    public ParticipantDeletionRequest(long eventId, long participantId){
        this.eventId=eventId;
        this.participantId=participantId;
    }

    public ParticipantDeletionRequest(){}


    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    //CLASS USED FOR WEBSOCKETS OF PARTICIPANT DELETION
}

