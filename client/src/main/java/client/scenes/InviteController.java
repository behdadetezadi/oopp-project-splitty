package client.scenes;


import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.ValidationUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.*;

public class InviteController  {
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;

    private Event event;

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
    @FXML
    private Label textBeforeCode;
    @FXML
    private Label invitePeople;

    @FXML
    private Button backButton;
    /**
     * invite Controller injection
     * @param primaryStage primary stage
     * @param server server
     * @param mainController main controller
     */
    @Inject
    public InviteController(Stage primaryStage,ServerUtils server, MainController mainController) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;

    }

    /**
     * handler for the submit button.
     *
     * @return an array list containing all email addresses to be processed further
     */
    @FXML
    public ArrayList<String> handleSubmitButtonAction() {
        Window owner = submitButton.getScene().getWindow();

        if(emailsField.getText().isEmpty()) {
            AlertUtils.showErrorAlert("Form Error!", "Error",
                    "Please enter email addresses!");
            return null;
        }

        String emailAddressesAsString = emailsField.getText();
        Scanner scanner = new Scanner(emailAddressesAsString);
        ArrayList<String> emailAddresses = new ArrayList<>();
        boolean flag = false; // hack. needs a better alternative
        while(scanner.hasNext()) {
            String temp = scanner.next();

            if(!ValidationUtils.isValidEmail(temp)) {
                AlertUtils.showErrorAlert("Invalid email!", "Error",
                        temp + " is not a valid email address! " +
                                "Email must be in a valid format (e.g., user@example.com).");
                flag = true;
            } else {
                emailAddresses.add(temp);
            }
        }

        if (!flag) {
            AlertUtils.showInformationAlert("Invites send!", "Information",
                    "Invites sent successfully");
        }
        return emailAddresses;
    }

    /**
     * method that sets title and invite code according to passed event
     * @param newEvent the event
     * @param locale the locale of the user
     */
    public void initData(Event newEvent, Locale locale) {
        this.activeLocale = locale;
        resourceBundle = ResourceBundle.getBundle("message", locale);
        updateUIElements();
        this.event = newEvent;
        inviteCode.setPrefWidth(Double.MAX_VALUE);
        inviteCode.setText(String.valueOf(event.getInviteCode()));
        title.setText(event.getTitle());

        this.inviteCode.setOnMouseClicked(event -> copyInviteCode());
        Tooltip inviteCodeToolTip = new Tooltip(resourceBundle.getString("Click_to_copy_the_invite_code"));
        Tooltip.install(inviteCode,inviteCodeToolTip);
        this.inviteCode.getStyleClass().add("label-hover");

    }

    /**
     * updates the ui elements, this is necessary for the language switch
     */
    public void updateUIElements() {
        textBeforeCode.setText(resourceBundle.getString("Give_people_the_following_invite_Code"));
        invitePeople.setText(resourceBundle.getString("Invite_the_following_people_by_email_(one_address_per_line)"));
        emailsField.setPromptText(resourceBundle.getString("emails_go_here"));
        backButton.setText(resourceBundle.getString("back"));
        submitButton.setText(resourceBundle.getString("Send_Invites"));
    }

    /**
     * handler of the button that takes you back to the overview scene
     *
     */
    @FXML
    public void handleBackButtonAction() {
        try {
            mainController.showEventOverview(event, activeLocale);
        } catch (IllegalStateException e) {
            e.printStackTrace();

        }
    }

    /**
     * this is the method that makes sure that you can copy the invite code
     */
    private void copyInviteCode() {

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(
                this.inviteCode.getText()
        );
        clipboard.setContent(content);
        AlertUtils.showInformationAlert("Invite code copied!",
                "copied the following invite code: ",
                this.inviteCode.getText());
    }
}
