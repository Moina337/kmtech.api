package moinammaoueni.kmtech.api.post;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryPublic;

@RestController
@RequestMapping("/api/public/posts")
@RequiredArgsConstructor
public class PostPublicController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostSummaryPublic>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostResponseDTO> findPublicBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.findPublicBySlug(slug));
    }

    @GetMapping("/users/{userSlug}")
    public ResponseEntity<List<PostSummaryPublic>> findUserPosts(@PathVariable String userSlug) {
        return ResponseEntity.ok(postService.findUserPosts(userSlug));
    }

    @GetMapping("/organizations/{organizationSlug}")
    public ResponseEntity<List<PostSummaryPublic>> findOrganizationPosts(@PathVariable String organizationSlug) {
        return ResponseEntity.ok(postService.findOrganizationPosts(organizationSlug));
    }
}