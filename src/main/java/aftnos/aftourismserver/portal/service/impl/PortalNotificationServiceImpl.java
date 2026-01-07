package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.portal.dto.PortalNotificationPageQuery;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.PortalNotificationMapper;
import aftnos.aftourismserver.portal.pojo.PortalNotification;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import aftnos.aftourismserver.portal.vo.PortalNotificationVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户通知业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortalNotificationServiceImpl implements PortalNotificationService {

    private final PortalNotificationMapper portalNotificationMapper;

    @Override
    public void createNotification(Long userId,
                                   String type,
                                   String title,
                                   String content,
                                   String relatedType,
                                   Long relatedId) {
        if (userId == null) {
            return;
        }
        PortalNotification notification = new PortalNotification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedType(relatedType);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notification.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        notification.setCreateTime(now);
        notification.setUpdateTime(now);
        portalNotificationMapper.insert(notification);
        log.info("【门户-通知】创建通知成功，用户ID={}，类型={}", userId, type);
    }

    @Override
    public PageInfo<PortalNotificationVO> pageNotifications(Long userId, PortalNotificationPageQuery query) {
        log.info("【门户-通知】分页查询通知，用户ID={}，页码={}，每页={}", userId, query.getCurrent(), query.getSize());
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<PortalNotificationVO> list = portalNotificationMapper.pageList(userId, query.getUnreadOnly());
        fillTypeText(list);
        return new PageInfo<>(list);
    }

    @Override
    public void markRead(Long userId, Long notificationId) {
        portalNotificationMapper.markRead(notificationId, userId, LocalDateTime.now());
    }

    @Override
    public void markAllRead(Long userId) {
        portalNotificationMapper.markAllRead(userId, LocalDateTime.now());
    }

    private void fillTypeText(List<PortalNotificationVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (PortalNotificationVO vo : list) {
            PortalNotificationTypeEnum typeEnum = PortalNotificationTypeEnum.fromCode(vo.getType());
            vo.setTypeText(typeEnum.getText());
        }
    }
}
