package moinammaoueni.kmtech.api.skill;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.common.exception.ConflictException;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.user.User;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillMapper skillMapper;
    private final CurrentUser currentUser;

    @Override
    @Transactional(readOnly = true)
    public List<SkillResponseDTO> getActiveSkills() {
        return skillRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(skillMapper::toSkillResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillResponseDTO> getMySkills() {
        User user = currentUser.get();

        return userSkillRepository.findAllByUserId(user.getId())
                .stream()
                .map(UserSkill::getSkill)
                .map(skillMapper::toSkillResponseDTO)
                .toList();
    }

    @Override
    public SkillResponseDTO addSkillToMyProfile(Long skillId) {
        User user = currentUser.get();

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable"));

        if (!skill.isActive()) {
            throw new BadRequestException("Cette compétence est inactive");
        }

        if (userSkillRepository.existsByUserIdAndSkillId(user.getId(), skillId)) {
            throw new BadRequestException("Vous avez déjà cette compétence");
        }

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .build();

        userSkillRepository.save(userSkill);

        return skillMapper.toSkillResponseDTO(skill);
    }

    @Override
    public void removeSkillFromMyProfile(Long skillId) {
        User user = currentUser.get();

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable"));

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cette compétence n'est pas dans votre profil"));

        userSkillRepository.delete(userSkill);
    }

    @Override
    public SkillAdminResponseDTO createSkill(SkillRequestDTO request) {
        String trimmedName = normalizeName(request.getName());

        if (skillRepository.existsByNameIgnoreCase(trimmedName)) {
            throw new BadRequestException("Cette compétence existe déjà");
        }

        Skill skill = Skill.builder()
                .name(trimmedName)
                .active(true)
                .build();

        Skill savedSkill = skillRepository.save(skill);

        return skillMapper.toSkillAdminResponseDTO(savedSkill);
    }

    @Override
    public SkillAdminResponseDTO updateSkill(Long id, SkillRequestDTO request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable"));

        String trimmedName = normalizeName(request.getName());

        if (skillRepository.existsByNameIgnoreCaseAndIdNot(trimmedName, id)) {
            throw new BadRequestException("Cette compétence existe déjà");
        }

        skill.setName(trimmedName);

        return skillMapper.toSkillAdminResponseDTO(skillRepository.save(skill));
    }

    @Override
    public SkillAdminResponseDTO activateSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable"));

        skill.setActive(true);

        return skillMapper.toSkillAdminResponseDTO(skillRepository.save(skill));
    }

    @Override
    public SkillAdminResponseDTO deactivateSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable"));

        skill.setActive(false);

        return skillMapper.toSkillAdminResponseDTO(skillRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable"));

        if (userSkillRepository.existsBySkillId(id)) {
            throw new ConflictException(
                    "Cette compétence est utilisée par des utilisateurs et ne peut pas être supprimée."
            );
        }

        skillRepository.delete(skill);
    }

    private String normalizeName(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new BadRequestException("Le nom de la compétence est obligatoire");
        }

        return name.trim();
    }
}
