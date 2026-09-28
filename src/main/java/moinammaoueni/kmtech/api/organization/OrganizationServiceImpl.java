package moinammaoueni.kmtech.api.organization;

import java.text.Normalizer;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.auth.EmailSender;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.media.Media;
import moinammaoueni.kmtech.api.media.MediaService;
import moinammaoueni.kmtech.api.media.MediaType;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;
import moinammaoueni.kmtech.api.organization.dto.OrganizationPublicResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationRequestDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryManagement;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMember;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRepository;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRole;
import moinammaoueni.kmtech.api.organizationmember.dto.MyOrganizationResponseDTO;
import moinammaoueni.kmtech.api.user.User;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationMapper organizationMapper;
    private final CurrentUser currentUser;
    private final MediaService mediaService;
    private final EmailSender emailSender;
    
    @Value("${app.admin-notification-email}")
    private String adminNotificationEmail;

    @Override
    public OrganizationResponseDTO createOrganization(OrganizationRequestDTO request) {
        User creator = currentUser.get();

        String slug = generateUniqueSlug(request.getName());

        Organization organization = Organization.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .type(request.getType())
                .website(request.getWebsite())
                .location(request.getLocation())
                .status(OrganizationStatus.PENDING)
                .build();

        organization = organizationRepository.save(organization);

        OrganizationMember ownerMember = OrganizationMember.builder()
                .organization(organization)
                .user(creator)
                .role(OrganizationMemberRole.OWNER)
                .build();

        organizationMemberRepository.save(ownerMember);
        
        emailSender.sendOrganizationPendingReview(creator.getEmail(), creator.getName(), organization.getName());
        emailSender.sendNewOrganizationPendingAdmin(adminNotificationEmail, organization.getName(), creator.getName());

        return organizationMapper.toResponseDTO(organization);
    }
    
    

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponseDTO> findActiveOrganizations() {

        List<Organization> organizations =
                organizationRepository.findAllByStatusOrderByCreatedAtDesc(OrganizationStatus.ACTIVE);

        return organizations.stream()
                .map(organizationMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationPublicResponseDTO findPublicBySlug(String slug) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        if (organization.getStatus() != OrganizationStatus.ACTIVE) {
            throw new ResourceNotFoundException("Organisation introuvable");
        }
        
        List<OrganizationMember> members = organizationMemberRepository.findByOrganization(organization);

        return organizationMapper.toPublicResponseDTO(organization,members);
    }

    @Override
    public OrganizationResponseDTO updateOrganization(Long memberId, OrganizationRequestDTO request) {
    	
        Organization organization = organizationRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        User user = currentUser.get();
        requireOwner(organization, user);

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new BadRequestException("Une organisation inactive ne peut pas être modifiée");
        }

        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setType(request.getType());
        organization.setWebsite(request.getWebsite());
        organization.setLocation(request.getLocation());

        Organization updated = organizationRepository.save(organization);
        return organizationMapper.toResponseDTO(updated);
    }

    @Override
    public void deactivateOrganization(Long memberId) {
        Organization organization = organizationRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        User current = currentUser.get();
        requireOwner(organization, current);

        organization.setStatus(OrganizationStatus.INACTIVE);
        organizationRepository.save(organization);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationSummaryManagement> findOrganizationsForAdmin(OrganizationStatus status) {
        // TODO: requireAdmin(currentUser.get());

        List<Organization> organizations = (status != null)
                ? organizationRepository.findAllByStatusOrderByCreatedAtDesc(status)
                : organizationRepository.findAllByOrderByCreatedAtDesc();

        return organizations.stream()
                .map(organizationMapper::toSummaryManagement)
                .toList();
    }

    @Override
    public OrganizationResponseDTO validateOrganization(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        // TODO: requireAdmin(currentUser.get()); — à activer une fois le
        // mécanisme de rôle confirmé, ne pas laisser cette méthode ouverte en prod

        if (organization.getStatus() != OrganizationStatus.PENDING) {
            throw new BadRequestException("Seule une organisation en attente peut être validée");
        }

        organization.setStatus(OrganizationStatus.ACTIVE);
        
        User owner = organizationMemberRepository.findByOrganizationAndRole(organization, OrganizationMemberRole.OWNER)
                .map(OrganizationMember::getUser)
                .orElseThrow(() -> new IllegalStateException("Organisation sans owner"));

        emailSender.sendOrganizationValidated(owner.getEmail(), owner.getName(), organization.getName());
        return organizationMapper.toResponseDTO(organizationRepository.save(organization));
    }

    @Override
    public OrganizationResponseDTO rejectOrganization(Long organizationId, String reason) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        // TODO: requireAdmin(currentUser.get()); — même remarque que ci-dessus

        if (organization.getStatus() != OrganizationStatus.PENDING) {
            throw new BadRequestException("Seule une organisation en attente peut être refusée");
        }

        organization.setStatus(OrganizationStatus.REJECTED);
        // TODO: stocker "reason" quelque part (nouveau champ sur Organization,
        // ou table séparée d'historique) si tu veux le montrer au owner —
        // pour l'instant le paramètre est reçu mais pas persisté, dis-moi
        // où tu veux qu'il aille
        return organizationMapper.toResponseDTO(organizationRepository.save(organization));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyOrganizationResponseDTO> findMyOrganizations() {
        User current = currentUser.get();

        return organizationMemberRepository.findByUserOrderByJoinedAtAsc(current)
                .stream()
                .map(member -> new MyOrganizationResponseDTO(
                		member.getId(),
                        member.getOrganization().getSlug(),
                        member.getOrganization().getName(),
                        member.getOrganization().getType(),
                        member.getOrganization().getStatus(),
                        member.getRole(),
                        member.getJoinedAt()))
                .toList();
    }

    private String generateUniqueSlug(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        String base = normalized.isBlank() ? "organization" : normalized;
        String slug = base;
        int counter = 2;

        while (organizationRepository.existsBySlug(slug)) {
            slug = base + "-" + counter;
            counter++;
        }

        return slug;
    }

    void requireOwner(Organization organization, User user) {
        OrganizationMember member = organizationMemberRepository
                .findByOrganizationAndUser(organization, user)
                .orElseThrow(() -> new AccessDeniedException("Vous n'êtes pas membre de cette organisation"));

        if (member.getRole() != OrganizationMemberRole.OWNER) {
            throw new AccessDeniedException("Seul le propriétaire peut effectuer cette action");
        }
    }



	@Override
	public OrganizationResponseDTO uploadOrganizationLogo(Long organizationId, MultipartFile file) {
		
		Organization organization = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));
 
		Media media = mediaService.replace(organization.getMedia(),
				file, MediaFolder.ORGANIZATIONS, MediaType.ORGANIZATION);
		
		organization.setMedia(media); 	
		 
		
		return organizationMapper.toResponseDTO(organizationRepository.save(organization));
	}



	@Override
	public OrganizationResponseDTO findOrganizationById(Long organizationId) {
		
		 Organization organization = organizationRepository.findById(organizationId)
	                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));
		 
		 
		 
		return organizationMapper.toResponseDTO(organization);
	}
}
