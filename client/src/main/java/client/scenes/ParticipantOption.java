package client.scenes;

public class ParticipantOption {
    private final Long id;
    private final String name;

    /**
     * Constructor for ParticipantOption
     * @param id Long
     * @param name String
     */
    public ParticipantOption(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * ID getter
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * To string method
     * @return String
     */
    @Override
    public String toString() {
        return name;
    }
}
