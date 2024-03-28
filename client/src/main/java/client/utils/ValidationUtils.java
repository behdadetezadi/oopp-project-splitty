package client.utils;
import javafx.scene.control.ComboBox;
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

    /**
     * checks if the name is valid by only checking it has letters
     * @param text String
     * @return a boolean value
     */
    public static boolean isValidName(String text) {
        return text.matches("[a-zA-Z\\s]+");
    }

    /**
     * ensures proper username with no weird characters (letters/# are accepted)
     * @param text String
     * @return boolean
     */
    public static boolean isValidUsername(String text) {
        return text.matches("\\w+");
    }

    /**
     * ensures proper email format
     * @param text String
     * @return a boolean
     */
    public static boolean isValidEmail(String text) {
        return text.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * ensures *common* IBAN format is used in our code
     * @param text String
     * @return boolean
     */
    public static boolean isValidIBAN(String text) {
        return text.matches("[A-Z]{2}\\d{2}[a-zA-Z0-9]{1,30}");
    }

    /**
     * ensures *common* BIC format is used in our code
     * @param text String
     * @return boolean
     */
    public static boolean isValidBIC(String text) {
        return text.matches("[A-Z]{6}[A-Z2-9][A-NP-Z1-9]");
    }

    /**
     * checks that an option is selected, not left blank
     * @param languageComboBox  ComboBox<String>
     * @return a boolean
     */
    public static boolean isValidLanguage(ComboBox<String> languageComboBox) {
        return languageComboBox.getValue() != null;
    }

    /**
     * Names start with capital letters
     * @param text String
     * @return boolean
     */
    public static boolean isValidCapitalizedName(String text) {
        return text.matches("[A-Z][a-zA-Z\\s]*");
    }

}
