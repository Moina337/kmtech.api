package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectPublicController {

	private final ProjectService projectService;

	@GetMapping
	public List<ProjectSummaryDTO> listPublished() {
		return projectService.getPublishedProjects();
	}

	@GetMapping("/{slug}")
	public ProjectResponseDTO getBySlug(@PathVariable String slug) {
		return projectService.getPublicProject(slug);
	}

	@GetMapping("/users/{userSlug}/projects")
	public List<ProjectSummaryDTO> listUserProjects(@PathVariable String userSlug) {
		return projectService.getPublicUserProjects(userSlug);
	}

	@GetMapping("/organizations/{organizationSlug}/projects")
	public List<ProjectSummaryDTO> listOrganizationProjects(@PathVariable String organizationSlug) {
		return projectService.getPublicOrganizationProjects(organizationSlug);
	}

	@PostMapping("/{slug}/comment")
	public ResponseEntity<CommentResponseDTO> ajouterCommentaire(String slug, CommentRequestDTO request) {

		return ResponseEntity.ok(projectService.ajouterCommentaire(slug, request));

	}
}