package client.scenes;

import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class TableOfParticipantsController {

    @FXML
    private Pagination pagination;

    private final ObservableList<Participant> participants = FXCollections.observableArrayList();

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
     * switching back to event overview page
     */
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource
                    ("/client/scenes/EventOverview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) pagination.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a participant
     */
    @FXML
    private void handleAddButton() {
        Participant newParticipant = new Participant("", "", "", "", "",
                "", new HashMap<>(), new HashMap<>(), new HashSet<>(), "");
        showAddDialog(newParticipant);

    }

    /**
     * edits the participant on the page
     */
    @FXML
    private void handleEditButton() {
        if (participants.isEmpty()) {
            displayNoParticipantsError();
            return;
        }
        int currentPageIndex = pagination.getCurrentPageIndex();
        if(currentPageIndex < participants.size()) {
            Participant participantToEdit = participants.get(currentPageIndex);
            showEditDialog(participantToEdit);
        }
    }

    /**
     * deletes current participant
     */
    @FXML
    private void handleDeleteButton() {
        if (participants.isEmpty()) {
            displayNoParticipantsError();
            return;
        }
        int currentPageIndex = pagination.getCurrentPageIndex();
        if (currentPageIndex < participants.size()) {
            Participant participantToDelete = participants.get(currentPageIndex);
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to remove " +
                    participantToDelete.getFirstName() + " " +
                            participantToDelete.getLastName() + "?", ButtonType.YES, ButtonType.NO);
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    participants.remove(participantToDelete);

                    int numberOfPages = participants.isEmpty() ? 0 :
                            (int) Math.ceil(participants.size()-1
                                    / (double) pagination.getMaxPageIndicatorCount());
                    pagination.setPageCount(numberOfPages);

                    if (currentPageIndex >= numberOfPages) {
                        pagination.setCurrentPageIndex(Math.max(0, numberOfPages - 1));
                    }

                    pagination.setPageFactory(this::createPage);
                }
            });
        }
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
     * this methods ensures all participants are added and
     * loaded before the method create Page executes
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

    /**
     * edit dialog used to change our participant and the edit button
     * @param participant Participant
     */
    private void showEditDialog(Participant participant) {
        Dialog<Participant> dialog = new Dialog<>();
        dialog.setTitle("Edit Participant");
        dialog.setHeaderText("Edit the details of the participant.");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        int textFieldWidth = 200;

        TextField firstNameField = new TextField(participant.getFirstName());
        firstNameField.setPrefWidth(textFieldWidth);
        TextField lastNameField = new TextField(participant.getLastName());
        lastNameField.setPrefWidth(textFieldWidth);
        TextField usernameField = new TextField(participant.getUsername());
        usernameField.setPrefWidth(textFieldWidth);
        TextField emailField = new TextField(participant.getEmail());
        emailField.setPrefWidth(textFieldWidth);
        TextField ibanField = new TextField(participant.getIban());
        ibanField.setPrefWidth(textFieldWidth);
        TextField bicField = new TextField(participant.getBic());
        bicField.setPrefWidth(textFieldWidth);
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "Dutch");
        languageComboBox.setPromptText("Select a Language");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("IBAN:"), 0, 4);
        grid.add(ibanField, 1, 4);
        grid.add(new Label("BIC:"), 0, 5);
        grid.add(bicField, 1, 5);
        grid.add(new Label("Language:"), 0, 6);
        grid.add(languageComboBox, 1, 6);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setMinHeight(350);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHgrow(Priority.ALWAYS);

        ColumnConstraints columnTwoConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstraints.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstraints);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String selectedLanguage = languageComboBox.getValue() != null ? languageComboBox.getValue() : "";
                String validationErrors = validateParticipantDetails(
                        firstNameField.getText(), lastNameField.getText(), usernameField.getText(),
                        emailField.getText(), ibanField.getText(), bicField.getText(), selectedLanguage
                );

                if (!validationErrors.isEmpty()) {
                    showAlertWithText("Validation Error",
                            "Please correct the following errors:", validationErrors);

                    participant.setFirstName(firstNameField.getText());
                    participant.setLastName(lastNameField.getText());
                    participant.setUsername(usernameField.getText());
                    participant.setEmail(emailField.getText());
                    participant.setIban(ibanField.getText());
                    participant.setBic(bicField.getText());
                    participant.setLanguageChoice(languageComboBox.getValue());
                    showEditDialog(participant);
                    return null;
                }

                participant.setFirstName(firstNameField.getText());
                participant.setLastName(lastNameField.getText());
                participant.setUsername(usernameField.getText());
                participant.setEmail(emailField.getText());
                participant.setIban(ibanField.getText());
                participant.setBic(bicField.getText());
                participant.setLanguageChoice(languageComboBox.getValue());
                return participant;
            }
            return null;
        });

        Optional<Participant> result = dialog.showAndWait();
        result.ifPresent(updatedParticipant -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Participant Saved");
            alert.setHeaderText(null);
            alert.setContentText("The participant has been successfully saved.");
            alert.showAndWait();
            pagination.setPageFactory(this::createPage);
        });
    }
    /**
     * Display an error message when there are no participants.
     */
    private void displayNoParticipantsError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No Participants Found");
        alert.setContentText("There are no participants to edit or modify.");
        alert.showAndWait();
    }

    /**
     * adds a new participant. quite similar to modifying one!
     * @param participant Participant
     */
    private void showAddDialog(Participant participant) {
        Dialog<Participant> dialog = new Dialog<>();
        dialog.setTitle("Add New Participant");
        dialog.setHeaderText("Enter details for the new participant.");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        int textFieldWidth = 200;

        TextField firstNameField = new TextField(participant.getFirstName());
        firstNameField.setPrefWidth(textFieldWidth);
        TextField lastNameField = new TextField(participant.getLastName());
        lastNameField.setPrefWidth(textFieldWidth);
        TextField usernameField = new TextField(participant.getUsername());
        usernameField.setPrefWidth(textFieldWidth);
        TextField emailField = new TextField(participant.getEmail());
        emailField.setPrefWidth(textFieldWidth);
        TextField ibanField = new TextField(participant.getIban());
        ibanField.setPrefWidth(textFieldWidth);
        TextField bicField = new TextField(participant.getBic());
        bicField.setPrefWidth(textFieldWidth);
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "Dutch");
        languageComboBox.setPromptText("Select a Language");


        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("IBAN:"), 0, 4);
        grid.add(ibanField, 1, 4);
        grid.add(new Label("BIC:"), 0, 5);
        grid.add(bicField, 1, 5);
        grid.add(new Label("Language:"), 0, 6);
        grid.add(languageComboBox, 1, 6);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setMinHeight(350);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHgrow(Priority.ALWAYS);

        ColumnConstraints columnTwoConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstraints.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstraints);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String selectedLanguage = languageComboBox.getValue() != null ? languageComboBox.getValue() : "";
                String validationErrors = validateParticipantDetails(
                        firstNameField.getText(), lastNameField.getText(), usernameField.getText(),
                        emailField.getText(), ibanField.getText(), bicField.getText(), selectedLanguage
                );

                if (!validationErrors.isEmpty()) {
                    showAlertWithText("Validation Error",
                            "Please correct the following errors:", validationErrors);

                    participant.setFirstName(firstNameField.getText());
                    participant.setLastName(lastNameField.getText());
                    participant.setUsername(usernameField.getText());
                    participant.setEmail(emailField.getText());
                    participant.setIban(ibanField.getText());
                    participant.setBic(bicField.getText());
                    participant.setLanguageChoice(languageComboBox.getValue());
                    showEditDialog(participant);
                    return null;
                }

                return new Participant(
                        usernameField.getText(), firstNameField.getText(), lastNameField.getText(),
                        emailField.getText(), ibanField.getText(),
                        bicField.getText(), new HashMap<>(),
                        new HashMap<>(), new HashSet<>(), languageComboBox.getValue());
            }
            return null;
        });



        Optional<Participant> result = dialog.showAndWait();

        result.ifPresent(newParticipant -> {
            participants.add(newParticipant);
            int newPageCount = participants.size();
            pagination.setPageCount(newPageCount);
            pagination.setPageFactory(this::createPage);
        });
    }

    /**
     * Validation of participant details and ensuring
     * they are typed in the correct format for good error handling
     * @param firstName String
     * @param lastName String
     * @param username String
     * @param email String
     * @param iban String
     * @param bic String
     * @param language String
     * @return String
     */
    private String validateParticipantDetails(String firstName,
                                              String lastName, String username,
                                              String email, String iban, String bic,
                                              String language) {
        StringBuilder sb = new StringBuilder();

        if (firstName.trim().isEmpty() || lastName.trim().isEmpty() || username.trim().isEmpty() ||
                email.trim().isEmpty() || iban.trim().isEmpty() ||
                bic.trim().isEmpty() || language.trim().isEmpty()) {
            sb.append("All fields must be filled in.\n");
        }
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            sb.append("First name and last name must contain only letters.\n");
        }
        if (firstName.isEmpty() || !Character.isUpperCase(firstName.charAt(0)) ||
                lastName.isEmpty() || !Character.isUpperCase(lastName.charAt(0))) {
            sb.append("First name and last name must start with a capital letter.\n");
        }
        if (language.trim().isEmpty()) {
            sb.append("Language selection is required.\n");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            sb.append("Email is in an incorrect format.\n");
        }
        return sb.toString();
    }

    /**
     * method that displays the alert
     * @param title String
     * @param header String
     * @param content String
     */
    private void showAlertWithText(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
