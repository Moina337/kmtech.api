package moinammaoueni.kmtech.api.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;

@Tag(name = "Commentaires (gestion)", description = "Modification et suppression de ses propres commentaires — authentification requise")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Modifier un de mes commentaires")
    @ApiResponse(responseCode = "200", description = "Commentaire modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Contenu invalide")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas l'auteur de ce commentaire")
    @ApiResponse(responseCode = "404", description = "Commentaire introuvable")
    @PutMapping("/{id}")
    // TODO: ajouter la vérification d'auteur ici — je ne connais pas
    // le nom de ton bean de sécurité, dis-le moi pour le brancher correctement
    public ResponseEntity<CommentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDTO request) {
        return ResponseEntity.ok(commentService.update(id, request));
    }

    @Operation(summary = "Supprimer un de mes commentaires")
    @ApiResponse(responseCode = "204", description = "Commentaire supprimé avec succès")
    @ApiResponse(responseCode = "403", description = "Vous n'êtes pas l'auteur de ce commentaire")
    @ApiResponse(responseCode = "404", description = "Commentaire introuvable")
    @DeleteMapping("/{id}")
    // TODO: même chose ici
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}