package com.aftourism.file.service;

import com.aftourism.file.pojo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口。
 */
public interface FileService {

    FileInfo upload(MultipartFile file);
}
