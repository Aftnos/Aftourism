package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * 工作台内容摘要响应
 */
@Data
public class ContentDigestVO {
    /** 最新新闻列表 */
    private List<ContentBriefVO> newsList;
    /** 最新通知公告列表 */
    private List<ContentBriefVO> noticeList;
}
