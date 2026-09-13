package moinammaoueni.kmtech.api.organizationmember;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.common.exception.ConflictException;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.organization.OrganizationRepository;
import moinammaoueni.kmtech.api.organization.OrganizationStatus;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberRoleRequestDTO;
import moinammaoueni.kmtech.api.user.User;
import moinammaoueni.kmtech.api.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationMemberServiceImpl implements OrganizationMemberService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final UserRepository userRepository;
    private final OrganizationMemberMapper organizationMemberMapper;
    private final CurrentUser currentUser;

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMemberResponseDTO> getMembers(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            User current = currentUser.get();
            if (!isOwnerOrAdmin(organization, current)) {
                throw new AccessDeniedException("Cette organisation est inactive");
            }
        }

        return organizationMemberRepository.findByOrganizationOrderByJoinedAtAsc(organization)
                .stream()
                .map(organizationMemberMapper::toResponseDTO)
                .toList();
    }

    @Override
    public OrganizationMemberResponseDTO addMember(Long organizationId, String userSlug) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        User actor = currentUser.get();
        requireOwnerOrAdmin(organization, actor);

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new BadRequestException("Une organisation inactive ne peut pas recevoir de nouveaux membres");
        }

        User targetUser = userRepository.findBySlug(userSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (organizationMemberRepository.existsByOrganizationAndUser(organization, targetUser)) {
            throw new ConflictException("L'utilisateur appartient déjà à cette organisation");
        }

        OrganizationMember member = OrganizationMember.builder()
                .organization(organization)
                .user(targetUser)
                .role(OrganizationMemberRole.MEMBER)
                .build();

        return organizationMemberMapper.toResponseDTO(organizationMemberRepository.save(member));
    }

    @Override
    public OrganizationMemberResponseDTO updateMemberRole(
            Long organizationId,
            String userSlug,
            OrganizationMemberRoleRequestDTO request
    ) {
        if (request == null || request.getRole() == null) {
            throw new BadRequestException("Le rôle est obligatoire");
        }

        if (request.getRole() == OrganizationMemberRole.OWNER) {
            throw new BadRequestException("Le rôle OWNER ne peut pas être attribué via cette action");
        }

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        User actor = currentUser.get();
        requireOwnerOrAdmin(organization, actor);

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new BadRequestException("Une organisation inactive ne peut pas modifier ses membres");
        }

        User targetUser = userRepository.findBySlug(userSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        OrganizationMember member = organizationMemberRepository.findByOrganizationAndUser(organization, targetUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cet utilisateur n'est pas membre de l'organisation"));

        if (member.getRole() == OrganizationMemberRole.OWNER) {
            throw new BadRequestException("Le propriétaire ne peut pas être modifié");
        }

        member.setRole(request.getRole());
        return organizationMemberMapper.toResponseDTO(organizationMemberRepository.save(member));
    }

    @Override
    public void removeMember(Long organizationId, String userSlug) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        User actor = currentUser.get();
        requireOwnerOrAdmin(organization, actor);

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new BadRequestException("Une organisation inactive ne peut pas retirer de membre");
        }

        User targetUser = userRepository.findBySlug(userSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        OrganizationMember member = organizationMemberRepository.findByOrganizationAndUser(organization, targetUser)
                .orElseThrow(() -> new ResourceNotFoundException("Cet utilisateur n'est pas membre de l'organisation"));

        if (member.getRole() == OrganizationMemberRole.OWNER) {
            throw new BadRequestException("Le propriétaire ne peut pas être retiré de l'organisation");
        }

        organizationMemberRepository.delete(member);
    }

    private void requireOwnerOrAdmin(Organization organization, User user) {
        OrganizationMember member = organizationMemberRepository.findByOrganizationAndUser(organization, user)
                .orElseThrow(() -> new AccessDeniedException("Vous n'êtes pas membre de cette organisation"));

        if (member.getRole() != OrganizationMemberRole.OWNER && member.getRole() != OrganizationMemberRole.ADMIN) {
            throw new AccessDeniedException("Droits insuffisants pour gérer les membres");
        }
    }

    private boolean isOwnerOrAdmin(Organization organization, User user) {
        return organizationMemberRepository.findByOrganizationAndUser(organization, user)
                .map(member -> member.getRole() == OrganizationMemberRole.OWNER
                        || member.getRole() == OrganizationMemberRole.ADMIN)
                .orElse(false);
    }
}
