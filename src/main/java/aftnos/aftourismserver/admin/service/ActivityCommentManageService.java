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

    /**
     * 后台分页查询全部评论，不强制指定活动 ID。
     *
     * @param query 分页参数，支持父级留言筛选
     * @return 评论分页结果
     */
    PageInfo<ActivityCommentVO> pageAllComments(ActivityCommentManagePageQuery query);

    /**
     * 按活动分页查询评论，可用于定位单个活动下的留言。
     *
     * @param activityId 活动 ID
     * @param query      分页参数，包含父级筛选
     * @return 指定活动的评论分页数据
     */
    PageInfo<ActivityCommentVO> pageCommentsByActivity(Long activityId, ActivityCommentManagePageQuery query);

    /**
     * 查询指定父级下的所有子留言。
     *
     * @param parentId 父级评论 ID
     * @return 子评论列表
     */
    List<ActivityCommentVO> listChildren(Long parentId);

    /** 创建评论（支持父级回复） */
    Long createComment(Long activityId, ActivityCommentManageDTO dto);

    /** 更新评论内容或父级关系 */
    void updateComment(Long commentId, ActivityCommentManageDTO dto);

    /** 查询单条评论详情（含子评论列表） */
    ActivityCommentDetailVO commentDetail(Long commentId);

    /** 删除评论（含递归删除子评论） */
    void deleteComment(Long commentId);
}
