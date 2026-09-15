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
import moinammaoueni.kmtech.api.organizationmember.dto.PublicOrganisationMembre;
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
    public List<OrganizationMemberResponseDTO> getManagementMembers(
            Long organizationId) {

        Organization organization = getOrganization(organizationId);

        User actor = currentUser.get();

        requireOwnerOrAdmin(organization, actor);
        requireActiveOrganization(organization);

        return organizationMemberRepository
                .findByOrganizationOrderByJoinedAtAsc(organization)
                .stream()
                .map(organizationMemberMapper::toResponseDTO)
                .toList();
    }

    @Override
    public OrganizationMemberResponseDTO addMember(
            Long organizationId,
            String userSlug) {

        Organization organization = getOrganization(organizationId);

        User actor = currentUser.get();

        requireOwnerOrAdmin(organization, actor);
        requireActiveOrganization(organization);

        User targetUser = getUserBySlug(userSlug);

        if (organizationMemberRepository
                .existsByOrganizationAndUser(organization, targetUser)) {

            throw new ConflictException(
                    "L'utilisateur appartient déjà à cette organisation"
            );
        }

        OrganizationMember member = OrganizationMember.builder()
                .organization(organization)
                .user(targetUser)
                .role(OrganizationMemberRole.MEMBER)
                .build();

        OrganizationMember savedMember =
                organizationMemberRepository.save(member);

        return organizationMemberMapper.toResponseDTO(savedMember);
    }

    @Override
    public OrganizationMemberResponseDTO updateMemberRole(
            Long organizationId,
            String userSlug,
            OrganizationMemberRoleRequestDTO request) {

        if (request == null || request.getRole() == null) {
            throw new BadRequestException("Le rôle est obligatoire");
        }

        if (request.getRole() == OrganizationMemberRole.OWNER) {
            throw new BadRequestException(
                    "Le rôle OWNER ne peut pas être attribué via cette action"
            );
        }

        Organization organization = getOrganization(organizationId);

        User actor = currentUser.get();

        requireOwnerOrAdmin(organization, actor);
        requireActiveOrganization(organization);

        User targetUser = getUserBySlug(userSlug);

        OrganizationMember member =
                organizationMemberRepository
                        .findByOrganizationAndUser(
                                organization,
                                targetUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cet utilisateur n'est pas membre de l'organisation"
                                )
                        );

        if (member.getRole() == OrganizationMemberRole.OWNER) {
            throw new BadRequestException(
                    "Le propriétaire ne peut pas être modifié"
            );
        }

        member.setRole(request.getRole());

        return organizationMemberMapper.toResponseDTO(member);
    }

    @Override
    public void removeMember(
            Long organizationId,
            String userSlug) {

        Organization organization = getOrganization(organizationId);

        User actor = currentUser.get();

        requireOwnerOrAdmin(organization, actor);
        requireActiveOrganization(organization);

        User targetUser = getUserBySlug(userSlug);

        OrganizationMember member =
                organizationMemberRepository
                        .findByOrganizationAndUser(
                                organization,
                                targetUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cet utilisateur n'est pas membre de l'organisation"
                                )
                        );

        if (member.getRole() == OrganizationMemberRole.OWNER) {
            throw new BadRequestException(
                    "Le propriétaire ne peut pas être retiré de l'organisation"
            );
        }

        organizationMemberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicOrganisationMembre> getPublicMembers(
            String organizationSlug) {

        Organization organization =
                organizationRepository.findBySlug(organizationSlug)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organisation introuvable"
                                )
                        );

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new ResourceNotFoundException(
                    "Organisation introuvable"
            );
        }

        return organizationMemberRepository
                .findByOrganizationOrderByJoinedAtAsc(organization)
                .stream()
                .map(organizationMemberMapper::toPublicResponseDTO)
                .toList();
    }

    private Organization getOrganization(Long organizationId) {

        return organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organisation introuvable"
                        )
                );
    }

    private User getUserBySlug(String userSlug) {

        return userRepository.findBySlug(userSlug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );
    }

    private OrganizationMember requireOwnerOrAdmin(
            Organization organization,
            User user) {

        OrganizationMember member =
                organizationMemberRepository
                        .findByOrganizationAndUser(
                                organization,
                                user
                        )
                        .orElseThrow(() ->
                                new AccessDeniedException(
                                        "Vous n'êtes pas membre de cette organisation"
                                )
                        );

        if (member.getRole() != OrganizationMemberRole.OWNER
                && member.getRole() != OrganizationMemberRole.ADMIN) {

            throw new AccessDeniedException(
                    "Droits insuffisants pour gérer les membres"
            );
        }

        return member;
    }

    private void requireActiveOrganization(
            Organization organization) {

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new BadRequestException(
                    "Cette organisation est inactive"
            );
        }
    }
}