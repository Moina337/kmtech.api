package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;

public interface ProjectService {

    ProjectResponseDTO create(ProjectRequestDTO request);

    List<ProjectSummaryDTO> getPublishedProjects();

    ProjectResponseDTO getPublicProject(String slug);

    List<ProjectSummaryDTO> getMyProjects();

    ProjectResponseDTO update(Long projectId, ProjectRequestDTO request);

    void delete(Long projectId);

    ProjectResponseDTO publish(Long projectId);

    ProjectResponseDTO draft(Long projectId);

    MediaResponseDTO uploadMedia(Long projectId, MultipartFile file);

    void deleteMedia(Long projectId, Long mediaId);
}
