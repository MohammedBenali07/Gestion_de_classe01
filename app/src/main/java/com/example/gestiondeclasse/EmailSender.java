package com.example.gestiondeclasse;

import java.io.File;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

public class EmailSender {
    public static void sendEmail(final String fromEmail, final String password,
                                 String toEmail, String subject, String messageBody, String filePath) {
        // Configuration des propriétés SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Serveur SMTP Gmail
        properties.put("mail.smtp.port", "587");           // Port SMTP
        properties.put("mail.smtp.auth", "true");          // Authentification obligatoire
        properties.put("mail.smtp.starttls.enable", "true"); // Activer STARTTLS

        // Créer une session avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password); // Identifiants Gmail
            }
        });

        try {
            // Construire le message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail)); // Expéditeur
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Destinataire
            message.setSubject(subject); // Sujet de l'email

            // Corps du message (partie texte)
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(messageBody); // Contenu de l'email

            // Partie pour le fichier joint
            MimeBodyPart fileBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(new File(filePath)); // Chemin du fichier
            fileBodyPart.setDataHandler(new DataHandler(source));
            fileBodyPart.setFileName(new File(filePath).getName()); // Nom du fichier dans l'email

            // Combiner texte et fichier
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart); // Ajouter le texte
            multipart.addBodyPart(fileBodyPart); // Ajouter le fichier

            message.setContent(multipart); // Ajouter toutes les parties au message

            // Envoyer le message
            Transport.send(message);

            System.out.println("Email envoyé avec succès avec le fichier attaché.");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
