package moinammaoueni.kmtech.api.media.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.upload.directory}")
    private String uploadDirectory;

    @Override
    public StoredFile upload(MultipartFile file, MediaFolder folder) {

        

        try {

            Path directory = Path.of(uploadDirectory, folder.getValue());

            Files.createDirectories(directory);

            String extension = getExtension(file.getOriginalFilename());

            String storedFilename = UUID.randomUUID() + extension;

            Path destination = directory.resolve(storedFilename);

            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String url = "/uploads/" + folder.getValue() + "/" + storedFilename;

            return new StoredFile(
                    file.getOriginalFilename(),
                    storedFilename,
                    file.getContentType(),
                    file.getSize(),
                    url
            );

        } catch (IOException exception) {

            throw new RuntimeException("Unable to upload file.", exception);

        }

    }

    @Override
    public void delete(String fileName, MediaFolder folder) {

        try {

            Path path = Path.of(uploadDirectory, folder.getValue(), fileName);

            Files.deleteIfExists(path);

        } catch (IOException exception) {

            throw new RuntimeException("Unable to delete file.", exception);

        }

    }
    
    @Override
    public String getFileUrl(String fileName, MediaFolder folder) {

        return "/uploads/" + folder.getValue() + "/" + fileName;

    }

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));

    }

}