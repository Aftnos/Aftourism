package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.portal.pojo.PortalNotification;
import aftnos.aftourismserver.portal.vo.PortalNotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户通知数据访问层
 */
@Mapper
public interface PortalNotificationMapper {

    int insert(PortalNotification notification);

    List<PortalNotificationVO> pageList(@Param("userId") Long userId,
                                        @Param("unreadOnly") Integer unreadOnly);

    int markRead(@Param("id") Long id,
                 @Param("userId") Long userId,
                 @Param("updateTime") LocalDateTime updateTime);

    int markAllRead(@Param("userId") Long userId,
                    @Param("updateTime") LocalDateTime updateTime);
}
