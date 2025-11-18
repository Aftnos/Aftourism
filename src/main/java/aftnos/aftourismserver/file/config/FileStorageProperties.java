package aftnos.aftourismserver.file.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件存储相关配置项
 */
@Data
@Validated
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    /**
     * 文件上传根目录，本地存储时会在该目录下创建业务子目录和日期目录
     */
    @NotBlank(message = "文件上传目录不能为空")
    private String uploadDir;

    /**
     * 对外访问的基础 URL，用于拼接最终的文件访问地址
     */
    @NotBlank(message = "文件访问基础地址不能为空")
    private String baseUrl;

    /**
     * 静态资源访问匹配路径，用于 Spring MVC 资源映射，例如 /files/**
     */
    @NotBlank(message = "文件访问匹配路径不能为空")
    private String accessPattern = "/files/**";

    /**
     * 存储方式，默认 local，预留 oss
     */
    @NotBlank(message = "文件存储方式不能为空")
    private String storageType = "local";

    /**
     * 允许上传的文件类型白名单（扩展名），统一使用小写，默认包含常用图片与 PDF
     */
    @NotEmpty(message = "文件类型白名单不能为空")
    private List<String> allowedTypes = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png", "gif", "pdf"));

    /*
     * 以下是 OSS 预留的接口，目前未使用，可以根据需要自行添加（谁用OSS啊？贵的一痞）
     */

    /**
     * OSS 相关占位配置，未来接入云存储时使用
     */
    private final Oss oss = new Oss();

    /**
     * OSS 占位配置类，仅作为配置项预留
     */
    @Data
    public static class Oss {
        /**
         * OSS 服务的 Endpoint，当前实现未使用
         */
        private String endpoint;

        /**
         * OSS 存储桶名称
         */
        private String bucketName;

        /**
         * 访问密钥 ID
         */
        private String accessKeyId;

        /**
         * 访问密钥 Secret
         */
        private String accessKeySecret;
    }
}
