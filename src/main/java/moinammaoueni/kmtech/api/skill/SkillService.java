package moinammaoueni.kmtech.api.skill;

import java.util.List;

public interface SkillService {

    List<SkillResponseDTO> getActiveSkills();

    List<SkillResponseDTO> getMySkills();

    SkillResponseDTO addSkillToMyProfile(Long skillId);

    void removeSkillFromMyProfile(Long skillId);

    SkillAdminResponseDTO createSkill(SkillRequestDTO request);

    SkillAdminResponseDTO updateSkill(Long id, SkillRequestDTO request);

    SkillAdminResponseDTO activateSkill(Long id);

    SkillAdminResponseDTO deactivateSkill(Long id);

    void deleteSkill(Long id);
}
