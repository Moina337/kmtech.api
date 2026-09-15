package moinammaoueni.kmtech.api.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.PublicOrganisationMembre;
import moinammaoueni.kmtech.api.skill.SkillResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicUserResponseDTO {

    private String slug;

    private String name;

    private String bio;

    private MediaResponseDTO media;

    private String location;

    private String website;

    private String github;

    private String linkedin;

    private List<SkillResponseDTO> skills;

    private List<PublicUserOrganizationResponseDTO> organizations;
}