package moinammaoueni.kmtech.api.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.user.User;
import moinammaoueni.kmtech.api.user.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final Duration TOKEN_VALIDITY = Duration.ofHours(24);
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Transactional
    public void sendVerification(User user) {
        tokenRepository.deleteByUser(user);

        String rawToken = generateToken();
        tokenRepository.save(EmailVerificationToken.builder()
                .tokenHash(hash(rawToken))
                .user(user)
                .expiresAt(LocalDateTime.now().plus(TOKEN_VALIDITY))
                .build());

        try {
            emailSender.sendVerificationEmail(
                    user.getEmail(),
                    user.getName(),
                    frontendUrl + "/verify-email?token=" + rawToken);
        } catch (Exception e) {
            log.error("Échec d'envoi de l'email de vérification à l'utilisateur {}", user.getId(), e);
        }
    }

    @Transactional
    public void verify(String rawToken) {
        EmailVerificationToken token = tokenRepository.findByTokenHash(hash(rawToken))
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new BadRequestException("Invalid or expired verification link"));

        User user = token.getUser();
        if (user.getStatus() != User.Status.PENDING) {
            throw new BadRequestException("Invalid or expired verification link");
        }

        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);
        tokenRepository.deleteByUser(user);
    }

    @Transactional
    public void resend(String email) {
        userRepository.findByEmail(email)
                .filter(u -> u.getStatus() == User.Status.PENDING)
                .filter(this::cooldownElapsed)
                .ifPresent(this::sendVerification);
    }

    private boolean cooldownElapsed(User user) {
        return tokenRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .map(t -> t.getCreatedAt().plus(RESEND_COOLDOWN).isBefore(LocalDateTime.now()))
                .orElse(true);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}