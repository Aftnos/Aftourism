package aftnos.aftourismserver.admin.vo;

import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动留言树形视图对象，便于一次性返回楼层及楼中楼结构。
 */
@Data
public class ActivityCommentTreeVO extends ActivityCommentVO {

    /**
     * 子留言集合，保持原有发布时间顺序，方便前端直接渲染楼中楼。
     */
    private List<ActivityCommentVO> children = new ArrayList<>();
}
