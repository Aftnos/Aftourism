package com.aftourism.file.service.impl;

import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import com.aftourism.file.config.FileStorageProperties;
import com.aftourism.file.pojo.FileInfo;
import com.aftourism.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 文件服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public FileInfo upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "上传文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        if (StringUtils.hasText(ext)) {
            fileName = fileName + "." + ext;
        }
        Path basePath = Paths.get(fileStorageProperties.getBasePath(), LocalDate.now().toString());
        try {
            Files.createDirectories(basePath);
            Path target = basePath.resolve(fileName);
            file.transferTo(target.toFile());
            String url = fileStorageProperties.getAccessHost() + "/" + basePath.getFileName() + "/" + fileName;
            return FileInfo.builder()
                    .originalFilename(originalFilename)
                    .filename(fileName)
                    .size(file.getSize())
                    .url(url)
                    .build();
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
    }
}
