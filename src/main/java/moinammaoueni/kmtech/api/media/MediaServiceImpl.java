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
    public MediaResponseDTO upload(
            MultipartFile file,
            MediaFolder folder, MediaType mediaType) {

    	fileValidator.validate(file);

    	StoredFile storedFile =
    	        fileStorageService.upload(file, folder);

        Media media = Media.builder()
                .originalName(storedFile.originalFilename())
                .size(storedFile.size())
                .mimeType(storedFile.contentType())
                .url(storedFile.url())
                .type(mediaType)
                .build();

        Media savedMedia = mediaRepository.save(media);

        return mediaMapper.toMediaResponseDTO(savedMedia);
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

     

    }

    @Override
    @Transactional
    public Media replace(Media oldMedia,
                         MultipartFile newFile,
                         MediaFolder folder) {

      
       

        return null;
    }
    
    

}