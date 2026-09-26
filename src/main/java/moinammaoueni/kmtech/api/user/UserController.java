package moinammaoueni.kmtech.api.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.user.dto.ChangePasswordRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Tag(name = "Utilisateurs (gestion)", description = "Gestion de son propre compte — authentification requise")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Récupérer mon profil complet")
    @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès")
    @GetMapping("/me")
    public UserResponseDTO findMe() {
        return userService.findMe();
    }

    @Operation(summary = "Modifier mon profil")
    @ApiResponse(responseCode = "200", description = "Profil modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données de modification invalides")
    @PatchMapping("/me")
    public UserResponseDTO updateMe(@Valid @RequestBody UpdateUserRequestDTO request) {
        return userService.updateMe(request);
    }

    @Operation(summary = "Modifier ma photo de profil", description = "Envoi en multipart/form-data")
    @ApiResponse(responseCode = "200", description = "Photo de profil mise à jour avec succès")
    @ApiResponse(responseCode = "400", description = "Fichier invalide")
    @PatchMapping(value = "/me/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaResponseDTO> updateProfilePhoto(
            @Parameter(description = "Fichier image de profil")
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePhoto(file));
    }

    @Operation(summary = "Changer mon mot de passe")
    @ApiResponse(responseCode = "200", description = "Mot de passe modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Mot de passe actuel incorrect ou nouveau mot de passe invalide")
    @PatchMapping("/me/password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        userService.changePassword(request);
    }
}