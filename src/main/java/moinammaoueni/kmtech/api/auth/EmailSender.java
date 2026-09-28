package moinammaoueni.kmtech.api.auth;

public interface EmailSender {

    void sendVerificationEmail(String to, String name, String link);
}