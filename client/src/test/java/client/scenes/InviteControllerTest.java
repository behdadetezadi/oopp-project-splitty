package client.scenes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static client.scenes.InviteController.handleSubmitButtonActionLogic;
import static org.junit.jupiter.api.Assertions.*;


public class InviteControllerTest {

    @Test
    void testEmails() {

        String emailsAsString = "albert@hein.nl\n" +
                "pieter@post.nl\n" +
                "pope@gfrancis.nl";

        ArrayList<String> emails = new ArrayList<>();
        emails.add("albert@hein.nl");
        emails.add("pieter@post.nl");
        emails.add("pope@gfrancis.nl");

        ArrayList<String> result = handleSubmitButtonActionLogic(emailsAsString);
        int j = 0;
        for (String email : result) {
            assertEquals(email, emails.get(j++));
        }
    }
}
