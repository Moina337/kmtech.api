package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;

@Tag(name = "Projets (public)", description = "Consultation publique des projets, sans authentification")
@RestController
@RequestMapping("/api/public/projects")
@RequiredArgsConstructor
public class ProjectPublicController {

    private final ProjectService projectService;

    @Operation(summary = "Lister les projets publiés")
    @ApiResponse(responseCode = "200", description = "Liste des projets publiés récupérée avec succès")
    @GetMapping
    public List<ProjectSummaryDTO> listPublished() {
        return projectService.getPublishedProjects();
    }

    @Operation(summary = "Récupérer un projet par son slug")
    @ApiResponse(responseCode = "200", description = "Projet trouvé")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à ce slug")
    @GetMapping("/{slug}")
    public ProjectResponseDTO getBySlug(@PathVariable String slug) {
        return projectService.getPublicProject(slug);
    }

    @Operation(summary = "Lister les projets publics d'un utilisateur")
    @ApiResponse(responseCode = "200", description = "Liste des projets de l'utilisateur récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Aucun utilisateur ne correspond à ce slug")
    @GetMapping("/users/{userSlug}/projects")
    public List<ProjectSummaryDTO> listUserProjects(@PathVariable String userSlug) {
        return projectService.getPublicUserProjects(userSlug);
    }

    @Operation(summary = "Lister les projets publics d'une organisation")
    @ApiResponse(responseCode = "200", description = "Liste des projets de l'organisation récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Aucune organisation ne correspond à ce slug")
    @GetMapping("/organizations/{organizationSlug}/projects")
    public List<ProjectSummaryDTO> listOrganizationProjects(@PathVariable String organizationSlug) {
        return projectService.getPublicOrganizationProjects(organizationSlug);
    }
}