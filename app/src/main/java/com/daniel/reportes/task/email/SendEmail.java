package com.daniel.reportes.task.email;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail extends AsyncTask<String, Integer, Integer> {

    private Properties mailProperties;

    public SendEmail() {

        mailProperties = new Properties();

        mailProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.port", "465");
        mailProperties.put("mail.smtp.socketFactory.port", "465");
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }

    @Override
    protected Integer doInBackground(String... email) {

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("antonion7a@gmail.com", "nolasco123@");
            }
        };

        Session session = Session.getDefaultInstance(mailProperties, authenticator);

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress("antonion7a@gmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email[0]));
            message.setSubject("Código de verificación");
            message.addHeader("Importance", "High");
            message.addHeader("Priority", "Urgent");
            message.addHeader("X-Priority", "1");
            message.setText("This is your code -> " + email[1]);

            Transport.send(message);
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
