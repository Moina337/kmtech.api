package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.comment.Comment;
import moinammaoueni.kmtech.api.comment.CommentMapper;
import moinammaoueni.kmtech.api.comment.CommentRepository;
import moinammaoueni.kmtech.api.comment.CommentService;
import moinammaoueni.kmtech.api.comment.CommentType;
import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
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
import moinammaoueni.kmtech.api.project.dto.ProjectManagementResponse;

import moinammaoueni.kmtech.api.project.dto.ProjectRequestDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryManagement;
import moinammaoueni.kmtech.api.project.dto.ProjectUpdateRequest;
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
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final CommentMapper commentMapper;  
  

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

        return projectMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getPublishedProjects() {
    	
        List<Project> projects = projectRepository.findByStatus(ProjectStatus.PUBLISHED);

        return projects.stream()
        		.map(projectMapper::toSummaryDTO)
        		.toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getPublicProject(String slug) {
        Project project = projectRepository.findBySlugAndStatus(slug, ProjectStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        return projectMapper.toResponseDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getPublicUserProjects(String userSlug) {
        User user = userRepository.findBySlug(userSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        List<Project> projects = projectRepository.findByUserAndStatus(user, ProjectStatus.PUBLISHED);

        return projects.stream()
        		.map(projectMapper::toSummaryDTO)
        		.toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getPublicOrganizationProjects(String organizationSlug) {
        Organization organization = organizationRepository.findBySlug(organizationSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        List<Project> projects = projectRepository.findByOrganizationAndStatus(
                organization,
                ProjectStatus.PUBLISHED
        );

        return projects.stream()
        		.map(projectMapper::toSummaryDTO)
        		.toList();
    }

    @Override
    public List<ProjectSummaryManagement> getMyProjects() {
        User user = currentUser.get();

        var projects = projectRepository.findByUser(user);

        return projects.stream()
        		.map(projectMapper::toSummaryManagementDTO)
        		.toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryManagement> getOrganizationProjects(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable"));

        User user = currentUser.get();

        OrganizationMember member = organizationMemberRepository.findByOrganizationAndUser(organization, user)
                .orElseThrow(() -> new BadRequestException("Vous n'êtes pas membre de cette organisation"));

        if (!(member.getRole() == OrganizationMemberRole.OWNER || member.getRole() == OrganizationMemberRole.ADMIN)) {
            throw new BadRequestException("Seuls le propriétaire et les administrateurs peuvent voir les projets de cette organisation");
        }

        return projectRepository.findByOrganization(organization)
                .stream()
                .map(projectMapper::toSummaryManagementDTO)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectManagementResponse getMyProjectById(Long projectId) {

        User current = currentUser.get();

        Project project = projectRepository
                .findByIdAndUser(projectId, current)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Projet personnel introuvable"
                        )
                );

        return projectMapper.toManagementProject(project);
    }

  	@Override
  	public ProjectManagementResponse getOrganizationProjectById(Long organizationId, Long projectId) {

  		Organization organization = organizationRepository.findById(organizationId)
  	            .orElseThrow(() ->
  	                    new ResourceNotFoundException(
  	                            "Organisation introuvable"
  	                    )
  	            );
  		
  		Project project = projectRepository
  	            .findByIdAndOrganization(projectId, organization)
  	            .orElseThrow(() ->
  	                    new ResourceNotFoundException(
  	                            "Projet introuvable dans cette organisation"
  	                    )
  	            );

  	    User user = currentUser.get();
  	    
  		requireProjectAccessForModify(project, user);
  		
  		return projectMapper.toManagementProject(project);
  		
  	}

    @Override
    public ProjectResponseDTO update(Long projectId, ProjectUpdateRequest request) {
    	
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

        return projectMapper.toResponseDTO(updated);
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

        return projectMapper.toResponseDTO(saved);
    }

    @Override
    public ProjectResponseDTO draft(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable"));

        User user = currentUser.get();
        requireProjectAccessForModify(project, user);

        project.setStatus(ProjectStatus.DRAFT);
        Project saved = projectRepository.save(project);

        return projectMapper.toResponseDTO(saved);
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

    @Override
    public CommentResponseDTO ajouterCommentaire(
            String slug,
            CommentRequestDTO request) {

        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projet introuvable"));

        Comment comment = commentService.prepare(request);

        comment.setProject(project);
        comment.setType(CommentType.PROJECT);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponse(savedComment);
    }

	@Override
	public List<CommentResponseDTO> getProjectComment(Long projectId) {
      
		Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projet introuvable"));
		
		return commentRepository.findByProjectOrderByCreatedAtDesc(project)
				.stream()
				.map(commentMapper::toResponse)
				.toList();
		
	
	}
    
    
}