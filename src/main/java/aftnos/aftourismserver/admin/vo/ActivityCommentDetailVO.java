package aftnos.aftourismserver.admin.vo;

import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import lombok.Data;

import java.util.List;

/**
 * 后台活动留言详情视图，包含主楼层和楼中楼回复列表。
 */
@Data
public class ActivityCommentDetailVO {

    /** 主留言信息 */
    private ActivityCommentVO comment;

    /** 楼中楼回复列表，按时间顺序排列 */
    private List<ActivityCommentVO> replies;
}
