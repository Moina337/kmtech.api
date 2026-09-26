package moinammaoueni.kmtech.api.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectManagementResponse;
import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryManagement;
import moinammaoueni.kmtech.api.project.dto.ProjectUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectManagementController {

    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectRequestDTO request) {
        ProjectResponseDTO response = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public List<ProjectSummaryManagement> myProjects() {
        return projectService.getMyProjects();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectManagementResponse> getMyProjectById(@PathVariable Long projectId) {
        ProjectManagementResponse response = projectService.getMyProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{projectId}")
    public ProjectResponseDTO update(@PathVariable Long projectId, @RequestBody ProjectUpdateRequest request) {
        return projectService.update(projectId, request);
    }

    // getOrganizationProjectById retiré : organizationId n'a pas de segment
    // correspondant dans "/{projectId}/organization". À clarifier avant de remettre.

    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }

    @PatchMapping("/{projectId}/publish")
    public ProjectResponseDTO publish(@PathVariable Long projectId) {
        return projectService.publish(projectId);
    }

    @PatchMapping("/{projectId}/draft")
    public ProjectResponseDTO draft(@PathVariable Long projectId) {
        return projectService.draft(projectId);
    }

    @PostMapping(value = "/{projectId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public moinammaoueni.kmtech.api.media.dto.MediaResponseDTO uploadMedia(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        return projectService.uploadMedia(projectId, file);
    }

    @DeleteMapping("/{projectId}/media/{mediaId}")
    public void deleteMedia(@PathVariable Long projectId, @PathVariable Long mediaId) {
        projectService.deleteMedia(projectId, mediaId);
    }

    @GetMapping("/organizations/{organizationId}")
    public List<ProjectSummaryManagement> organizationProjects(@PathVariable Long organizationId) {
        return projectService.getOrganizationProjects(organizationId);
    }

    @PostMapping("/{slug}/comment")
    public ResponseEntity<CommentResponseDTO> ajouterCommentaire(@PathVariable String slug, @RequestBody CommentRequestDTO request) {
        return ResponseEntity.ok(projectService.ajouterCommentaire(slug, request));
    }
}