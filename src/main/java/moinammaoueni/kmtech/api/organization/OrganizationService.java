package moinammaoueni.kmtech.api.organization;

import java.util.List;

import moinammaoueni.kmtech.api.organization.dto.OrganizationPublicResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;

public interface OrganizationService {

    OrganizationResponseDTO createOrganization(OrganizationRequestDTO request);

    List<OrganizationResponseDTO> findActiveOrganizations();
    

    OrganizationPublicResponseDTO findPublicBySlug(String slug);

    OrganizationResponseDTO updateOrganization(Long memberId, OrganizationRequestDTO request);

    void deactivateOrganization(Long memberId);

    List<MyOrganizationResponseDTO> findMyOrganizations();
}
