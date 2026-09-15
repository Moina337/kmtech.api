package moinammaoueni.kmtech.api.organization;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.organization.dto.OrganizationPublicResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberService;
import moinammaoueni.kmtech.api.organizationmember.dto.PublicOrganisationMembre;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class OrganizationPublicController {
	
	private final OrganizationService organizationService;
	
	private final OrganizationMemberService organizationMemberService;
	
	@GetMapping("/organizations")
    public ResponseEntity<List<OrganizationResponseDTO>> findActiveOrganizations() {
        return ResponseEntity.ok(organizationService.findActiveOrganizations());
    }
	
	 @GetMapping("/organizations/{slug}")
	    public ResponseEntity<OrganizationPublicResponseDTO> findPublicBySlug(@PathVariable String slug) {
	        return ResponseEntity.ok(organizationService.findPublicBySlug(slug));
	    }
	 
	 @GetMapping("/organizations/{slug}/members")
	    public ResponseEntity<List<PublicOrganisationMembre>> getPublicMembers(@PathVariable String slug) {
	        return ResponseEntity.ok(organizationMemberService.getPublicMembers(slug));
	    }

}
