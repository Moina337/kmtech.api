package moinammaoueni.kmtech.api.organizationmember;

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
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberRoleRequestDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations")
public class OrganizationMemberController {

    private final OrganizationMemberService organizationMemberService;

    @GetMapping("/{slug}/members")
    public ResponseEntity<List<OrganizationMemberResponseDTO>> getMembers(@PathVariable Long slug) {
        return ResponseEntity.ok(organizationMemberService.getMembers(slug));
    }

    @PostMapping("/{slug}/members/{userSlug}")
    public ResponseEntity<OrganizationMemberResponseDTO> addMember(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userSlug") String userSlug) {

        OrganizationMemberResponseDTO response = organizationMemberService.addMember(organizationId, userSlug);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{slug}/members/{userSlug}")
    public ResponseEntity<OrganizationMemberResponseDTO> updateMemberRole(
            @PathVariable("organisationId") Long organisationId,
            @PathVariable("userSlug") String userSlug,
            @Valid @RequestBody OrganizationMemberRoleRequestDTO request) {

        return ResponseEntity.ok(organizationMemberService.updateMemberRole(organisationId, userSlug, request));
    }

    @DeleteMapping("/{slug}/members/{userSlug}")
    public ResponseEntity<Void> removeMember(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userSlug") String userSlug) {

        organizationMemberService.removeMember(organizationId, userSlug);
        return ResponseEntity.noContent().build();
    }
}
