package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ExpenseController {
    @FXML
    private TextField payer;
    @FXML
    private TextField expenseDescription;
    @FXML
    private TextField amountPaid;

    /**
     * This method validates input for the amount paid field to make sure it's an integer/double
     * @param event keyboard input event
     */
    @FXML
    private void validateAmountInput(KeyEvent event) {
        String character = event.getCharacter();
        String text = amountPaid.getText();

        // Check if the character is not a digit or not a period
        if (!character.matches("[0-9]") && !character.equals(".")) {
            event.consume();
            return;
        }

        // If it's a period, don't allow a second one
        if (character.equals(".") && text.contains(".")) {
            event.consume();
            return;
        }

        // Create the resulting text if the character is added at the current caret position
        int caretPosition = amountPaid.getCaretPosition();
        String beforeCaret = text.substring(0, caretPosition);
        String afterCaret = text.substring(caretPosition);
        String resultingText = beforeCaret + character + afterCaret;

        // Try to parse the resulting text to a double, considering incomplete input as well
        try {
            // This allows incomplete numbers such as "."
            if (!resultingText.equals(".")) {
                Double.parseDouble(resultingText);
            }
        } catch (NumberFormatException e) {
            event.consume();
        }
    }
}
