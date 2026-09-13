package moinammaoueni.kmtech.api.media;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;

public interface MediaService {

	MediaResponseDTO upload(MultipartFile file, MediaFolder folder, MediaType mediaType);

	MediaResponseDTO findById(Long id);

	List<MediaResponseDTO> findAll();

	Media replace(Media oldMedia, MultipartFile newFile, MediaFolder folder);

	void delete(Long id);

}