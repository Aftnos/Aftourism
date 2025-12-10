package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ActivityCommentManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityCommentManagePageQuery;
import aftnos.aftourismserver.admin.vo.ActivityCommentDetailVO;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 后台管理模块的活动评论管理业务接口，提供评论分页与删除能力。
 */
public interface ActivityCommentManageService {

    PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentManagePageQuery query);

    Long createComment(Long activityId, ActivityCommentManageDTO dto);

    void updateComment(Long commentId, ActivityCommentManageDTO dto);

    ActivityCommentDetailVO commentDetail(Long commentId);

    /**
     * 查询指定活动下的全部留言，包含楼中楼结构
     *
     * @param activityId 活动ID
     * @return 留言列表（不分页）
     */
    List<ActivityCommentVO> listAllComments(Long activityId);

    void deleteComment(Long commentId);
}
