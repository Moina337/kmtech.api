package moinammaoueni.kmtech.api.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdate(

		@NotBlank(message = "Le contenu du post ne peut pas être vide")
		@Size(max = 1000, message = "Content must not exceed 1000 characters")
		String content
		
		) {}
