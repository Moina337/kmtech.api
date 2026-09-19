package moinammaoueni.kmtech.api.post.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryDTO;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

@Getter
@Setter
@Builder
public class PostResponse {

    private Long id;

    private String content;
    private String media;

    private UserSummaryDTO author;
    private OrganizationSummaryDTO organization;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}