package client.utils;
import javafx.scene.input.KeyEvent;

public class ValidationUtils {
    /**
     * Validates if the input string is a valid double with no more than two decimal places.
     * Allows a trailing period which represents an incomplete decimal number being typed.
     *
     * @param text The input string to validate.
     * @return True if the input is a valid double, otherwise false.
     */
    public static boolean isValidDouble(String text) {
        // Allow a number ending in a period (incomplete decimal input)
        if (text.endsWith(".")) {
            return true;
        }

        // Try parsing the number
        try {
            Double.parseDouble(text);
            int decimalPointIndex = text.indexOf(".");
            if (decimalPointIndex >= 0) {
                String decimalPart = text.substring(decimalPointIndex + 1);
                return decimalPart.length() <= 2;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates the character and current text for the amount input.
     * It consumes the event if the character would lead to an invalid amount.
     *
     * @param event The KeyEvent to validate.
     * @param currentText The current text of the amount input field.
     */
    public static void validateAmountInput(KeyEvent event, String currentText) {
        String character = event.getCharacter();

        // Allow digits, period, and comma (for EU people)
        if (!character.matches("[0-9.,]")) {
            event.consume();
            return;
        }

        // Only allow one period/comma in the text
        if ((character.equals(".") || character.equals(",")) &&
                (currentText.contains(".") || currentText.contains(","))) {
            event.consume();
            return;
        }

        // Construct the expected text at the current caret position
        StringBuilder expectedTextBuilder = new
                StringBuilder(currentText).insert(currentText.length(), character);
        String expectedText = expectedTextBuilder.toString().replace(',', '.');

        // Validate text
        if (!isValidDouble(expectedText)) {
            event.consume();
        }
    }
}
