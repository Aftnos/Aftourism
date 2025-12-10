package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ActivityCommentManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityCommentManagePageQuery;
import aftnos.aftourismserver.admin.vo.ActivityCommentDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityCommentTreeVO;
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
     * 查询指定活动下的全部留言，返回树形结构（包含楼中楼）。
     *
     * @param activityId 活动ID
     * @return 留言树，包含顶层与多级回复
     */
    List<ActivityCommentTreeVO> listCommentTreeByActivity(Long activityId);

    /**
     * 查询后台全部活动留言（不区分活动），返回树形结构。
     *
     * @return 全部留言的树形列表，便于统一审核与检索
     */
    List<ActivityCommentTreeVO> listAllCommentTree();

    void deleteComment(Long commentId);
}
