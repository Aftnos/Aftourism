package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.MessageFeedbackManageDTO;
import aftnos.aftourismserver.admin.dto.MessageFeedbackManagePageQuery;
import aftnos.aftourismserver.admin.service.MessageFeedbackManageService;
import aftnos.aftourismserver.admin.vo.MessageFeedbackManageVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.enums.MessageFeedbackStatusEnum;
import aftnos.aftourismserver.portal.enums.MessageFeedbackTypeEnum;
import aftnos.aftourismserver.portal.mapper.MessageFeedbackMapper;
import aftnos.aftourismserver.portal.pojo.MessageFeedback;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言反馈后台管理实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageFeedbackManageServiceImpl implements MessageFeedbackManageService {

    private final MessageFeedbackMapper messageFeedbackMapper;

    @Override
    public PageInfo<MessageFeedbackManageVO> page(MessageFeedbackManagePageQuery query) {
        log.info("【后台-留言反馈】分页查询，页码={}，每页={}", query.getCurrent(), query.getSize());
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<MessageFeedbackManageVO> list = messageFeedbackMapper.pageListForManage(
                normalizeType(query.getType()),
                query.getStatus(),
                query.getKeyword()
        );
        return new PageInfo<>(list);
    }

    @Override
    public MessageFeedbackManageVO detail(Long id) {
        MessageFeedbackManageVO vo = messageFeedbackMapper.selectManageById(id);
        if (vo == null) {
            throw new BusinessException("留言反馈不存在或已删除");
        }
        return vo;
    }

    @Override
    public void update(Long id, MessageFeedbackManageDTO dto) {
        MessageFeedback feedback = messageFeedbackMapper.selectById(id);
        if (feedback == null || feedback.getIsDeleted() != null && feedback.getIsDeleted() == 1) {
            throw new BusinessException("留言反馈不存在或已删除");
        }
        MessageFeedbackTypeEnum typeEnum = MessageFeedbackTypeEnum.fromCode(dto.getType());
        MessageFeedbackStatusEnum statusEnum = MessageFeedbackStatusEnum.fromCode(dto.getStatus());
        int rows = messageFeedbackMapper.updateForManage(
                id,
                typeEnum.getCode(),
                dto.getTitle(),
                dto.getContent(),
                dto.getContactPhone(),
                dto.getContactEmail(),
                statusEnum.getCode(),
                LocalDateTime.now()
        );
        if (rows == 0) {
            throw new BusinessException("更新失败，请稍后重试");
        }
    }

    @Override
    public void delete(Long id) {
        MessageFeedback feedback = messageFeedbackMapper.selectById(id);
        if (feedback == null || feedback.getIsDeleted() != null && feedback.getIsDeleted() == 1) {
            throw new BusinessException("留言反馈不存在或已删除");
        }
        int rows = messageFeedbackMapper.markDeleted(id, LocalDateTime.now());
        if (rows == 0) {
            throw new BusinessException("删除失败，请稍后重试");
        }
    }

    private String normalizeType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return null;
        }
        return MessageFeedbackTypeEnum.fromCode(rawType).getCode();
    }
}
