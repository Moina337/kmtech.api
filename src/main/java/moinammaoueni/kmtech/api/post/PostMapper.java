package moinammaoueni.kmtech.api.post;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.comment.Comment;
import moinammaoueni.kmtech.api.comment.CommentMapper;
import moinammaoueni.kmtech.api.media.Media;
import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.organization.OrganizationMapper;
import moinammaoueni.kmtech.api.post.dto.PostResponse;
import moinammaoueni.kmtech.api.post.dto.PostResponseDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryDTO;
import moinammaoueni.kmtech.api.post.dto.PostSummaryPublic;
import moinammaoueni.kmtech.api.user.UserMapper;

@Mapper(componentModel = "spring", 
uses = { MediaMapper.class, UserMapper.class, OrganizationMapper.class,
		CommentMapper.class, }

)
public interface PostMapper {

	@Mapping(target = "create", source = "post.createdAt")
	PostResponseDTO toResponseDTO(Post post, List<Media> medias, List<Comment> comments);
	
	@Mapping(target = "create", source = "post.createdAt")
	PostResponse toResponse(Post post, List<Media> media);

	@Mapping(target = "id", source = "post.id")
	@Mapping(target = "create", source = "post.createdAt")
	PostSummaryDTO toSummaryDTO(Post post, Media cover);

	@Mapping(target = "create", source = "post.createdAt")
	PostSummaryPublic toPostSummaryPublic(Post post, List<Media> medias);
}