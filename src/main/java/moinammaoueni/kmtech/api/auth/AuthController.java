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

@Tag(name = "Authentification", description = "Inscription et connexion, sans authentification requise")
@SecurityRequirement(name = "")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Créer un nouveau compte utilisateur")
    @ApiResponse(responseCode = "201", description = "Compte créé avec succès, token renvoyé")
    @ApiResponse(responseCode = "400", description = "Données d'inscription invalides")
    @ApiResponse(responseCode = "409", description = "Un compte existe déjà avec cet email")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthenticationResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Se connecter avec email et mot de passe")
    @ApiResponse(responseCode = "200", description = "Connexion réussie, token renvoyé")
    @ApiResponse(responseCode = "400", description = "Requête invalide")
    @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    @PostMapping("/login")
    public AuthenticationResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}