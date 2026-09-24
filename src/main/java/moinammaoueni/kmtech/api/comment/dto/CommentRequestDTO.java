package moinammaoueni.kmtech.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    @Size(max = 1000, message = "Le commentaire ne peut pas dépasser 1000 caractères")
    private String content;
}