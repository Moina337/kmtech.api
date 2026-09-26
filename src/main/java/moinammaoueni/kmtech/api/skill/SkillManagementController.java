package moinammaoueni.kmtech.api.skill;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Compétences (gestion)", description = "Gestion des compétences de son propre profil — authentification requise")
@RestController
@RequestMapping("/api/users/me/skills")
@RequiredArgsConstructor
public class SkillManagementController {

    private final SkillService skillService;

    @Operation(summary = "Lister mes compétences")
    @ApiResponse(responseCode = "200", description = "Liste de mes compétences récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getMySkills() {
        return ResponseEntity.ok(skillService.getMySkills());
    }

    @Operation(summary = "Ajouter une compétence à mon profil")
    @ApiResponse(responseCode = "201", description = "Compétence ajoutée à mon profil avec succès")
    @ApiResponse(responseCode = "404", description = "Aucune compétence ne correspond à cet id dans le catalogue")
    @ApiResponse(responseCode = "409", description = "Cette compétence est déjà sur mon profil")
    @PostMapping("/{skillId}")
    public ResponseEntity<SkillResponseDTO> addSkillToMyProfile(@PathVariable Long skillId) {
        SkillResponseDTO response = skillService.addSkillToMyProfile(skillId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Retirer une compétence de mon profil")
    @ApiResponse(responseCode = "204", description = "Compétence retirée de mon profil avec succès")
    @ApiResponse(responseCode = "404", description = "Cette compétence n'est pas sur mon profil")
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> removeSkillFromMyProfile(@PathVariable Long skillId) {
        skillService.removeSkillFromMyProfile(skillId);
        return ResponseEntity.noContent().build();
    }
}