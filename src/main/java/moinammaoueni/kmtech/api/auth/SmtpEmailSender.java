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
}