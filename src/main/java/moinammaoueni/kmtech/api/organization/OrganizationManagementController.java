package moinammaoueni.kmtech.api.organization;

import java.util.List;

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
import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberService;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;

@Tag(name = "Organisations (gestion)", description = "Création et administration des organisations — authentification requise")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrganizationManagementController {

    private final OrganizationService organizationService;
    private final OrganizationMemberService organizationMemberService;

    @Operation(summary = "Créer une nouvelle organisation")
    @ApiResponse(responseCode = "201", description = "Organisation créée avec succès, le créateur en devient owner")
    @ApiResponse(responseCode = "400", description = "Données d'organisation invalides")
    @ApiResponse(responseCode = "409", description = "Une organisation avec ce nom ou ce domaine existe déjà")
    @PostMapping("/organizations")
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @Valid @RequestBody OrganizationRequestDTO request) {

        OrganizationResponseDTO response =
                organizationService.createOrganization(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Envoyer le logo de l'organisation", description = "Envoi en multipart/form-data")
    @ApiResponse(responseCode = "201", description = "Logo envoyé avec succès")
    @ApiResponse(responseCode = "400", description = "Fichier invalide")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas owner/admin de cette organisation")
    @PostMapping(value = "/organizations/{organizationId}/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrganizationResponseDTO> uploadOrganizationLogo(
            @PathVariable Long organizationId,
            @Parameter(description = "Fichier image du logo")
            @RequestParam("file") MultipartFile file) {

        OrganizationResponseDTO response =
                organizationService.uploadOrganizationLogo(organizationId, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    @Operation(summary = "Récupérer une organisation par son id, en vue gestion")
    @ApiResponse(responseCode = "200", description = "Organisation trouvée")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas owner de cette organisation")
    @ApiResponse(responseCode = "404", description = "Organisation introuvable")
    @GetMapping("/organizations/{organizationId}")
    public ResponseEntity<OrganizationResponseDTO> findOrganizationById(@PathVariable Long organizationId) {
        return ResponseEntity.ok(organizationService.findOrganizationById(organizationId));
    }

    @Operation(summary = "Ajouter un membre à l'organisation")
    @ApiResponse(responseCode = "201", description = "Membre ajouté avec succès")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas owner/admin de cette organisation")
    @ApiResponse(responseCode = "404", description = "Organisation ou utilisateur introuvable")
    @PostMapping("/organizations/{organizationId}/members/{userSlug}")
    public ResponseEntity<OrganizationMemberResponseDTO> addMember(
            @PathVariable Long organizationId,
            @PathVariable String userSlug) {

        OrganizationMemberResponseDTO response =
                organizationMemberService.addMember(
                        organizationId,
                        userSlug
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Retirer un membre de l'organisation")
    @ApiResponse(responseCode = "204", description = "Membre retiré avec succès")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas owner/admin de cette organisation")
    @ApiResponse(responseCode = "404", description = "Organisation ou membre introuvable")
    @DeleteMapping("/organizations/{organizationId}/members/{userSlug}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long organizationId,
            @PathVariable String userSlug) {

        organizationMemberService.removeMember(
                organizationId,
                userSlug
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lister mes organisations")
    @ApiResponse(responseCode = "200", description = "Liste de mes organisations récupérée avec succès")
    @GetMapping("/users/me/organizations")
    public ResponseEntity<List<MyOrganizationResponseDTO>> findMyOrganizations() {

        return ResponseEntity.ok(
                organizationService.findMyOrganizations()
        );
    }

    @Operation(summary = "Lister les membres d'une organisation, en vue gestion")
    @ApiResponse(responseCode = "200", description = "Liste des membres récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas membre de cette organisation")
    @GetMapping("/organizations/id/{organizationId}/members")
    public ResponseEntity<List<OrganizationMemberResponseDTO>> findOrganizationMembers(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                organizationMemberService.getManagementMembers(organizationId)
        );
    }

    @Operation(summary = "Modifier une organisation")
    @ApiResponse(responseCode = "200", description = "Organisation modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données de modification invalides")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas owner/admin de cette organisation")
    @ApiResponse(responseCode = "404", description = "Organisation introuvable")
    @PatchMapping("/organizations/{organizationId}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable Long organizationId,
            @Valid @RequestBody OrganizationRequestDTO request) {

        return ResponseEntity.ok(
                organizationService.updateOrganization(
                        organizationId,
                        request
                )
        );
    }

    @Operation(summary = "Désactiver une organisation")
    @ApiResponse(responseCode = "204", description = "Organisation désactivée avec succès")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas owner de cette organisation")
    @ApiResponse(responseCode = "404", description = "Organisation introuvable")
    @DeleteMapping("/organizations/{organizationId}")
    public ResponseEntity<Void> deactivateOrganization(
            @PathVariable Long organizationId) {

        organizationService.deactivateOrganization(
                organizationId
        );

        return ResponseEntity.noContent().build();
    }
}