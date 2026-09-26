package moinammaoueni.kmtech.api.skill;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Compétences (public)", description = "Catalogue des compétences disponibles")
@RestController
@RequestMapping("/api/public/skills")
@RequiredArgsConstructor
public class SkillPublicController {

    private final SkillService skillService;

    @Operation(summary = "Lister les compétences actives du catalogue")
    @ApiResponse(responseCode = "200", description = "Liste des compétences récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getActiveSkills() {
        return ResponseEntity.ok(skillService.getActiveSkills());
    }
}