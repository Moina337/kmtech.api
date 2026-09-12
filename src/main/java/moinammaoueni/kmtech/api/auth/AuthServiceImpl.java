package moinammaoueni.kmtech.api.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.dto.AuthenticationResponseDTO;
import moinammaoueni.kmtech.api.auth.dto.LoginRequestDTO;
import moinammaoueni.kmtech.api.auth.dto.RegisterRequestDTO;
import moinammaoueni.kmtech.api.user.User;
import moinammaoueni.kmtech.api.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String slug = generateUniqueSlug(request.getName());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .slug(slug)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        user = userRepository.save(user);

        return AuthenticationResponseDTO.builder()
                .slug(user.getSlug())
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(null)
                .tokenType(null)
                .expiresIn(null)
                .build();
    }

    @Override
    public AuthenticationResponseDTO login(LoginRequestDTO request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() ->
                            new IllegalArgumentException("User not found"));

            if (user.getStatus() != User.Status.ACTIVE) {
                throw new IllegalArgumentException(
                        "User account is not active"
                );
            }

            String token = jwtService.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );

            long expiresIn = jwtService.getExpirationTime();

            return AuthenticationResponseDTO.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresIn(expiresIn)
                    .slug(user.getSlug())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }
    }

    private String generateUniqueSlug(String name) {

        String baseSlug = name
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        String slug = baseSlug;
        int counter = 2;

        while (userRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }
}