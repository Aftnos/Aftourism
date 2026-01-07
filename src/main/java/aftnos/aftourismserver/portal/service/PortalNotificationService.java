package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.PortalNotificationPageQuery;
import aftnos.aftourismserver.portal.vo.PortalNotificationVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户通知业务接口
 */
public interface PortalNotificationService {

    void createNotification(Long userId,
                            String type,
                            String title,
                            String content,
                            String relatedType,
                            Long relatedId);

    PageInfo<PortalNotificationVO> pageNotifications(Long userId, PortalNotificationPageQuery query);

    void markRead(Long userId, Long notificationId);

    void markAllRead(Long userId);
}
