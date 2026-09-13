package moinammaoueni.kmtech.api.media;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

   
    
    // Affficher la liste des médias
    @GetMapping
    public ResponseEntity<List<MediaResponseDTO>> getAllMedia() {
		List<MediaResponseDTO> mediaList = mediaService.findAll();
		return ResponseEntity.ok(mediaList);
	}
}
