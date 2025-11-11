package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.ActivityCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ActivityCommentPageQuery;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageInfo;

/**
 * 活动留言业务接口
 */
public interface ActivityCommentService {

    /**
     * 新增留言
     *
     * @param activityId 活动ID
     * @param dto        留言内容参数
     * @param userId     当前登录用户ID
     * @return 新增留言ID
     */
    Long addComment(Long activityId, ActivityCommentCreateDTO dto, Long userId);

    /**
     * 分页查询留言
     *
     * @param activityId 活动ID
     * @param query      分页参数
     * @return 留言分页数据
     */
    PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentPageQuery query);
}
