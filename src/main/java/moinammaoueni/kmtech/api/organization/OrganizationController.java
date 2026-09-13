package moinammaoueni.kmtech.api.organization;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/organizations")
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @Valid @RequestBody OrganizationRequestDTO request) {

        OrganizationResponseDTO response = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationResponseDTO>> findActiveOrganizations() {
        return ResponseEntity.ok(organizationService.findActiveOrganizations());
    }

    @GetMapping("/organizations/{slug}")
    public ResponseEntity<OrganizationResponseDTO> findPublicBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(organizationService.findPublicBySlug(slug));
    }

    @PatchMapping("/organizations/{slug}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable String slug,
            @Valid @RequestBody OrganizationRequestDTO request) {

        return ResponseEntity.ok(organizationService.updateOrganization(slug, request));
    }

    @DeleteMapping("/organizations/{slug}")
    public ResponseEntity<Void> deactivateOrganization(@PathVariable String slug) {
        organizationService.deactivateOrganization(slug);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/organizations")
    public ResponseEntity<List<MyOrganizationResponseDTO>> findMyOrganizations() {
        return ResponseEntity.ok(organizationService.findMyOrganizations());
    }
}
