package moinammaoueni.kmtech.api.organization;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryManagement;
import moinammaoueni.kmtech.api.organization.dto.RejectOrganizationRequest;

@Tag(name = "Organisations (admin)", description = "Validation, refus et supervision des organisations — réservé aux administrateurs")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/organizations")
@RequiredArgsConstructor
public class OrganizationAdminController {

    private final OrganizationService organizationService;

    @Operation(summary = "Lister les organisations, en vue admin", description = "Filtrable par statut via ?status=PENDING, ?status=ACTIVE, etc. Sans paramètre, retourne toutes les organisations.")
    @ApiResponse(responseCode = "200", description = "Liste des organisations récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @GetMapping
    public ResponseEntity<List<OrganizationSummaryManagement>> findOrganizationsForAdmin(
            @RequestParam(required = false) OrganizationStatus status) {
        return ResponseEntity.ok(organizationService.findOrganizationsForAdmin(status));
    }
    
    @Operation(summary = "Récupérer le détail complet d'une organisation, en vue admin")
    @ApiResponse(responseCode = "200", description = "Organisation trouvée")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Organisation introuvable")
    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponseDTO> findOrganizationByIdAdmin(@PathVariable Long organizationId) {
        return ResponseEntity.ok(organizationService.findOrganizationById(organizationId));
    }

    @Operation(summary = "Valider une organisation en attente")
    @ApiResponse(responseCode = "200", description = "Organisation validée, désormais visible publiquement")
    @ApiResponse(responseCode = "400", description = "Cette organisation n'est pas en attente de validation")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Organisation introuvable")
    @PatchMapping("/{organizationId}/validate")
    public ResponseEntity<OrganizationResponseDTO> validateOrganization(@PathVariable Long organizationId) {
        return ResponseEntity.ok(organizationService.validateOrganization(organizationId));
    }

    @Operation(summary = "Refuser une organisation en attente")
    @ApiResponse(responseCode = "200", description = "Organisation refusée")
    @ApiResponse(responseCode = "400", description = "Cette organisation n'est pas en attente de validation")
    @ApiResponse(responseCode = "403", description = "Accès réservé aux administrateurs")
    @ApiResponse(responseCode = "404", description = "Organisation introuvable")
    @PatchMapping("/{organizationId}/reject")
    public ResponseEntity<OrganizationResponseDTO> rejectOrganization(
            @PathVariable Long organizationId,
            @RequestBody RejectOrganizationRequest request) {
        return ResponseEntity.ok(organizationService.rejectOrganization(organizationId, request.reason()));
    }
}