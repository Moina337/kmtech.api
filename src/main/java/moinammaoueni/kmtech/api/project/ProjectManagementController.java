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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectManagementResponse;
import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryManagement;
import moinammaoueni.kmtech.api.project.dto.ProjectUpdateRequest;

import java.util.List;

@Tag(name = "Projets (gestion)", description = "Création, modification et suppression de ses propres projets — authentification requise")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectManagementController {

    private final ProjectService projectService;

    @Operation(summary = "Créer un nouveau projet")
    @ApiResponse(responseCode = "201", description = "Projet créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données de projet invalides")
    @PostMapping()
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody ProjectRequestDTO request) {
        ProjectResponseDTO response = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lister mes propres projets")
    @ApiResponse(responseCode = "200", description = "Liste de mes projets récupérée avec succès")
    @GetMapping("/me")
    public List<ProjectSummaryManagement> myProjects() {
        return projectService.getMyProjects();
    }

    @Operation(summary = "Récupérer un de mes projets par son id, en vue gestion")
    @ApiResponse(responseCode = "200", description = "Projet trouvé")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à cet id")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectManagementResponse> getMyProjectById(@PathVariable Long projectId) {
        ProjectManagementResponse response = projectService.getMyProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Modifier un de mes projets")
    @ApiResponse(responseCode = "200", description = "Projet modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données de modification invalides")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à cet id")
    @PatchMapping("/{projectId}")
    public ProjectResponseDTO update(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.update(projectId, request);
    }

    // getOrganizationProjectById retiré : organizationId n'a pas de segment
    // correspondant dans "/{projectId}/organization". À clarifier avant de remettre.

    @Operation(summary = "Supprimer un de mes projets")
    @ApiResponse(responseCode = "200", description = "Projet supprimé avec succès")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à cet id")
    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }

    @Operation(summary = "Publier un projet (le rendre visible publiquement)")
    @ApiResponse(responseCode = "200", description = "Projet publié avec succès")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à cet id")
    @PatchMapping("/{projectId}/publish")
    public ProjectResponseDTO publish(@PathVariable Long projectId) {
        return projectService.publish(projectId);
    }

    @Operation(summary = "Repasser un projet en brouillon")
    @ApiResponse(responseCode = "200", description = "Projet repassé en brouillon")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à cet id")
    @PatchMapping("/{projectId}/draft")
    public ProjectResponseDTO draft(@PathVariable Long projectId) {
        return projectService.draft(projectId);
    }

    @Operation(summary = "Ajouter un média à un projet", description = "Envoi en multipart/form-data")
    @ApiResponse(responseCode = "200", description = "Média ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Fichier invalide")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @PostMapping(value = "/{projectId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public moinammaoueni.kmtech.api.media.dto.MediaResponseDTO uploadMedia(
            @PathVariable Long projectId,
            @Parameter(description = "Fichier image ou vidéo à associer au projet")
            @RequestParam("file") MultipartFile file) {
        return projectService.uploadMedia(projectId, file);
    }

    @Operation(summary = "Supprimer un média d'un projet")
    @ApiResponse(responseCode = "200", description = "Média supprimé avec succès")
    @ApiResponse(responseCode = "403", description = "Ce projet ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Média introuvable")
    @DeleteMapping("/{projectId}/media/{mediaId}")
    public void deleteMedia(@PathVariable Long projectId, @PathVariable Long mediaId) {
        projectService.deleteMedia(projectId, mediaId);
    }

    @Operation(summary = "Lister les projets d'une organisation, en vue gestion")
    @ApiResponse(responseCode = "200", description = "Liste des projets récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas membre de cette organisation")
    @GetMapping("/organizations/{organizationId}")
    public List<ProjectSummaryManagement> organizationProjects(@PathVariable Long organizationId) {
        return projectService.getOrganizationProjects(organizationId);
    }

    @Operation(summary = "Ajouter un commentaire à un projet")
    @ApiResponse(responseCode = "200", description = "Commentaire ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Contenu du commentaire invalide")
    @ApiResponse(responseCode = "404", description = "Aucun projet ne correspond à ce slug")
    @PostMapping("/{slug}/comment")
    public ResponseEntity<CommentResponseDTO> ajouterCommentaire(@PathVariable String slug, @Valid @RequestBody CommentRequestDTO request) {
        return ResponseEntity.ok(projectService.ajouterCommentaire(slug, request));
    }
}