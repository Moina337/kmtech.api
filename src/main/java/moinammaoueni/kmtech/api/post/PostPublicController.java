package moinammaoueni.kmtech.api.post;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryPublic;

@Tag(name = "Posts (public)", description = "Fil d'actualité et articles, consultation libre")
@RestController
@RequestMapping("/api/public/posts")
@RequiredArgsConstructor
public class PostPublicController {

    private final PostService postService;

    @Operation(summary = "Lister tous les posts publics")
    @ApiResponse(responseCode = "200", description = "Liste des posts récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<PostSummaryPublic>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @Operation(summary = "Récupérer un post par son slug")
    @ApiResponse(responseCode = "200", description = "Post trouvé")
    @ApiResponse(responseCode = "404", description = "Aucun post ne correspond à ce slug")
    @GetMapping("/{slug}")
    public ResponseEntity<PostResponseDTO> findPublicBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.findPublicBySlug(slug));
    }

    @Operation(summary = "Lister les posts publics d'un utilisateur")
    @ApiResponse(responseCode = "200", description = "Liste des posts de l'utilisateur récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Aucun utilisateur ne correspond à ce slug")
    @GetMapping("/users/{userSlug}")
    public ResponseEntity<List<PostSummaryPublic>> findUserPosts(@PathVariable String userSlug) {
        return ResponseEntity.ok(postService.findUserPosts(userSlug));
    }

    @Operation(summary = "Lister les posts publics d'une organisation")
    @ApiResponse(responseCode = "200", description = "Liste des posts de l'organisation récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Aucune organisation ne correspond à ce slug")
    @GetMapping("/organizations/{organizationSlug}")
    public ResponseEntity<List<PostSummaryPublic>> findOrganizationPosts(@PathVariable String organizationSlug) {
        return ResponseEntity.ok(postService.findOrganizationPosts(organizationSlug));
    }
}