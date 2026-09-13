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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/skills")
public class AdminSkillController {

    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<SkillAdminResponseDTO> createSkill(
            @Valid @RequestBody SkillRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.createSkill(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SkillAdminResponseDTO> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequestDTO request) {

        return ResponseEntity.ok(skillService.updateSkill(id, request));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SkillAdminResponseDTO> activateSkill(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.activateSkill(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SkillAdminResponseDTO> deactivateSkill(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.deactivateSkill(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
