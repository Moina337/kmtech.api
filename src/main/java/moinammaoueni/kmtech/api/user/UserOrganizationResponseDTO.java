package moinammaoueni.kmtech.api.user;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRole;

public record UserOrganizationResponseDTO(
        String slug,
        String name,
        MediaResponseDTO media,
        OrganizationMemberRole role
) {}