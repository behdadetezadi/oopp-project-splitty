package client.scenes;

import client.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testIsValidDouble() {
        assertTrue(ValidationUtils.isValidDouble("123.45"));
        assertTrue(ValidationUtils.isValidDouble("123."));
        assertTrue(ValidationUtils.isValidDouble("123"));
        assertFalse(ValidationUtils.isValidDouble("123.456"));
        assertFalse(ValidationUtils.isValidDouble("abc.12"));
        assertFalse(ValidationUtils.isValidDouble("123.45.67"));
        assertTrue(ValidationUtils.isValidDouble("-123.45"));
    }

    @Test
    void testIsValidName() {
        assertTrue(ValidationUtils.isValidName("John Doe"));
        assertFalse(ValidationUtils.isValidName("John123"));
        assertFalse(ValidationUtils.isValidName("John_Doe"));
        assertTrue(ValidationUtils.isValidName("Mikey"));
        assertTrue(ValidationUtils.isValidName("mikey"));
        assertFalse(ValidationUtils.isValidName(""));
        assertFalse(ValidationUtils.isValidName("Anne-Marie"));
        assertFalse(ValidationUtils.isValidName("O'Neil"));
        assertFalse(ValidationUtils.isValidName("12345"));



    }

    @Test
    void testIsValidUsername() {
        assertTrue(ValidationUtils.isValidUsername("john_doe"));
        assertFalse(ValidationUtils.isValidUsername("john.doe"));
        assertFalse(ValidationUtils.isValidUsername("john doe"));
        assertFalse(ValidationUtils.isValidUsername("john@doe"));
        assertFalse(ValidationUtils.isValidUsername("---"));
        assertFalse(ValidationUtils.isValidUsername(""));
        assertFalse(ValidationUtils.isValidUsername("]["));
        assertTrue(ValidationUtils.isValidUsername("a"));
        assertTrue(ValidationUtils.isValidUsername("_john_doe123"));
        assertFalse(ValidationUtils.isValidUsername("john*doe"));
        assertTrue(ValidationUtils.isValidUsername("12345"));
        assertFalse(ValidationUtils.isValidUsername(" "));

    }

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("email@example.com"));
        assertFalse(ValidationUtils.isValidEmail("email@example"));
        assertFalse(ValidationUtils.isValidEmail("email@.com"));
        assertTrue(ValidationUtils.isValidEmail("name.surname@example.co.uk"));
        assertFalse(ValidationUtils.isValidEmail("email@example,com"));
        assertFalse(ValidationUtils.isValidEmail("@no-local-part.com"));
        assertFalse(ValidationUtils.isValidEmail("no-at-symbol"));
        assertFalse(ValidationUtils.isValidEmail("no-tld@domain"));
        assertTrue(ValidationUtils.isValidEmail("email@domain.com"));
    }

    @Test
    void testIsValidIBAN() {
        assertFalse(ValidationUtils.isValidIBAN("GB82 WEST12345698765432"));
        assertTrue(ValidationUtils.isValidIBAN("GB82 WEST 1234 5698 32"));
        assertTrue(ValidationUtils.isValidIBAN("NL91 ABNA 0417 1643 00"));
        assertFalse(ValidationUtils.isValidIBAN("ABCD1234567890"));

    }

    @Test
    void testIsValidBIC() {
        assertFalse(ValidationUtils.isValidBIC("NEDSZAJJXXX"));
        assertFalse(ValidationUtils.isValidBIC("NEDSZAJJXX"));
        assertFalse(ValidationUtils.isValidBIC("NEDSZAJJXXXXX"));
        assertTrue(ValidationUtils.isValidBIC("NEDS2AJJ"));
    }

    @Test
    void testIsValidLanguage() {
        assertTrue(ValidationUtils.isValidLanguage("Dutch"));
        assertTrue(ValidationUtils.isValidLanguage("English"));
        assertFalse(ValidationUtils.isValidLanguage("French"));
        assertFalse(ValidationUtils.isValidLanguage(""));

    }

    @Test
    void testAutoCapitalizeWord() {
        assertEquals("John", ValidationUtils.autoCapitalizeWord("john"));
        assertEquals("John", ValidationUtils.autoCapitalizeWord("John"));
        assertEquals("J", ValidationUtils.autoCapitalizeWord("j"));
        assertEquals("", ValidationUtils.autoCapitalizeWord(""));
        assertEquals("John doe", ValidationUtils.autoCapitalizeWord("john doe")); }


}
