package moinammaoueni.kmtech.api.media.storage;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {

    /**
     * Exemple : ./uploads
     */
    private String directory;

    /**
     * Exemple : 10MB
     */
    private DataSize maxFileSize;

    /**
     * Exemple :
     * image/jpeg
     * image/png
     * image/webp
     */
    private List<String> allowedFileTypes;

}