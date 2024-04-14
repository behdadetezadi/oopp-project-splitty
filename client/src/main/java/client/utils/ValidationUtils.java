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
     * ensures *normal* IBAN format is used in our code
     * @param text String
     * @return boolean
     */
    public static boolean isValidIBAN(String text) {
        String sanitizedText = text.replaceAll("\\s+", "");
        String ibanPattern = "[A-Z]{2}\\d{2}[A-Z]{4}\\d{10}";

        return sanitizedText.matches(ibanPattern);
    }


    /**
     * ensures *common* BIC format is used in our code/// EDIT : only requires 8 alphanumeric chars for specificity
     * @param text String
     * @return boolean
     */
    public static boolean isValidBIC(String text) {
        return text.matches("[A-Z0-9]{8}");
    }

    /**
     * checks that an option is selected, not left blank
     * @param language String
     * @return a boolean
     */
    public static boolean isValidLanguage(String language) {
        return language.matches("Dutch|English|German");
    }

    /**
     * capitalize the first letter of first/last name
     * @param word String
     * @return String
     */
    public static String autoCapitalizeWord(String word) {
        String trimmedWord = word.trim();

        if (trimmedWord.length() > 1) {
            return Character.toUpperCase(trimmedWord.charAt(0)) + trimmedWord.substring(1).toLowerCase();
        } else {
            return trimmedWord.toUpperCase();
        }
    }

}

