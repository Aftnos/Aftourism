package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ActivityCommentManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityCommentManagePageQuery;
import aftnos.aftourismserver.admin.vo.ActivityCommentDetailVO;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageInfo;

/**
 * 后台管理模块的活动评论管理业务接口，提供评论分页与删除能力。
 */
public interface ActivityCommentManageService {

    PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentManagePageQuery query);

    Long createComment(Long activityId, ActivityCommentManageDTO dto);

    void updateComment(Long commentId, ActivityCommentManageDTO dto);

    ActivityCommentDetailVO commentDetail(Long commentId);

    void deleteComment(Long commentId);
}
