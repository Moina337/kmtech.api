package moinammaoueni.kmtech.api.auth;

public interface EmailSender {

    void sendVerificationEmail(String to, String name, String link);

    void sendOrganizationPendingReview(String to, String ownerName, String organizationName);

    void sendOrganizationValidated(String to, String ownerName, String organizationName);

    void sendOrganizationRejected(String to, String ownerName, String organizationName, String reason);

    void sendNewOrganizationPendingAdmin(String to, String organizationName, String ownerName);
}