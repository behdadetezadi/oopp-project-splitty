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

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.FileInputStream;
import java.io.IOException;
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
     * Load email properties from a properties file.
     * @return Properties object containing email properties
     */
    private Properties loadEmailPropertiesFromFile() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("email.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            AlertUtils.showErrorAlert("IO Error", "No Properties File!", "the email.properties file was not fount in the root directory of the application");
            ex.printStackTrace();
        }
        return properties;
    }


    /**
     * handler for the submit button.
     *
     * @return an array list containing all email addresses to be processed further
     */
    @FXML
    public void handleSubmitButtonAction() {
        if (emailsField.getText().isEmpty()) {
            AlertUtils.showErrorAlert(resourceBundle.getString("FormError"), resourceBundle.getString("error"), resourceBundle.getString("FormErrorContent"));
            return;
        }

        ArrayList<String> emailAddresses = new ArrayList<>();
        String emailAddressesAsString = emailsField.getText();
        Scanner scanner = new Scanner(emailAddressesAsString);
        while (scanner.hasNext()) {
            String temp = scanner.next();

            if (!ValidationUtils.isValidEmail(temp)) {
                AlertUtils.showErrorAlert(resourceBundle.getString("InvalidEmail"), resourceBundle.getString("error"), temp + " "+resourceBundle.getString("InvalidEmailContent")+
                "   "+ resourceBundle.getString("InvalidEmailContinue"));
                return;
            } else {
                emailAddresses.add(temp);
            }
        }

        // Get email credentials from properties file
        Properties emailProps = loadEmailPropertiesFromFile();
        String host = emailProps.getProperty("mail.smtp.host");
        int port = Integer.parseInt(emailProps.getProperty("mail.smtp.port"));
        String username = emailProps.getProperty("mail.smtp.username");
        String password = emailProps.getProperty("mail.smtp.password");

        // Compose email content
        String subject = resourceBundle.getString("InvitationHeader");
        String message =  resourceBundle.getString("InvitationComponent1")+"\n\n"
                + resourceBundle.getString("InvitationComponent2")+"\n\n"
                + resourceBundle.getString("InvitationComponent3")+" "+ event.getInviteCode() + "\n\n"
                + resourceBundle.getString("InvitationComponent4")+" "+"\n\n"
                + resourceBundle.getString("InvitationComponent5")+"\n "+ username;

        // Send emails
        for (String emailAddress : emailAddresses) {
            try {
                EmailUtils.sendEmail(host, port, username, password, emailAddress, subject, message);
            } catch (MessagingException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert(resourceBundle.getString("EmailError"), resourceBundle.getString("error"), resourceBundle.getString("EmailErrorContent")+" "+ emailAddress);
                return;
            }
        }
        AlertUtils.showInformationAlert(resourceBundle.getString("InvitationConfirm"), resourceBundle.getString("InformationHeader"), resourceBundle.getString("InvitationSucceed"));
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
        AlertUtils.showInformationAlert(resourceBundle.getString("CopyConfirm"),resourceBundle.getString("CopyDetails")+" ",
                this.inviteCode.getText());
    }
}
