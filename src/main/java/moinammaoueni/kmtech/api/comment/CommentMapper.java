package moinammaoueni.kmtech.api.comment;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.user.UserMapper;


@Mapper(
	    componentModel = "spring",
	    uses = { MediaMapper.class, UserMapper.class }
	)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentRequestDTO request);

    @Mapping(target = "author", source = "author")
    CommentResponseDTO toResponse(Comment comment);

    
}
