package utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Mail {

	public static void sendMail(String recepient, String content) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "chaalabfadi@gmail.com";
        String password = "7fadi:92573019";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recepient, content);

        //Send mail
        Transport.send(message);
        System.out.println("Message sent successfully");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Mail");
        alert.setHeaderText("Card information");
        alert.setContentText("please check your email first");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("ATM System");
            message.setText(content);
            return message;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
