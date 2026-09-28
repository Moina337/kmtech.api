package moinammaoueni.kmtech.api.auth;

import java.text.Normalizer;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.dto.AuthenticationResponseDTO;
import moinammaoueni.kmtech.api.auth.dto.LoginRequestDTO;
import moinammaoueni.kmtech.api.auth.dto.RegisterRequestDTO;
import moinammaoueni.kmtech.api.common.exception.ConflictException;
import moinammaoueni.kmtech.api.common.exception.EmailNotVerifiedException;
import moinammaoueni.kmtech.api.user.User;
import moinammaoueni.kmtech.api.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {

        Optional<User> existing = userRepository.findByEmail(request.getEmail());

        if (existing.isPresent()) {
            User pending = existing.get();

            if (pending.getStatus() != User.Status.PENDING) {
                throw new ConflictException("Email already registered");
            }

            // Adresse jamais vérifiée : elle n'appartient encore à personne,
            // la dernière inscription gagne.
            pending.setName(request.getName());
            pending.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(pending);
            emailVerificationService.sendVerification(pending);
            return toRegisterResponse(pending);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .slug(generateUniqueSlug(request.getName()))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .status(User.Status.PENDING)
                .build();

        user = userRepository.save(user);
        emailVerificationService.sendVerification(user);
        return toRegisterResponse(user);
    }

    @Override
    public AuthenticationResponseDTO login(LoginRequestDTO request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (DisabledException e) {
            boolean pendingWithGoodPassword = userRepository.findByEmail(request.getEmail())
                    .filter(u -> u.getStatus() == User.Status.PENDING)
                    .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                    .isPresent();

            if (pendingWithGoodPassword) {
                throw new EmailNotVerifiedException();
            }
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return AuthenticationResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .slug(user.getSlug())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public void verifyEmail(String token) {
        emailVerificationService.verify(token);
    }

    @Override
    public void resendVerification(String email) {
        emailVerificationService.resend(email);
    }

    private AuthenticationResponseDTO toRegisterResponse(User user) {
        return AuthenticationResponseDTO.builder()
                .slug(user.getSlug())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    private String generateUniqueSlug(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        String base = normalized.isBlank() ? "user" : normalized;
        String slug = base;
        int counter = 2;

        while (userRepository.existsBySlug(slug)) {
            slug = base + "-" + counter;
            counter++;
        }

        return slug;
    }
}