package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ActivityCommentManagePageQuery;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageInfo;

public interface ActivityCommentManageService {

    PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentManagePageQuery query);

    void deleteComment(Long commentId);
}
