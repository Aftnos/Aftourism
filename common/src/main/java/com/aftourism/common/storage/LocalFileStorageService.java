package com.aftourism.common.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Stores files on the local filesystem.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "oss.enable", havingValue = "false", matchIfMissing = true)
public class LocalFileStorageService implements FileStorageService {

    private final FileStorageProperties properties;

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        if (extension != null && !extension.isBlank()) {
            fileName += "." + extension;
        }
        Path uploadPath = Paths.get(properties.getUploadDir());
        try {
            Files.createDirectories(uploadPath);
            File destination = uploadPath.resolve(fileName).toFile();
            file.transferTo(destination);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new IllegalStateException("Failed to store file", e);
        }
    }
}
