package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.MessageFeedbackCreateDTO;
import aftnos.aftourismserver.portal.dto.MessageFeedbackPageQuery;
import aftnos.aftourismserver.portal.vo.MessageFeedbackVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户留言反馈服务
 */
public interface MessageFeedbackService {

    /**
     * 新增留言反馈
     */
    Long createFeedback(Long userId, MessageFeedbackCreateDTO dto);

    /**
     * 门户分页查询留言反馈
     */
    PageInfo<MessageFeedbackVO> pageFeedback(MessageFeedbackPageQuery query);

    /**
     * 门户留言反馈详情
     */
    MessageFeedbackVO getDetail(Long id);
}
