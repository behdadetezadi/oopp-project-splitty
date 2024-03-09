package client.scenes;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ContactDetailsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;
    @FXML
    private Button contactButton;
    @FXML
    private Button cancelButton;
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * QuoteOverview controller
     * @param server   ServerUtils type
     * @param mainCtrl MainCtrl type
     */
    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializer method
     */
    @FXML
    public void initialize() {
//        cancelButton.setOnAction(this::clearFields);  -- verander
        contactButton.setOnAction(this::ok);
    }


    public void ok(ActionEvent actionEvent) {
        String userName = userNameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();

        // TODO validate email?
        // TODO validate iban?
        // TODO validate bic?

        try {
            Participant participant =  new Participant(userName, firstName, lastName, email, iban, bic,
                new HashMap<>(), new HashMap<>(), new HashSet<>(), "English");
            ServerUtils.addParticipant(participant);
//            switchToStartPageScene(actionEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void switchToStartPageScene(javafx.event.ActionEvent event){
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client/scenes/startPage.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * clears all the fields
     */
    @FXML
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        userNameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
    }

}