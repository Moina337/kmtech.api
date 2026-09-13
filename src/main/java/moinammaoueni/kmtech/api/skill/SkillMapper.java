package moinammaoueni.kmtech.api.skill;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillResponseDTO toSkillResponseDTO(Skill skill);

    SkillAdminResponseDTO toSkillAdminResponseDTO(Skill skill);
}
