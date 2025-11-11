package aftnos.aftourismserver.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传返回信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadVO {

    /**
     * 文件访问 URL
     */
    private String url;

    /**
     * 系统生成的文件名
     */
    private String fileName;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件大小（字节）
     */
    private Long size;
}
