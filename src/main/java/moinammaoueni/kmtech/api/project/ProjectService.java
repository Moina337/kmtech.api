package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectManagementResponse;

import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryManagement;
import moinammaoueni.kmtech.api.project.dto.ProjectUpdateRequest;

public interface ProjectService {

    // Creation
    ProjectResponseDTO create(ProjectRequestDTO request);

    // Public
    List<ProjectSummaryDTO> getPublishedProjects();

    ProjectResponseDTO getPublicProject(String slug);

    List<ProjectSummaryDTO> getPublicUserProjects(String userSlug);

    List<ProjectSummaryDTO> getPublicOrganizationProjects(
            String organizationSlug
    );

    // Authenticated user
    List<ProjectSummaryManagement> getMyProjects();
    
    ProjectManagementResponse getMyProjectById(Long projectId);

    // Organization management
    List<ProjectSummaryManagement> getOrganizationProjects(
            Long organizationId
    );
    
    ProjectManagementResponse getOrganizationProjectById(Long organizationId, Long projectId);

    // Management
    ProjectResponseDTO update(
            Long projectId,
            ProjectUpdateRequest request
    );

    void delete(Long projectId);

    ProjectResponseDTO publish(Long projectId);

    ProjectResponseDTO draft(Long projectId);

    // Media
    MediaResponseDTO uploadMedia(
            Long projectId,
            MultipartFile file
    );

    void deleteMedia(
            Long projectId,
            Long mediaId
    );
}