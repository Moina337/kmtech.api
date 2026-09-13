package moinammaoueni.kmtech.api.media.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	  StoredFile upload(MultipartFile file, MediaFolder folder);

    void delete(String fileName, MediaFolder folder);

    String getFileUrl(String fileName, MediaFolder folder);

}