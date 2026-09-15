package moinammaoueni.kmtech.api.organizationmember.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicOrganisationMembre {
	
	private String userSlug;
    private String userName;
    MediaResponseDTO media;
   
}
