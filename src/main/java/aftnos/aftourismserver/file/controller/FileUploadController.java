package aftnos.aftourismserver.file.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.file.storage.FileStorageService;
import aftnos.aftourismserver.file.storage.StoredFileInfo;
import aftnos.aftourismserver.common.vo.FileUploadVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    /**
     * 上传文件并返回访问地址
     *
     * @param file   上传文件
     * @param bizTag 业务目录标签
     * @return 文件上传结果
     */
    @PostMapping("/upload")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).FILE_UPLOAD)")
    public Result<FileUploadVO> upload(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "bizTag", required = false) String bizTag) {
        log.info("【文件上传】收到请求，原始文件名={}，业务标签={}", file.getOriginalFilename(), bizTag);
        StoredFileInfo storedFileInfo = fileStorageService.store(file, bizTag);
        FileUploadVO vo = new FileUploadVO(
                storedFileInfo.getUrl(),
                extractFileName(storedFileInfo.getRelativePath()),
                file.getOriginalFilename(),
                file.getSize()
        );
        return Result.success(vo);
    }

    /**
     * 从相对路径中提取文件名
     */
    private String extractFileName(String relativePath) {
        if (relativePath == null) {
            return null;
        }
        int index = relativePath.lastIndexOf('/') + 1;
        return index > 0 && index < relativePath.length() ? relativePath.substring(index) : relativePath;
    }
}
