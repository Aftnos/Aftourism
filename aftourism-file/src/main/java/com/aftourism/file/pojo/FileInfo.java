package com.aftourism.file.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 文件信息对象。
 */
@Data
@Builder
public class FileInfo {

    /** 原始文件名 */
    private String originalFilename;

    /** 存储后的文件名 */
    private String filename;

    /** 访问地址 */
    private String url;

    /** 文件大小 */
    private Long size;
}
