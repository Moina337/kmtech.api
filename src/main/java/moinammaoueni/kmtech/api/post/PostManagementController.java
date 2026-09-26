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

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostRequestDTO;
import moinammaoueni.kmtech.api.post.dto.PostResponse;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryDTO;
import moinammaoueni.kmtech.api.post.dto.PostUpdate;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostManagementController {

    private final PostService postService;

    @GetMapping("/me")
    public ResponseEntity<List<PostSummaryDTO>> findMyPosts() {
        return ResponseEntity.ok(postService.findMyPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody PostRequestDTO request) {
        PostResponseDTO response = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long postId, @RequestBody PostUpdate request) {
        return ResponseEntity.ok(postService.update(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{postId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MediaResponseDTO>> uploadMedia(
            @PathVariable Long postId,
            @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(postService.uploadMedia(postId, files));
    }

    @DeleteMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long postId, @PathVariable Long mediaId) {
        postService.deleteMedia(postId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{slug}/comment")
    public ResponseEntity<CommentResponseDTO> commentPost(@PathVariable String slug, @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.ok(postService.commentPost(slug, dto));
    }
}