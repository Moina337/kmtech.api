

package moinammaoueni.kmtech.api.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.PostUpdate;
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
import moinammaoueni.kmtech.api.media.storage.FileStorageService;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;
import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.organization.OrganizationRepository;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMember;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRepository;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRole;
import moinammaoueni.kmtech.api.post.dto.PostRequestDTO;
import moinammaoueni.kmtech.api.post.dto.PostResponse;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryPublic;
import moinammaoueni.kmtech.api.user.User;
import moinammaoueni.kmtech.api.user.UserRepository;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final MediaRepository mediaRepository;
    private final MediaService mediaService;
    private final CurrentUser currentUser;
    private final PostMapper postMapper;
    private final FileStorageService fileStorageService;
    private final MediaMapper mediaMapper;

    @Override
    @Transactional
    public PostResponseDTO create(PostRequestDTO request) {

        User user = currentUser.get();

        Organization organization = null;

        if (request.organizationId() != null) {
            organization = organizationRepository.findById(request.organizationId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Organisation introuvable"));

            requireOrganizationAccess(organization, user);
        }

        String slug = generateSlug();

        Post post = Post.builder()
                .slug(slug)
                .content(normalizeContent(request.content()))
                .user(organization == null ? user : null)
                .organization(organization)
                .build();

        post = postRepository.save(post);

        List<Media> media = mediaRepository.findByPost(post);

        return postMapper.toResponseDTO(post, media);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryPublic> findAll() {

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        return posts.stream()
                .map(post -> {
                    // On récupère simplement la liste des médias associés au post
                    List<Media> medias = mediaRepository.findByPost(post);
                   
                    // On passe le post et sa liste de médias au mapper
                    return postMapper.toPostSummaryPublic(post, medias);
                })
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public PostResponseDTO findPublicBySlug(String slug) {

        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post introuvable"));

        List<Media> media = mediaRepository.findByPost(post);

        return postMapper.toResponseDTO(post, media);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> findMyPosts() {

        User user = currentUser.get();

        List<Post> posts = postRepository.findByUser(user);

        return posts.stream()
                .map(post -> {
                    Media cover = mediaRepository
                            .findByPostOrderByCreatedAtDesc(post)
                            .stream()
                            .findFirst()
                            .orElse(null);

                    return postMapper.toSummaryDTO(post, cover);
                })
                .toList();
    }
    
    @Override
	public PostResponse findById(Long postId) {
		
		User user = currentUser.get();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post introuvable"));

        requirePostAccessForModify(post, user);
        
        List<Media> medias = mediaRepository.findByPost(post);
		
		return postMapper.toResponse(post, medias);
	}

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> findUserPosts(String userSlug) {

        User user = userRepository.findBySlug(userSlug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Utilisateur introuvable"));

        List<Post> posts = postRepository.findByUser(user);

        return posts.stream()
                .map(post -> {
                    Media cover = mediaRepository
                            .findByPostOrderByCreatedAtDesc(post)
                            .stream()
                            .findFirst()
                            .orElse(null);

                    return postMapper.toSummaryDTO(post, cover);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryPublic> findOrganizationPosts(String organizationSlug) {

        Organization organization = organizationRepository.findBySlug(organizationSlug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organisation introuvable"));

        List<Post> posts = postRepository.findByOrganization(organization);

        return posts.stream()
                .map(post -> {
                    // On récupère simplement la liste des médias associés au post
                    List<Media> medias = mediaRepository.findByPost(post);
                   
                    // On passe le post et sa liste de médias au mapper
                    return postMapper.toPostSummaryPublic(post, medias);
                })
                .toList();
    }

    @Override
    @Transactional
    public PostResponseDTO update(Long postId, PostUpdate request) {

        User user = currentUser.get();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post introuvable"));

        requirePostAccessForModify(post, user);

        String content = normalizeContent(request.toString());

        /*
         * Le propriétaire du post ne peut pas être changé pendant un update.
         * Un post personnel reste personnel et un post d'organisation reste
         * attaché à son organisation.
         */
        post.setContent(content);

        post = postRepository.save(post);

        List<Media> media = mediaRepository.findByPost(post);

        validatePostContentOrMedia(post, media);

       
        return postMapper.toResponseDTO(post, media);
    }


    @Override
    @Transactional
    public void delete(Long postId) {

        User user = currentUser.get();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post introuvable"));

        requirePostAccessForModify(post, user);

        List<Media> medias = mediaRepository.findByPost(post);

        for (Media media : medias) {
            deleteMediaFile(media);
            mediaRepository.delete(media);
        }

        postRepository.delete(post);
    }

    @Override
    @Transactional
    public List<MediaResponseDTO> uploadMedia(Long postId, List<MultipartFile> files) {

        User user = currentUser.get();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post introuvable"));

        requirePostAccessForModify(post, user);

        // 1. Créer une liste pour stocker les médias enregistrés
        List<Media> savedMedias = new ArrayList<>();

        // 2. Boucler sur chaque fichier envoyé
        for (MultipartFile file : files) {
            // Upload du fichier via votre service média
            Media media = mediaService.upload(
                    file,
                    MediaFolder.POSTS,
                    MediaType.POST
            );
            
            // Liaison avec le post actuel
            media.setPost(post);
            
            // Sauvegarde en base de données et ajout à notre liste
            savedMedias.add(mediaRepository.save(media));
        }

        // 3. Validation métier (si nécessaire pour vérifier la cohérence du post)
        validatePostContentOrMedia(post, savedMedias);

        // 4. Conversion de toute la liste en DTOs de réponse
        return savedMedias.stream()
                .map(mediaMapper::toMediaResponseDTO)
                .toList();
    }


    @Override
    @Transactional
    public void deleteMedia(Long postId, Long mediaId) {

        User user = currentUser.get();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post introuvable"));

        requirePostAccessForModify(post, user);

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Media introuvable"));

        if (media.getPost() == null
                || !media.getPost().getId().equals(postId)) {
            throw new BadRequestException(
                    "Le media n'appartient pas à ce post"
            );
        }

        deleteMediaFile(media);
        mediaRepository.delete(media);
    }

    /**
     * Vérifie que l'utilisateur connecté peut modifier ou supprimer le post.
     *
     * Post personnel :
     * seul son propriétaire peut agir.
     *
     * Post d'organisation :
     * seul OWNER ou ADMIN actuel de l'organisation peut agir.
     */
    private void requirePostAccessForModify(
            Post post,
            User user) {

        if (post.getUser() != null) {

            if (!post.getUser().getId().equals(user.getId())) {
                throw new BadRequestException(
                        "Vous n'avez pas la permission de modifier ce post"
                );
            }

            return;
        }

        if (post.getOrganization() != null) {
            requireOrganizationAccess(post.getOrganization(), user);
            return;
        }

        throw new BadRequestException(
                "Le post est dans un état invalide"
        );
    }

    /**
     * Vérifie que l'utilisateur est OWNER ou ADMIN de l'organisation.
     */
    private void requireOrganizationAccess(
            Organization organization,
            User user) {

        OrganizationMember member =
                organizationMemberRepository
                        .findByOrganizationAndUser(organization, user)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Vous n'êtes pas membre de cette organisation"
                                )
                        );

        if (member.getRole() != OrganizationMemberRole.OWNER
                && member.getRole() != OrganizationMemberRole.ADMIN) {

            throw new BadRequestException(
                    "Vous n'avez pas la permission d'effectuer cette action dans cette organisation"
            );
        }
    }

    /**
     * Vérifie qu'un post contient du texte ou au moins un média.
     *
     * Cette validation est utile après l'ajout/suppression d'un média
     * et lors des mises à jour.
     */
    private void validatePostContentOrMedia(
            Post post,
            List<Media> media) {

        boolean hasContent =
                post.getContent() != null
                        && !post.getContent().isBlank();

        boolean hasMedia =
                media != null && !media.isEmpty();

        if (!hasContent && !hasMedia) {
            throw new BadRequestException(
                    "Le post doit contenir du texte ou au moins un média"
            );
        }
    }

    /**
     * Supprime le fichier physique correspondant au média.
     */
    private void deleteMediaFile(Media media) {

        MediaFolder folder =
                MediaFolder.valueOf(
                        media.getFolder().toUpperCase(Locale.ROOT)
                );

        fileStorageService.delete(
                media.getStoredName(),
                folder
        );
    }

    /**
     * Nettoie le contenu avant sauvegarde.
     */
    private String normalizeContent(String content) {

        if (content == null) {
            return null;
        }

        String normalized = content.trim();

        return normalized.isBlank() ? null : normalized;
    }

    /**
     
     *
     * Le slug reste stable après la création du post.
     */
    private String generateSlug() {

        String slug;

        do {
            slug = "post-" + UUID.randomUUID()
                    .toString()
                    .substring(0, 8);
        } while (postRepository.existsBySlug(slug));

        return slug;
    }
	
}

