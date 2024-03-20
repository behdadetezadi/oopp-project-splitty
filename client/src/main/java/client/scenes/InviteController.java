package client.scenes;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import commons.Event;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class InviteController implements Initializable {


    @FXML
    private Button backButton;
    @FXML
    private Label title;
    @FXML
    private Label inviteCode;

    @FXML
    private TextArea emailsField;

    @FXML
    private Button submitButton;

    @FXML
    private AnchorPane root;


    /**
     * handler for the submit button.
     *
     * @return an array list containing all email addresses to be processed further
     */
    @FXML
    public ArrayList<String> handleSubmitButtonAction() {
        Window owner = submitButton.getScene().getWindow();

        if(emailsField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter email addresses!");
            return null;
        }

        String emailAddressesAsString = emailsField.getText();
        Scanner scanner = new Scanner(emailAddressesAsString);
        ArrayList<String> emailAddresses = new ArrayList<>();
        boolean flag = false; // hack. needs a better alternative
        while(scanner.hasNext()) {
            String temp = scanner.next();

            if (!temp.contains("@") || !temp.contains(".")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "non-valid email!",
                        temp + " is not a valid email address!");
                flag = true;
            } else {
                emailAddresses.add(temp);
            }
        }

        if (!flag) {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "invites send!",
                    "MONEY!!!");
        }
        return emailAddresses;
    }

    /**
     * initializes the invite scene with correct title and invite code.
     * todo: is semi static for now, needs show correct attributes
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        Event event = new Event(null, null, "Watching paint dry", 12345);
        title.setText(event.getTitle());
        inviteCode.setText(String.valueOf(event.getInviteCode()));
    }


    /**
     * handler of the button that takes you back to the overview scene
     *
     */
    @FXML
    public void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/client/scenes/EventOverview.fxml"));
            Parent inviteRoot = loader.load();
            Scene scene = new Scene(inviteRoot);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();

        }
    }
}
