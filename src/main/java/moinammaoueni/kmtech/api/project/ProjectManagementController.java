package moinammaoueni.kmtech.api.project;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectManagementController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ProjectResponseDTO create(@RequestBody ProjectRequestDTO request) {
        return projectService.create(request);
    }

    @GetMapping("/users/me/projects")
    public List<ProjectSummaryDTO> myProjects() {
        return projectService.getMyProjects();
    }

    @PatchMapping("/projects/{projectId}")
    public ProjectResponseDTO update(@PathVariable Long projectId, @RequestBody ProjectRequestDTO request) {
        return projectService.update(projectId, request);
    }

    @DeleteMapping("/projects/{projectId}")
    public void delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }

    @PatchMapping("/projects/{projectId}/publish")
    public ProjectResponseDTO publish(@PathVariable Long projectId) {
        return projectService.publish(projectId);
    }

    @PatchMapping("/projects/{projectId}/draft")
    public ProjectResponseDTO draft(@PathVariable Long projectId) {
        return projectService.draft(projectId);
    }

    @PostMapping(value = "/projects/{projectId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public moinammaoueni.kmtech.api.media.dto.MediaResponseDTO uploadMedia(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        return projectService.uploadMedia(projectId, file);
    }

    @DeleteMapping("/projects/{projectId}/media/{mediaId}")
    public void deleteMedia(@PathVariable Long projectId, @PathVariable Long mediaId) {
        projectService.deleteMedia(projectId, mediaId);
    }

    @GetMapping("/organizations/{organizationId}/projects")
    public List<ProjectSummaryDTO> organizationProjects(@PathVariable Long organizationId) {
        return projectService.getOrganizationProjects(organizationId);
    }
}