package com.aftourism.common.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Abstracts file storage to allow swapping local and OSS implementations.
 */
public interface FileStorageService {

    String store(MultipartFile file);
}
