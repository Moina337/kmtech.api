package moinammaoueni.kmtech.api.organization;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.organization.dto.OrganizationPublicResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberService;
import moinammaoueni.kmtech.api.organizationmember.dto.PublicOrganisationMembre;

@Tag(name = "Organisations (public)", description = "Annuaire public des organisations et de leurs membres")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/organizations")
public class OrganizationPublicController {

    private final OrganizationService organizationService;

    private final OrganizationMemberService organizationMemberService;

    @Operation(summary = "Lister les organisations actives")
    @ApiResponse(responseCode = "200", description = "Liste des organisations actives récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<OrganizationResponseDTO>> findActiveOrganizations() {
        return ResponseEntity.ok(organizationService.findActiveOrganizations());
    }

    @Operation(summary = "Récupérer une organisation par son slug")
    @ApiResponse(responseCode = "200", description = "Organisation trouvée")
    @ApiResponse(responseCode = "404", description = "Aucune organisation ne correspond à ce slug")
    @GetMapping("/{slug}")
    public ResponseEntity<OrganizationPublicResponseDTO> findPublicBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(organizationService.findPublicBySlug(slug));
    }

    @Operation(summary = "Lister les membres publics d'une organisation")
    @ApiResponse(responseCode = "200", description = "Liste des membres récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Aucune organisation ne correspond à ce slug")
    @GetMapping("/{slug}/members")
    public ResponseEntity<List<PublicOrganisationMembre>> getPublicMembers(@PathVariable String slug) {
        return ResponseEntity.ok(organizationMemberService.getPublicMembers(slug));
    }
}