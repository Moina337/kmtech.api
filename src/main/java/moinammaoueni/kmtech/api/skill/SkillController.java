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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/skills")
    public ResponseEntity<List<SkillResponseDTO>> getActiveSkills() {
        return ResponseEntity.ok(skillService.getActiveSkills());
    }

    @GetMapping("/users/me/skills")
    public ResponseEntity<List<SkillResponseDTO>> getMySkills() {
        return ResponseEntity.ok(skillService.getMySkills());
    }

    @PostMapping("/users/me/skills/{skillId}")
    public ResponseEntity<SkillResponseDTO> addSkillToMyProfile(@PathVariable Long skillId) {
        SkillResponseDTO response = skillService.addSkillToMyProfile(skillId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/users/me/skills/{skillId}")
    public ResponseEntity<Void> removeSkillFromMyProfile(@PathVariable Long skillId) {
        skillService.removeSkillFromMyProfile(skillId);
        return ResponseEntity.noContent().build();
    }
}
