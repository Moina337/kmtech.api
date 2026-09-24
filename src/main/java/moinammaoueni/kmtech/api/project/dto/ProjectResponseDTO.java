package moinammaoueni.kmtech.api.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryDTO;
import moinammaoueni.kmtech.api.project.ProjectStatus;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

public record ProjectResponseDTO(
    String slug,
    String name,
    String description,
    ProjectStatus status,
    String website,
    String github,
    List<MediaResponseDTO> media,
    UserSummaryDTO user,
    OrganizationSummaryDTO organization,
    List<CommentResponseDTO> comments,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
