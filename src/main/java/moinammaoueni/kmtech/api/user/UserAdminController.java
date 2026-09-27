package moinammaoueni.kmtech.api.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.user.dto.ChangeUserRoleRequest;
import moinammaoueni.kmtech.api.user.dto.ChangeUserStatusRequest;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Tag(name = "Utilisateurs (admin)", description = "Supervision des comptes — réservé aux administrateurs")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @Operation(summary = "Changer le rôle d'un utilisateur")
    @ApiResponse(responseCode = "200", description = "Rôle modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Rôle invalide, ou tentative de modifier son propre rôle")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> changeUserRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserRoleRequest request) {
        return ResponseEntity.ok(userAdminService.changeUserRole(id, request.role()));
    }
    
    @Operation(summary = "Lister les utilisateurs")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userAdminService.findAll());
    }

    @Operation(summary = "Détail d'un utilisateur")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userAdminService.findById(id));
    }

    @Operation(summary = "Suspendre ou réactiver un compte (INACTIVE / ACTIVE)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> changeUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserStatusRequest request) {
        return ResponseEntity.ok(userAdminService.changeUserStatus(id, request.status()));
    }
}