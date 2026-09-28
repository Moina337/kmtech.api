package moinammaoueni.kmtech.api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Override
    public void sendVerificationEmail(String to, String name, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Confirmez votre adresse email");
        message.setText("Bonjour " + name + ",\n\n"
                + "Confirmez votre adresse en ouvrant ce lien (valable 24 h) :\n"
                + link + "\n\n"
                + "Si vous n'êtes pas à l'origine de cette inscription, ignorez ce message.");
        mailSender.send(message);
    }
    
    @Override
    public void sendOrganizationPendingReview(String to, String ownerName, String organizationName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Votre organisation est en cours d'examen");
        message.setText("Bonjour " + ownerName + ",\n\n"
                + "Votre organisation \"" + organizationName + "\" a bien été créée. "
                + "Notre équipe l'examine avant de la valider. Vous recevrez un email "
                + "dès que la décision sera prise.");
        mailSender.send(message);
    }

    @Override
    public void sendOrganizationValidated(String to, String ownerName, String organizationName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Votre organisation a été validée");
        message.setText("Bonjour " + ownerName + ",\n\n"
                + "Bonne nouvelle : votre organisation \"" + organizationName + "\" est maintenant active "
                + "et visible publiquement sur KmTech.");
        mailSender.send(message);
    }

    @Override
    public void sendOrganizationRejected(String to, String ownerName, String organizationName, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Votre organisation n'a pas été validée");
        message.setText("Bonjour " + ownerName + ",\n\n"
                + "Votre organisation \"" + organizationName + "\" n'a pas été validée pour la raison suivante :\n\n"
                + reason + "\n\n"
                + "Vous pouvez modifier votre organisation et soumettre une nouvelle demande.");
        mailSender.send(message);
    }

    @Override
    public void sendNewOrganizationPendingAdmin(String to, String organizationName, String ownerName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Nouvelle organisation en attente de validation");
        message.setText("Une nouvelle organisation attend votre validation :\n\n"
                + "Nom : " + organizationName + "\n"
                + "Créée par : " + ownerName + "\n\n"
                + "Connectez-vous à l'espace admin pour l'examiner.");
        mailSender.send(message);
    }
}