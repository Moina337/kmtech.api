package moinammaoueni.kmtech.api.media.storage;

public record StoredFile(

        String originalFilename,

        String storedFilename,

        String contentType,

        long size,

        String url

) {
}