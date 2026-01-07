package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 交流文章发布参数
 */
@Data
public class ExchangeArticleCreateDTO {
    /** 标题 */
    @NotBlank(message = "标题不能为空")
    @Size(max = 120, message = "标题不能超过120字")
    private String title;

    /** 富文本内容 */
    @NotBlank(message = "内容不能为空")
    @Size(max = 20000, message = "内容不能超过20000字")
    private String content;

    /** 封面地址 */
    @Size(max = 255, message = "封面地址长度不能超过255字")
    private String coverUrl;
}
