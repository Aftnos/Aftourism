package com.aftourism.file.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.file.pojo.FileInfo;
import com.aftourism.file.service.FileService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口。
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileService fileService;

    /** 上传文件 */
    @PostMapping("/upload")
    public ResponseResult<FileInfo> upload(@NotNull(message = "文件不能为空") MultipartFile file) {
        return ResponseResult.ok(fileService.upload(file));
    }
}
