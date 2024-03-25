package client.scenes;

public class ParticipantOption {
    private final Long id;
    private final String name;

    public ParticipantOption(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
