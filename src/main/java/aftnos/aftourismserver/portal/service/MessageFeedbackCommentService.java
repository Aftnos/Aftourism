package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.MessageFeedbackCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCommentPageQuery;
import aftnos.aftourismserver.portal.vo.MessageFeedbackCommentVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户留言反馈评论服务
 */
public interface MessageFeedbackCommentService {

    Long addComment(Long feedbackId, MessageFeedbackCommentCreateDTO dto, Long userId);

    PageInfo<MessageFeedbackCommentVO> pageComments(Long feedbackId, MessageFeedbackCommentPageQuery query);

    void likeComment(Long commentId, Long userId);

    void deleteOwnComment(Long commentId, Long userId);
}
