package com.aftourism.common.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Holds storage configuration for uploaded files.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    /**
     * Directory for storing uploaded files.
     */
    private String uploadDir = "uploads";

    /**
     * Whether OSS support is enabled; reserved for future usage.
     */
    private boolean ossEnable = false;
}
