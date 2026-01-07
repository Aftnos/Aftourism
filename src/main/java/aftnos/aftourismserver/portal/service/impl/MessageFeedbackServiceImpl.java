package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCreateDTO;
import aftnos.aftourismserver.portal.dto.MessageFeedbackPageQuery;
import aftnos.aftourismserver.portal.enums.MessageFeedbackStatusEnum;
import aftnos.aftourismserver.portal.enums.MessageFeedbackTypeEnum;
import aftnos.aftourismserver.portal.mapper.MessageFeedbackMapper;
import aftnos.aftourismserver.portal.pojo.MessageFeedback;
import aftnos.aftourismserver.portal.service.MessageFeedbackService;
import aftnos.aftourismserver.portal.vo.MessageFeedbackVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 门户留言反馈服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageFeedbackServiceImpl implements MessageFeedbackService {

    private final MessageFeedbackMapper messageFeedbackMapper;
    private final UserMapper userMapper;

    @Override
    public Long createFeedback(Long userId, MessageFeedbackCreateDTO dto) {
        log.info("【门户-留言反馈】提交留言，用户ID={}，类型={}", userId, dto.getType());
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("用户不存在或已失效");
        }
        MessageFeedbackTypeEnum typeEnum = MessageFeedbackTypeEnum.fromCode(dto.getType());
        MessageFeedback feedback = new MessageFeedback();
        feedback.setUserId(userId);
        feedback.setType(typeEnum.getCode());
        feedback.setTitle(dto.getTitle());
        feedback.setContent(dto.getContent());
        feedback.setContactPhone(dto.getContactPhone());
        feedback.setContactEmail(dto.getContactEmail());
        feedback.setStatus(MessageFeedbackStatusEnum.PENDING.getCode());
        feedback.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        messageFeedbackMapper.insert(feedback);
        return feedback.getId();
    }

    @Override
    public PageInfo<MessageFeedbackVO> pageFeedback(MessageFeedbackPageQuery query) {
        log.info("【门户-留言反馈】分页查询留言，页码={}，每页={}", query.getCurrent(), query.getSize());
        PageHelper.startPage(query.getCurrent(), query.getSize());
        String type = normalizeType(query.getType());
        List<MessageFeedbackVO> list = messageFeedbackMapper.pageList(type);
        enrichUserInfo(list);
        fillTextInfo(list);
        return new PageInfo<>(list);
    }

    @Override
    public MessageFeedbackVO getDetail(Long id) {
        MessageFeedbackVO vo = messageFeedbackMapper.selectVOById(id);
        if (vo == null) {
            throw new BusinessException("留言不存在或已删除");
        }
        enrichUserInfo(List.of(vo));
        fillTextInfo(List.of(vo));
        return vo;
    }

    /**
     * 补充用户昵称与头像
     */
    private void enrichUserInfo(List<MessageFeedbackVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Set<Long> userIds = list.stream()
                .map(MessageFeedbackVO::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return;
        }
        List<User> users = userMapper.findByIds(userIds.stream().toList());
        Map<Long, User> userMap = users == null ? Collections.emptyMap()
                : users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        for (MessageFeedbackVO vo : list) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("用户已注销");
            }
        }
    }

    private void fillTextInfo(List<MessageFeedbackVO> list) {
        for (MessageFeedbackVO vo : list) {
            MessageFeedbackTypeEnum typeEnum = MessageFeedbackTypeEnum.fromCode(vo.getType());
            vo.setTypeText(typeEnum.getText());
            vo.setStatusText(MessageFeedbackStatusEnum.fromCode(vo.getStatus()).getText());
        }
    }

    private String normalizeType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return null;
        }
        return MessageFeedbackTypeEnum.fromCode(rawType).getCode();
    }
}
