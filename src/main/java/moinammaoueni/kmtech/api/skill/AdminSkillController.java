package moinammaoueni.kmtech.api.skill;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Tag(name = "Compétences (admin)", description = "Gestion du catalogue de compétences — réservé aux administrateurs")
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/skills")
public class AdminSkillController {

    private final SkillService skillService;

    @Operation(summary = "Créer une compétence dans le catalogue")
    @ApiResponse(responseCode = "201", description = "Compétence créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "409", description = "Une compétence avec ce nom existe déjà")
    @PostMapping
    public ResponseEntity<SkillAdminResponseDTO> createSkill(
            @Valid @RequestBody SkillRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.createSkill(request));
    }

    @Operation(summary = "Modifier une compétence du catalogue")
    @ApiResponse(responseCode = "200", description = "Compétence modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Compétence introuvable")
    @PatchMapping("/{id}")
    public ResponseEntity<SkillAdminResponseDTO> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequestDTO request) {

        return ResponseEntity.ok(skillService.updateSkill(id, request));
    }

    @Operation(summary = "Activer une compétence")
    @ApiResponse(responseCode = "200", description = "Compétence activée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Compétence introuvable")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<SkillAdminResponseDTO> activateSkill(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.activateSkill(id));
    }

    @Operation(summary = "Désactiver une compétence")
    @ApiResponse(responseCode = "200", description = "Compétence désactivée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Compétence introuvable")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SkillAdminResponseDTO> deactivateSkill(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.deactivateSkill(id));
    }

    @Operation(summary = "Supprimer définitivement une compétence du catalogue")
    @ApiResponse(responseCode = "204", description = "Compétence supprimée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Compétence introuvable")
    @ApiResponse(responseCode = "409", description = "Impossible de supprimer : compétence encore utilisée par des profils")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}