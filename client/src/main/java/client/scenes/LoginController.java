package client.scenes;

import client.Language;
import client.utils.LanguageChangeListener;
import client.utils.AlertUtils;
import client.utils.LanguageUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import java.util.*;

public class LoginController implements LanguageChangeListener{
    private ResourceBundle resourceBundle;
    private Locale activeLocale;
    private Stage primaryStage;
    private MainController mainController;
    private String adminPassword;
    @FXML
    private ComboBox<Language> languageComboBox;
    @FXML
    private ImageView logo;
    @FXML
    private Button userLoginButton;
    @FXML
    private Button adminLoginButton;

    /**
     *
     * @param primaryStage primary stage
     * @param mainController main controller
     */
    @Inject
    public LoginController(Stage primaryStage, MainController mainController) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;
    }

    /**
     * Sets up the controller after the FXML file is loaded.
     */
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);
        // Populates the language combo box
        LanguageUtils.configureLanguageComboBox(languageComboBox, this);

        adminPassword = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Admin password: " + adminPassword);

        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("images/MatrixGif.gif")));
        logo.setImage(image);
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
        userLoginButton.setText(resourceBundle.getString("User_Login"));
        adminLoginButton.setText(resourceBundle.getString("Admin_Login"));
    }

    /**
     * Set the language combo box
     */
    public void setLanguageComboBox() {
        String languageName = LanguageUtils.localeToLanguageName(activeLocale);
        List<Language> languages = new ArrayList<>();
        languages.add(new Language("English",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/english.png")))));
        languages.add(new Language("Deutsch",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/german.png")))));
        languages.add(new Language("Nederlands",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/dutch.png")))));
        for (Language language : languages) {
            if (language.getName().equals(languageName)) {
                languageComboBox.setValue(language);
                break;
            }
        }
        languageComboBox.setItems(FXCollections.observableArrayList(languages));
        languageComboBox.setCellFactory(listView -> new LoginController.LanguageListCell());
        languageComboBox.setButtonCell(new LoginController.LanguageListCell());
    }

    /**
     * Transitions to the start page upon user login action.
     */
    @FXML
    private void handleUserLogin() {
        mainController.showStartPage();
    }

    /**
     * Displays admin login prompt and processes authentication.
     */
    @FXML
    private void handleAdminLoginPrompt() {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Admin Login");
        passwordDialog.setHeaderText("Admin Authentication");
        passwordDialog.setContentText("Please enter admin password:");
        String cssPath = this.getClass().getResource("/styles.css").toExternalForm();
        passwordDialog.getDialogPane().getScene().getStylesheets().add(cssPath);

        Optional<String> result = passwordDialog.showAndWait();
        result.ifPresent(password -> {
            if (password.equals(adminPassword)) {
                mainController.showAdminPage();
            } else {
                AlertUtils.showErrorAlert("Invalid Password", "Error", "The password you have entered is incorrect, please try again.");
            }
        });
    }

    /**
     * Injects the MainController into this controller.
     * @param mainController The main controller to be used.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private class LanguageListCell extends ListCell<Language> {
        @Override
        protected void updateItem(Language item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getName());
                ImageView imageView = new ImageView(item.getFlag());
                imageView.setFitHeight(10);
                imageView.setFitWidth(20);
                setGraphic(imageView);
            }
        }
    }
}
