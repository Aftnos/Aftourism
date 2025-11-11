package aftnos.aftourismserver.common.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文件存储结果信息
 */
@Data
@AllArgsConstructor
public class StoredFileInfo {

    /**
     * 文件访问 URL
     */
    private String url;

    /**
     * 文件相对路径（相对于上传根目录）
     */
    private String relativePath;
}
