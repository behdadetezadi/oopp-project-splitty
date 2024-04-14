package client.utils;

import client.scenes.ParticipantExpenseViewController;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.Optional;
import java.util.ResourceBundle;

public class DialogUtils {
    /**
     * input dialog for a custom expense tag
     * @param comboBox combobox
     * @param resourceBundleSupplier resource bundle supplier
     */
    public static void otherTagInputDialog(ComboBox<String> comboBox, Supplier<ResourceBundle> resourceBundleSupplier) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ResourceBundle resourceBundle = resourceBundleSupplier.get();  // Get the current ResourceBundle
            if (newValue != null && newValue.equals(resourceBundle.getString("Other"))) {
                TextInputDialog dialog = new TextInputDialog();
                String cssPath = Objects.requireNonNull(ParticipantExpenseViewController.class.getResource("/styles.css"))
                        .toExternalForm();
                dialog.getDialogPane().getStylesheets().add(cssPath);

                dialog.setTitle(resourceBundle.getString("New_Tag"));
                dialog.setHeaderText(resourceBundle.getString("Enter_a_new_tag"));
                dialog.setContentText(resourceBundle.getString("Tag") + ":");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(tag -> {
                    if (!tag.isEmpty() && !comboBox.getItems().contains(tag)) {
                        comboBox.getItems().add(tag);
                        comboBox.getSelectionModel().select(tag);
                    }
                });
            }
        });
    }
}
