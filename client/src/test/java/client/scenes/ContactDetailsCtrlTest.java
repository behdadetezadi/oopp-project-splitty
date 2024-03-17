package client.scenes;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContactDetailsCtrlTest {

    @Test
    public void testValidateEmail_Valid() {
        String validEmail = "2402909753@qq.com";
        Assertions.assertTrue(ContactDetailsCtrl.validateEmail(validEmail));
    }

    @Test
    public void testValidateEmail_Invalid() {
        String invalidEmail = "something random";
        Assertions.assertFalse(ContactDetailsCtrl.validateEmail(invalidEmail));
    }

    @Test
    public void testValidateEmail_InvalidMissingAtSymbol() {
        Assertions.assertFalse(ContactDetailsCtrl.validateEmail("invalidemail.com"));
    }
    @Test
    public void testValidateIban_Valid() {
        String validIban = "GB82WEST12345698765432";
        Assertions.assertTrue(ContactDetailsCtrl.validateIban(validIban));
    }
    @Test
    public void testValidateIban_ValidWithSpaces() {
        Assertions.assertTrue(ContactDetailsCtrl.validateIban("GB82 WEST 1234 5698 7654 32"));
    }


    @Test
    public void testValidateIban_Invalid() {
        String invalidIban = "something random";
        Assertions.assertFalse(ContactDetailsCtrl.validateIban(invalidIban));
    }
    @Test
    public void testValidateIban_InvalidLength()
    {
        Assertions.assertFalse(ContactDetailsCtrl.validateIban("GB82WEST1"));
    }

    @Test
    public void testValidateBIC_Valid() {
        String validBic = "AEUTAEFF";
        Assertions.assertTrue(ContactDetailsCtrl.validateBIC(validBic));
    }

    @Test
    public void testValidateBIC_Invalid() {
        String invalidBic = "something random";
        Assertions.assertFalse(ContactDetailsCtrl.validateBIC(invalidBic));
    }
    @Test
    public void testValidateBIC_InvalidTooShort() {
        Assertions.assertFalse(ContactDetailsCtrl.validateBIC("AEUTA"));
    }
    @Test
    public void testValidateBIC_InvalidTooLong() {
        Assertions.assertFalse(ContactDetailsCtrl.validateBIC("AEUTAAAAAAAAZYR12138"));
    }
    @Test
    public void testValidateBIC_InvalidCharacters() {
        Assertions.assertFalse(ContactDetailsCtrl.validateBIC("AEUTA1"));
    }
}

