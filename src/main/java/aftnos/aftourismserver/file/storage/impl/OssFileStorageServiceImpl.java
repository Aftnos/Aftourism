package aftnos.aftourismserver.file.storage.impl;

import aftnos.aftourismserver.file.storage.FileStorageService;
import aftnos.aftourismserver.file.storage.StoredFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * OSS 文件存储预留实现，当前暂未接入云厂商 SDK
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "file.storage-type", havingValue = "oss")
public class OssFileStorageServiceImpl implements FileStorageService {

    @Override
    public StoredFileInfo store(MultipartFile file, String bizTag) {
        log.warn("【文件上传】当前存储方式设置为 OSS，但实现暂未完成");
        throw new UnsupportedOperationException("OSS 文件存储暂未实现");
    }
}
