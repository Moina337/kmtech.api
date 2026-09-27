package moinammaoueni.kmtech.api.organization;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.organization.dto.OrganizationPublicResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryManagement;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;

public interface OrganizationService {

    OrganizationResponseDTO createOrganization(OrganizationRequestDTO request);
    
    // Upload a logo for an organization
    OrganizationResponseDTO uploadOrganizationLogo(Long organizationId, MultipartFile file);
    
    OrganizationResponseDTO findOrganizationById(Long organizationId);

    List<OrganizationResponseDTO> findActiveOrganizations();
    
     
    OrganizationPublicResponseDTO findPublicBySlug(String slug);

    OrganizationResponseDTO updateOrganization(Long memberId, OrganizationRequestDTO request);

    void deactivateOrganization(Long memberId);

    List<MyOrganizationResponseDTO> findMyOrganizations();
    
    List<OrganizationSummaryManagement> findOrganizationsForAdmin(OrganizationStatus status);
    
    OrganizationResponseDTO validateOrganization(Long organizationId);
    
    OrganizationResponseDTO rejectOrganization(Long organizationId, String reason);
}
