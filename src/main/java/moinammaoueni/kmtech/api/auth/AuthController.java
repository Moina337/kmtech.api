package moinammaoueni.kmtech.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.dto.AuthenticationResponseDTO;
import moinammaoueni.kmtech.api.auth.dto.LoginRequestDTO;
import moinammaoueni.kmtech.api.auth.dto.RegisterRequestDTO;
import moinammaoueni.kmtech.api.auth.dto.ResendVerificationRequest;
import moinammaoueni.kmtech.api.auth.dto.VerifyEmailRequest;

@Tag(name = "Authentification", description = "Inscription, connexion et vérification d'email, sans authentification requise")
@SecurityRequirement(name = "")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Créer un compte")
    @ApiResponse(responseCode = "201", description = "Compte créé, email de vérification envoyé")
    @ApiResponse(responseCode = "400", description = "Données d'inscription invalides")
    @ApiResponse(responseCode = "409", description = "Un compte existe déjà avec cet email")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Se connecter avec email et mot de passe")
    @ApiResponse(responseCode = "200", description = "Connexion réussie, token renvoyé")
    @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    @ApiResponse(responseCode = "403", description = "Email non vérifié")
    @PostMapping("/login")
    public AuthenticationResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @Operation(summary = "Confirmer son adresse email avec le token reçu par email")
    @ApiResponse(responseCode = "204", description = "Email confirmé, le compte est actif")
    @ApiResponse(responseCode = "400", description = "Lien invalide ou expiré")
    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request.token());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Renvoyer l'email de vérification",
            description = "Répond toujours 204, que l'adresse existe ou non")
    @ApiResponse(responseCode = "204", description = "Demande prise en compte")
    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        authService.resendVerification(request.email());
        return ResponseEntity.noContent().build();
    }
}