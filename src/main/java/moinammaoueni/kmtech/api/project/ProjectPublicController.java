package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
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
}
