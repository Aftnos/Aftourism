package aftnos.aftourismserver.file.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储抽象接口，定义统一的存储行为
 */
public interface FileStorageService {

    /**
     * 保存文件并返回对外访问地址
     *
     * @param file   待上传的文件
     * @param bizTag 业务标识，用于划分子目录
     * @return 文件存储结果
     */
    StoredFileInfo store(MultipartFile file, String bizTag);
}
