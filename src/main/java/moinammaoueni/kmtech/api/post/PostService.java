package moinammaoueni.kmtech.api.post;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostRequestDTO;
import moinammaoueni.kmtech.api.post.dto.PostResponse;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryPublic;
import moinammaoueni.kmtech.api.post.dto.PostUpdate;

public interface PostService {

    PostResponseDTO create(PostRequestDTO request);

    List<PostSummaryPublic> findAll();

    PostResponseDTO findPublicBySlug(String slug);

    List<PostSummaryDTO> findMyPosts();

    List<PostSummaryPublic> findUserPosts(String userSlug);

    List<PostSummaryPublic> findOrganizationPosts(String organizationSlug);
    
    PostResponse findById(Long postId);

    PostResponseDTO update(Long postId, PostUpdate request);

    void delete(Long postId);

    List<MediaResponseDTO> uploadMedia(Long postId, List<MultipartFile> files);
    
    CommentResponseDTO commentPost(String slug, CommentRequestDTO dto);
    
    

    void deleteMedia(Long postId, Long mediaId);
}
