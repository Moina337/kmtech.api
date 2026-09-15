package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.media.Media;
import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.media.MediaRepository;
import moinammaoueni.kmtech.api.media.MediaService;
import moinammaoueni.kmtech.api.media.MediaType;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;
import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.organization.OrganizationRepository;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMember;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRepository;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRole;
import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;
import moinammaoueni.kmtech.api.project.ProjectMapper;
import moinammaoueni.kmtech.api.user.User;
import moinammaoueni.kmtech.api.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final CurrentUser currentUser;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    @Override
    public ProjectResponseDTO create(ProjectRequestDTO request) {

        User user = currentUser.get();

        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setWebsite(request.website());
        project.setGithub(request.github());

        if (request.organizationId() != null) {
            Organization org = organizationRepository.findById(request.organizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

            OrganizationMember member = organizationMemberRepository.findByOrganizationAndUser(org, user)
                    .orElseThrow(() -> new BadRequestException("Vous n'êtes pas membre de cette organisation"));

            if (!(member.getRole() == OrganizationMemberRole.OWNER || member.getRole() == OrganizationMemberRole.ADMIN)) {
                throw new BadRequestException("Seuls le propriétaire et les administrateurs peuvent créer des projets pour cette organisation");
            }

            project.setOrganization(org);
        } else {
            project.setUser(user);
        }

        // generate slug
        project.setSlug(generateSlug(project.getName()));

        project.setStatus(ProjectStatus.DRAFT);

        Project saved = projectRepository.save(project);

        return buildResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getPublishedProjects() {
        List<Project> projects = projectRepository.findByStatus(ProjectStatus.PUBLISHED);

        return projects.stream().map(p -> {
            ProjectSummaryDTO summary = projectMapper.toSummaryDTO(p);

            // cover: first media by createdAt asc
            var mediaList = mediaRepository.findByProjectOrderByCreatedAtAsc(p);
            if (!mediaList.isEmpty()) {
                summary = new ProjectSummaryDTO(
                        p.getSlug(),
                        p.getName(),
                        p.getDescription(),
                        mediaMapper.toMediaResponseDTO(mediaList.get(0)),
                        summary.ownerSlug(),
                        summary.ownerName(),
                        summary.organizationSlug(),
                        summary.organizationName()
                );
            }

            return summary;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getPublicProject(String slug) {
        Project project = projectRepository.findBySlugAndStatus(slug, ProjectStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        return buildResponse(project);
    }

    @Override
    public List<ProjectSummaryDTO> getMyProjects() {
        User user = currentUser.get();

        var projects = projectRepository.findByUser(user);

        return projects.stream().map(p -> {
            ProjectSummaryDTO summary = projectMapper.toSummaryDTO(p);
            var mediaList = mediaRepository.findByProjectOrderByCreatedAtAsc(p);
            if (!mediaList.isEmpty()) {
                summary = new ProjectSummaryDTO(
                        p.getSlug(),
                        p.getName(),
                        p.getDescription(),
                        mediaMapper.toMediaResponseDTO(mediaList.get(0)),
                        summary.ownerSlug(),
                        summary.ownerName(),
                        summary.organizationSlug(),
                        summary.organizationName()
                );
            }
            return summary;
        }).toList();
    }

    @Override
    public ProjectResponseDTO update(Long projectId, ProjectRequestDTO request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        User user = currentUser.get();

        requireProjectAccessForModify(project, user);

        // Do not allow changing owner/org after creation
        project.setName(request.name());
        project.setDescription(request.description());
        project.setWebsite(request.website());
        project.setGithub(request.github());

        // slug must not change on update per rules

        Project updated = projectRepository.save(project);

        return buildResponse(updated);
    }

    @Override
    public void delete(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        User user = currentUser.get();

        requireProjectAccessForModify(project, user);

        // For MVP we delete the entity if no deactivation mechanism exists
        projectRepository.delete(project);
    }

    @Override
    public ProjectResponseDTO publish(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        User user = currentUser.get();
        requireProjectAccessForModify(project, user);

        project.setStatus(ProjectStatus.PUBLISHED);
        Project saved = projectRepository.save(project);

        return buildResponse(saved);
    }

    @Override
    public ProjectResponseDTO draft(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        User user = currentUser.get();
        requireProjectAccessForModify(project, user);

        project.setStatus(ProjectStatus.DRAFT);
        Project saved = projectRepository.save(project);

        return buildResponse(saved);
    }

    @Override
    public MediaResponseDTO uploadMedia(Long projectId, MultipartFile file) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        User user = currentUser.get();
        requireProjectAccessForModify(project, user);

        Media media = mediaService.upload(file, MediaFolder.PROJECTS, MediaType.PROJECT);

        // Link media to project and save
        media.setProject(project);
        Media saved = mediaRepository.save(media);

        return mediaMapper.toMediaResponseDTO(saved);
    }

    @Override
    public void deleteMedia(Long projectId, Long mediaId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media introuvable"));

        if (media.getProject() == null || !media.getProject().getId().equals(project.getId())) {
            throw new BadRequestException("Ce média n'appartient pas à ce projet");
        }

        User user = currentUser.get();
        requireProjectAccessForModify(project, user);

        // delete physical file and DB record via MediaService
        mediaService.delete(mediaId);
    }

    private ProjectResponseDTO buildResponse(Project project) {
        ProjectResponseDTO dto = projectMapper.toResponseDTO(project);

        var mediaList = mediaRepository.findByProjectOrderByCreatedAtAsc(project);

        dto = new ProjectResponseDTO(
                dto.slug(),
                dto.name(),
                dto.description(),
                dto.status(),
                dto.website(),
                dto.github(),
                mediaList.stream().map(mediaMapper::toMediaResponseDTO).toList(),
                dto.ownerSlug(),
                dto.ownerName(),
                dto.organizationSlug(),
                dto.organizationName(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );

        return dto;
    }

    private void requireProjectAccessForModify(Project project, User user) {
        if (project.getUser() != null) {
            if (!project.getUser().getId().equals(user.getId())) {
                throw new BadRequestException("Vous n'avez pas les droits pour gérer ce projet");
            }
            return;
        }

        if (project.getOrganization() != null) {
            Organization org = project.getOrganization();
            OrganizationMember member = organizationMemberRepository.findByOrganizationAndUser(org, user)
                    .orElseThrow(() -> new BadRequestException("Vous n'êtes pas membre de cette organisation"));

            if (!(member.getRole() == OrganizationMemberRole.OWNER || member.getRole() == OrganizationMemberRole.ADMIN)) {
                throw new BadRequestException("Seuls le propriétaire et les administrateurs peuvent gérer ce projet");
            }
            return;
        }

        throw new BadRequestException("Projet sans propriétaire");
    }

    private String generateSlug(String name) {
        String baseSlug = name.toLowerCase().trim().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");

        String slug = baseSlug;
        int counter = 1;

        while (projectRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }
}
