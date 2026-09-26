package moinammaoueni.kmtech.api.post;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostRequestDTO;
import moinammaoueni.kmtech.api.post.dto.PostResponse;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryDTO;
import moinammaoueni.kmtech.api.post.dto.PostUpdate;

@Tag(name = "Posts (gestion)", description = "Création, modification et suppression de ses propres posts — authentification requise")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostManagementController {

    private final PostService postService;

    @Operation(summary = "Lister mes propres posts")
    @ApiResponse(responseCode = "200", description = "Liste de mes posts récupérée avec succès")
    @GetMapping("/me")
    public ResponseEntity<List<PostSummaryDTO>> findMyPosts() {
        return ResponseEntity.ok(postService.findMyPosts());
    }

    @Operation(summary = "Récupérer un de mes posts par son id, en vue gestion")
    @ApiResponse(responseCode = "200", description = "Post trouvé")
    @ApiResponse(responseCode = "403", description = "Ce post ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun post ne correspond à cet id")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }

    @Operation(summary = "Créer un nouveau post")
    @ApiResponse(responseCode = "201", description = "Post créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données de post invalides")
    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@Valid @RequestBody PostRequestDTO request) {
        PostResponseDTO response = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Modifier un de mes posts")
    @ApiResponse(responseCode = "200", description = "Post modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données de modification invalides")
    @ApiResponse(responseCode = "403", description = "Ce post ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun post ne correspond à cet id")
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long postId, @Valid @RequestBody PostUpdate request) {
        return ResponseEntity.ok(postService.update(postId, request));
    }

    @Operation(summary = "Supprimer un de mes posts")
    @ApiResponse(responseCode = "204", description = "Post supprimé avec succès")
    @ApiResponse(responseCode = "403", description = "Ce post ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Aucun post ne correspond à cet id")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ajouter un ou plusieurs médias à un post", description = "Envoi en multipart/form-data")
    @ApiResponse(responseCode = "200", description = "Médias ajoutés avec succès")
    @ApiResponse(responseCode = "400", description = "Un ou plusieurs fichiers invalides")
    @ApiResponse(responseCode = "403", description = "Ce post ne vous appartient pas")
    @PostMapping(value = "/{postId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MediaResponseDTO>> uploadMedia(
            @PathVariable Long postId,
            @Parameter(description = "Un ou plusieurs fichiers image/vidéo à associer au post")
            @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(postService.uploadMedia(postId, files));
    }

    @Operation(summary = "Supprimer un média d'un post")
    @ApiResponse(responseCode = "204", description = "Média supprimé avec succès")
    @ApiResponse(responseCode = "403", description = "Ce post ne vous appartient pas")
    @ApiResponse(responseCode = "404", description = "Média introuvable")
    @DeleteMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long postId, @PathVariable Long mediaId) {
        postService.deleteMedia(postId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ajouter un commentaire à un post")
    @ApiResponse(responseCode = "200", description = "Commentaire ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Contenu du commentaire invalide")
    @ApiResponse(responseCode = "404", description = "Aucun post ne correspond à ce slug")
    @PostMapping("/{slug}/comment")
    public ResponseEntity<CommentResponseDTO> commentPost(@PathVariable String slug, @Valid @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.ok(postService.commentPost(slug, dto));
    }
}