package client.scenes;

import client.utils.*;
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

public class InviteController implements LanguageChangeListener {
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
     * Initialize method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);
    }

    /**
     * handler for the submit button.
     *
     * @return an array list containing all email addresses to be processed further
     */
    @FXML
    ArrayList<String> handleSubmitButtonAction() {
        if(emailsField.getText().isEmpty()) {
            AlertUtils.showErrorAlert("Form Error!", "Error",
                    "Please enter email addresses!");
            return null;
        }

        String emailAddressesAsString = emailsField.getText();
        ArrayList<String> result =  handleSubmitButtonActionLogic(emailAddressesAsString);
        for (String s : result) {
            if (!ValidationUtils.isValidEmail(s)) {
                AlertUtils.showErrorAlert("Error: invalid email!", s, "is not a valid email address!");
                return  null;
            }
        }
        AlertUtils.showInformationAlert("Invites send!", "Information",
                "Invites sent successfully");

        return result;

    }


    /**
     * method that takes the logic part out of the handler of the submit button so that it can be tested
     * @param emailAddressesAsString list containing all email addresses
     * @return list of email addresses
     */
    static ArrayList<String> handleSubmitButtonActionLogic(String emailAddressesAsString) {
        Scanner scanner = new Scanner(emailAddressesAsString);
        ArrayList<String> emailAddresses = new ArrayList<>();
        while(scanner.hasNext()) {
            String temp = scanner.next();
            emailAddresses.add(temp);
        }
        return emailAddresses;
    }

    /**
     * method that sets title and invite code according to passed event
     * @param newEvent the event
     */
    public void setEvent(Event newEvent) {
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
     * sets the resource bundle
     * @param resourceBundle The resource bundle to set.
     */
    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * sets the active locale
     * @param locale The new locale to set as active.
     */
    @Override
    public void setActiveLocale(Locale locale) {
        this.activeLocale = locale;
    }

    /**
     * gets the main controller
     * @return main controller
     */
    @Override
    public MainController getMainController() {
        return mainController;
    }

    /**
     * updates the UI elements with the selected language
     */
    public void updateUIElements() {
        AnimationUtil.animateText(textBeforeCode, resourceBundle.getString("Give_people_the_following_invite_Code"));
        AnimationUtil.animateText(invitePeople, resourceBundle.getString("Invite_the_following_people_by_email_(one_address_per_line)"));
        emailsField.setPromptText(resourceBundle.getString("emails_go_here"));
        AnimationUtil.animateText(backButton, resourceBundle.getString("back"));
        AnimationUtil.animateText(submitButton, resourceBundle.getString("Send_Invites"));
    }

    /**
     * handler of the button that takes you back to the overview scene
     */
    @FXML
    void handleBackButtonAction() {
        mainController.showEventOverview(event);
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
