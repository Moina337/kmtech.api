package moinammaoueni.kmtech.api.comment.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

@Getter
@Setter
@Builder
public class CommentResponseDTO {

    private Long id;

    private String content;

    private UserSummaryDTO author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}