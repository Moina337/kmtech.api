package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;

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
    List<ProjectSummaryDTO> getMyProjects();

    // Organization management
    List<ProjectSummaryDTO> getOrganizationProjects(
            Long organizationId
    );

    // Management
    ProjectResponseDTO update(
            Long projectId,
            ProjectRequestDTO request
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