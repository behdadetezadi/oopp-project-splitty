

package client.scenes;

import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class TableOfParticipantsController {

    @FXML
    private Pagination pagination;

    private ObservableList<Participant> participants = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadParticipants();
        pagination.setPageCount(participants.size());
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        VBox box = new VBox(5);
        if (pageIndex < participants.size()) {
            Participant participant = participants.get(pageIndex);
            String content = participant.getFirstName() + " " + participant.getLastName();
            Label label = new Label(content);
            label.getStyleClass().add("participant-label");
            box.getChildren().add(label);
            box.setStyle("-fx-background-color: white; -fx-font-size: 20px;");

        }
        return box;
    }

    private void loadParticipants() {
        participants.addAll(
                new Participant("John", "Doe"),
                new Participant("Jane", "Doe"),
                new Participant("Joshy","Doe")
        );
    }
}
