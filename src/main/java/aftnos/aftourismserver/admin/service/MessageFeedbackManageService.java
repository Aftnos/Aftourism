package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.MessageFeedbackManageDTO;
import aftnos.aftourismserver.admin.dto.MessageFeedbackManagePageQuery;
import aftnos.aftourismserver.admin.vo.MessageFeedbackManageVO;
import com.github.pagehelper.PageInfo;

/**
 * 留言反馈后台管理服务
 */
public interface MessageFeedbackManageService {

    PageInfo<MessageFeedbackManageVO> page(MessageFeedbackManagePageQuery query);

    MessageFeedbackManageVO detail(Long id);

    void update(Long id, MessageFeedbackManageDTO dto);

    void delete(Long id);
}
