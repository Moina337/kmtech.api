package moinammaoueni.kmtech.api.media;

import org.mapstruct.Mapper;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaUploadResponseDTO;



@Mapper(componentModel = "spring")
public interface MediaMapper {

    MediaResponseDTO toMediaResponseDTO(Media media);

    MediaUploadResponseDTO toMediaUploadResponseDTO(Media media);

}