package moinammaoueni.kmtech.api.media;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.media.storage.FileStorageService;
import moinammaoueni.kmtech.api.media.storage.FileValidator;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;
import moinammaoueni.kmtech.api.media.storage.StoredFile;


@Service
@RequiredArgsConstructor
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    private final FileStorageService fileStorageService;
    
    private final FileValidator fileValidator;
    
    @Override
    public Media upload(
            MultipartFile file,
            MediaFolder folder, MediaType mediaType) {

    	fileValidator.validate(file);

    	StoredFile storedFile =
    	        fileStorageService.upload(file, folder);

        Media media = Media.builder()
                .originalName(storedFile.originalFilename())
                .storedName(storedFile.storedFilename())
                .folder(folder.getValue())
                .size(storedFile.size())
                .mimeType(storedFile.contentType())
                .url(storedFile.url())
                .type(mediaType)
                .build();

        Media savedMedia = mediaRepository.save(media);

        return savedMedia;
    }
    
    @Override
    @Transactional(readOnly = true)
    public MediaResponseDTO findById(Long id) {

        Media media = mediaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Media not found"));

        return mediaMapper.toMediaResponseDTO(media);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MediaResponseDTO> findAll() {

        return mediaRepository.findAll()
                .stream()
                .map(mediaMapper::toMediaResponseDTO)
                .toList();
    }
    
    
    
    @Override
    public void delete(Long id) {

    	Media media = mediaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Media not found"));

        fileStorageService.delete(
                media.getStoredName(),
                MediaFolder.valueOf(
                        media.getFolder().toUpperCase()));

        mediaRepository.delete(media);
     

    }

    @Override
    @Transactional
    public Media replace(Media oldMedia,
                         MultipartFile newFile,
                         MediaFolder folder, MediaType mediaType) {
  
    	// Aucun nouveau fichier → on garde l'ancien
        if (newFile == null || newFile.isEmpty()) {
            return oldMedia;
        }
    	
    	fileValidator.validate(newFile);
    	
    	// UPLOAD DU NOUVEAU FICHIER
    	Media newMedia = upload(newFile, folder, mediaType);
    	
    	// Une fois le nouveau média créé, on supprime l'ancien
    	if (oldMedia != null) {

            fileStorageService.delete(
                    oldMedia.getStoredName(),
                    folder
            );

            mediaRepository.delete(oldMedia);
        }
       

        return newMedia;
    }
    
    

}