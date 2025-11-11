package aftnos.aftourismserver.common.storage.impl;

import aftnos.aftourismserver.common.config.FileStorageProperties;
import aftnos.aftourismserver.common.storage.FileStorageService;
import aftnos.aftourismserver.common.storage.StoredFileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 本地文件存储实现，将文件保存在服务器磁盘
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "file.storage-type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageServiceImpl implements FileStorageService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final FileStorageProperties fileStorageProperties;

    @Override
    public StoredFileInfo store(MultipartFile file, String bizTag) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        // 规范化业务目录名称，默认归档到 common
        String safeBizTag = normalizeBizTag(bizTag);
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        validateExtension(fileExtension);

        String newFileName = buildNewFileName(fileExtension);
        String datePath = LocalDate.now().format(DATE_FORMATTER);
        String relativePath = safeBizTag + "/" + datePath + "/" + newFileName;
        Path basePath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        Path targetPath = basePath.resolve(relativePath).normalize();
        if (!targetPath.startsWith(basePath)) {
            throw new IllegalArgumentException("业务标签非法，路径越界");
        }
        Path targetDir = targetPath.getParent();

        try {
            if (targetDir != null) {
                Files.createDirectories(targetDir);
            }
            file.transferTo(targetPath);
            log.info("【文件上传】文件已保存，目标路径={}", targetPath);
        } catch (IOException e) {
            log.error("【文件上传】文件保存失败", e);
            throw new IllegalStateException("文件保存失败，请稍后重试", e);
        }

        return new StoredFileInfo(buildAccessUrl(relativePath), relativePath);
    }

    /**
     * 构建对外访问地址
     */
    private String buildAccessUrl(String relativePath) {
        String baseUrl = fileStorageProperties.getBaseUrl();
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return normalizedBaseUrl + "/" + relativePath.replace("\\", "/");
    }

    /**
     * 根据原始文件扩展名生成唯一文件名
     */
    private String buildNewFileName(String extension) {
        return UUID.randomUUID().toString().replaceAll("-", "") + (StringUtils.hasText(extension) ? "." + extension : "");
    }

    /**
     * 校验文件扩展名是否在白名单中
     */
    private void validateExtension(String extension) {
        if (!StringUtils.hasText(extension)) {
            throw new IllegalArgumentException("文件缺少扩展名，无法判断类型");
        }
        List<String> allowedTypes = fileStorageProperties.getAllowedTypes();
        boolean allowed = allowedTypes.stream()
                .map(type -> type.toLowerCase(Locale.ROOT))
                .anyMatch(type -> type.equals(extension.toLowerCase(Locale.ROOT)));
        if (!allowed) {
            throw new IllegalArgumentException("文件类型不被允许");
        }
    }

    /**
     * 提取文件扩展名
     */
    private String getFileExtension(String originalFilename) {
        if (!StringUtils.hasText(originalFilename)) {
            return "";
        }
        int index = originalFilename.lastIndexOf('.') + 1;
        if (index <= 0 || index >= originalFilename.length()) {
            return "";
        }
        return originalFilename.substring(index).toLowerCase(Locale.ROOT);
    }

    /**
     * 业务标签仅保留字母、数字、斜杠以及连接符，避免目录穿越
     */
    private String normalizeBizTag(String bizTag) {
        String defaultTag = "common";
        if (!StringUtils.hasText(bizTag)) {
            return defaultTag;
        }
        String sanitized = bizTag.trim().replaceAll("\\.\\.", "").replaceAll("[^a-zA-Z0-9/_-]", "");
        if (!StringUtils.hasText(sanitized)) {
            return defaultTag;
        }
        // 去掉路径首尾的斜杠，统一使用正斜杠
        sanitized = sanitized.replace('\\', '/');
        while (sanitized.startsWith("/")) {
            sanitized = sanitized.substring(1);
        }
        while (sanitized.endsWith("/")) {
            sanitized = sanitized.substring(0, sanitized.length() - 1);
        }
        return StringUtils.hasText(sanitized) ? sanitized : defaultTag;
    }
}
