package moinammaoueni.kmtech.api.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;
import moinammaoueni.kmtech.api.skill.SkillResponseDTO;
import moinammaoueni.kmtech.api.user.UserOrganizationResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String slug;

    private String name;
    
    private String titre;

    private String email;

    private String bio;

    private MediaResponseDTO media;

    private String location;

    private String website;

    private String github;

    private String linkedin;

    private List<SkillResponseDTO> skills;

    private List<UserOrganizationResponseDTO> organizations;
}