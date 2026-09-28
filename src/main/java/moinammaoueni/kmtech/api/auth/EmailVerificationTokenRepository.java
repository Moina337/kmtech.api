package moinammaoueni.kmtech.api.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import moinammaoueni.kmtech.api.user.User;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    Optional<EmailVerificationToken> findFirstByUserOrderByCreatedAtDesc(User user);

    void deleteByUser(User user);
}