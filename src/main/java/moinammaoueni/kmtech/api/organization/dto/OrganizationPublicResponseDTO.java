package moinammaoueni.kmtech.api.organization.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.OrganizationType;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;

@Getter
@Setter
public class OrganizationPublicResponseDTO {

    private String slug;
    private String name;
    private String description;
    private OrganizationType type;
    private String website;
    private String location;
    private MediaResponseDTO media;
    private List<OrganizationMemberResponseDTO> members;
}
