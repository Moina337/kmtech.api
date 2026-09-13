package moinammaoueni.kmtech.api.organization;

import java.util.List;

import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;

public interface OrganizationService {

    OrganizationResponseDTO createOrganization(OrganizationRequestDTO request);

    List<OrganizationResponseDTO> findActiveOrganizations();

    OrganizationResponseDTO findPublicBySlug(String slug);

    OrganizationResponseDTO updateOrganization(String slug, OrganizationRequestDTO request);

    void deactivateOrganization(String slug);

    List<MyOrganizationResponseDTO> findMyOrganizations();
}
