package moinammaoueni.kmtech.api.media.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.common.exception.InvalidFileException;



@Component
@RequiredArgsConstructor
public class FileValidator {

    private final UploadProperties uploadProperties;

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File must not be empty.");
        }

        if (file.getOriginalFilename() == null
                || file.getOriginalFilename().isBlank()) {
            throw new InvalidFileException("Invalid file name.");
        }

        String contentType = file.getContentType();

        if (contentType == null
                || !uploadProperties.getAllowedFileTypes().contains(contentType)) {
            throw new InvalidFileException("Unsupported file type.");
        }

        if (file.getSize() > uploadProperties.getMaxFileSize().toBytes()) {
            throw new InvalidFileException(
                    "Maximum allowed file size is "
                            + uploadProperties.getMaxFileSize().toMegabytes()
                            + " MB."
            );
        }
    }

}