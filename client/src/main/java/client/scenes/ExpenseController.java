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
     * Initializer method
     */
    public void initialize() {
        amountPaid.addEventFilter(KeyEvent.KEY_TYPED, this::validateAmountInput);
    }

    /**
     * This method validates input for the amount paid field to make sure it's an integer/double
     * @param event keyboard input event
     */
    @FXML
    private void validateAmountInput(KeyEvent event) {
        String character = event.getCharacter();
        String currentText = amountPaid.getText();

        // Allow digits, period, and comma
        if (!character.matches("[0-9.,]")) {
            event.consume();
            return;
        }

        // If it's a period or comma, only allow one in the text
        if (character.equals(".") || character.equals(",")) {
            if (currentText.contains(".") || currentText.contains(",")) {
                event.consume();
                return;
            }
        }

        // Construct the expected text at the current caret position
        StringBuilder expectedTextBuilder = new StringBuilder(currentText).insert(amountPaid.getCaretPosition(), character);
        String expectedText = expectedTextBuilder.toString();

        // Replace comma with period for parsing
        expectedText = expectedText.replace(',', '.');

        // Check if it's a valid number
        if (!isValidDouble(expectedText)) {
            event.consume();
        }
    }

    /**
     * Validates text to make sure it's a valid double in this context
     * @param text input
     * @return if valid number (e.g. 15.36 valid, 5 valid, 7.257 not valid)
     */
    private boolean isValidDouble(String text) {
        // If the text ends with a decimal point, it's still considered valid at this point
        if (text.endsWith(".")) {
            return true;
        }

        try {
            Double.parseDouble(text);
            if (text.contains(".")) {
                // Check if there are not more than 2 decimal places
                String decimalPart = text.substring(text.indexOf(".") + 1);
                return decimalPart.length() <= 2;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
