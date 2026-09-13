package moinammaoueni.kmtech.api.organizationmember;

import java.util.List;

import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberRoleRequestDTO;

public interface OrganizationMemberService {

    List<OrganizationMemberResponseDTO> getMembers(Long organizationId);

    OrganizationMemberResponseDTO addMember(Long organizationId, String userSlug);

    OrganizationMemberResponseDTO updateMemberRole(
            Long organizationId,
            String userSlug,
            OrganizationMemberRoleRequestDTO request
    );

    void removeMember(Long organizationId, String userSlug);
}
