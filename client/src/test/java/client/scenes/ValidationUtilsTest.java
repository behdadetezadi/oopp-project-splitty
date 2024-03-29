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
    }

    @Test
    void testIsValidName() {
        assertTrue(ValidationUtils.isValidName("John Doe"));
        assertFalse(ValidationUtils.isValidName("John123"));
        assertFalse(ValidationUtils.isValidName("John_Doe"));
        assertTrue(ValidationUtils.isValidName("Mikey"));
        assertTrue(ValidationUtils.isValidName("mikey"));
        assertFalse(ValidationUtils.isValidName(""));



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

    }

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("email@example.com"));
        assertFalse(ValidationUtils.isValidEmail("email@example"));
        assertFalse(ValidationUtils.isValidEmail("email@.com"));
    }

    @Test
    void testIsValidIBAN() {
        assertTrue(ValidationUtils.isValidIBAN("NL91ABNA0417164300"));
        assertFalse(ValidationUtils.isValidIBAN("NL91ABNA04171643001"));
        assertFalse(ValidationUtils.isValidIBAN("US91ABNA0417164300"));

    }

    @Test
    void testIsValidBIC() {
        assertTrue(ValidationUtils.isValidBIC("ABNANL2A"));
        assertFalse(ValidationUtils.isValidBIC("ABNANL2A1"));
        assertFalse(ValidationUtils.isValidBIC("ABNANL2"));
    }

    @Test
    void testIsValidLanguage() {
        assertTrue(ValidationUtils.isValidLanguage("Dutch"));
        assertTrue(ValidationUtils.isValidLanguage("English"));
        assertFalse(ValidationUtils.isValidLanguage("French"));
        assertFalse(ValidationUtils.isValidLanguage(""));

    }

    @Test
    void testIsValidCapitalizedName() {
        assertTrue(ValidationUtils.isValidCapitalizedName("John"));
        assertFalse(ValidationUtils.isValidCapitalizedName("john"));
        assertTrue(ValidationUtils.isValidCapitalizedName("Jane Doe"));
        assertTrue(ValidationUtils.isValidCapitalizedName("Mike Doe"));
        assertTrue(ValidationUtils.isValidCapitalizedName("Josh"));
        assertTrue(ValidationUtils.isValidCapitalizedName("A"));
        assertFalse(ValidationUtils.isValidCapitalizedName(""));
        assertFalse(ValidationUtils.isValidCapitalizedName("0"));

    }
}
