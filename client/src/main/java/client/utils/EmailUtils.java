package client.utils;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtils {

    /**
     * send an email using credentials
      * @param host host
     * @param port port
     * @param username username of email
     * @param password password of email
     * @param toAddress address to send
     * @param subject subject of the email
     * @param message message content
     * @throws MessagingException exception for handling
     */
    public static void sendEmail(String host, int port, final String username,
                                 final String password, String toAddress, String subject,
                                 String message) throws MessagingException {
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Get the Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(username));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        mimeMessage.setSubject(subject);
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
    }
}
