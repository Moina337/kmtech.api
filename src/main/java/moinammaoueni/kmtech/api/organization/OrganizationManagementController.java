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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberService;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrganizationManagementController {

    private final OrganizationService organizationService;
    private final OrganizationMemberService organizationMemberService;

    @PostMapping("/organizations")
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @Valid @RequestBody OrganizationRequestDTO request) {

        OrganizationResponseDTO response =
                organizationService.createOrganization(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    // UPLOAD LOGO
    @PostMapping( value ="/organizations/{organizationId}/logo",
    		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrganizationResponseDTO> uploadOrganizationLogo(
			@PathVariable Long organizationId,
			@Valid @RequestParam("file") MultipartFile file) {

		OrganizationResponseDTO response =
				organizationService.uploadOrganizationLogo(organizationId, file);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}

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

    @GetMapping("/users/me/organizations")
    public ResponseEntity<List<MyOrganizationResponseDTO>> findMyOrganizations() {

        return ResponseEntity.ok(
                organizationService.findMyOrganizations()
        );
    }

    @GetMapping("/organizations/id/{organizationId}/members")
    public ResponseEntity<List<OrganizationMemberResponseDTO>> findOrganizationMembers(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                organizationMemberService.getManagementMembers(organizationId)
        );
    }

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

    @DeleteMapping("/organizations/{organizationId}")
    public ResponseEntity<Void> deactivateOrganization(
            @PathVariable Long organizationId) {

        organizationService.deactivateOrganization(
                organizationId
        );

        return ResponseEntity.noContent().build();
    }
}