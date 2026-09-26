package moinammaoueni.kmtech.api.skill;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/skills")
@RequiredArgsConstructor
public class SkillPublicController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getActiveSkills() {
        return ResponseEntity.ok(skillService.getActiveSkills());
    }
}