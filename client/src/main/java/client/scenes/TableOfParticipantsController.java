

package client.scenes;

import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.HashSet;

public class TableOfParticipantsController {

    @FXML
    private Pagination pagination;

    private ObservableList<Participant> participants = FXCollections.observableArrayList();

    /**
     * this is an initializer method.
     */
    @FXML
    public void initialize() {
        loadParticipants();
        pagination.setPageCount(participants.size());
        pagination.setPageFactory(this::createPage);
    }

    /**
     * This allows each page to display a single participant with his/her aattributes
     * @param pageIndex as an int
     * @return a Node
     */
    private Node createPage(int pageIndex) {
        VBox box = new VBox(5);
        if (pageIndex < participants.size()) {
            Participant participant = participants.get(pageIndex);
            StringBuilder sb = new StringBuilder();
            sb.append("First Name: "+ participant.getFirstName()+'\n');
            sb.append("Last Name: "+ participant.getLastName()+'\n');
            sb.append("Username: "+ participant.getUsername()+'\n');
            sb.append("Email: "+ participant.getEmail()+'\n');
            sb.append("IBAN: "+ participant.getIban()+'\n');
            sb.append("BIC: "+ participant.getBic()+'\n');
            sb.append("Language Preference: "+ participant.getLanguageChoice()+'\n');
            String content = sb.toString();
            Label label = new Label(content);
            label.getStyleClass().add("participant-label");
            box.getChildren().add(label);

        }
        return box;
    }

    /**
     * this methods ensures all participants are added and loaded before the method create Page executes
     */
    private void loadParticipants() {
        participants.addAll(
                new Participant("johnDoe", "John", "Doe","john.doe@student.tudelft.com","A1","B2",
                        new HashMap<>(), new HashMap<>(), new HashSet<>(),"English"),
                new Participant("janeDoe", "Jane", "Doe","jane.doe@student.tudelft.com","A1","B2",
                        new HashMap<>(), new HashMap<>(), new HashSet<>(),"Dutch"),
                new Participant("joshDoe", "Josh", "Doe","josh.doe@student.tudelft.com","A1","B2",
                        new HashMap<>(), new HashMap<>(), new HashSet<>(),"English")
        );
    }
}
