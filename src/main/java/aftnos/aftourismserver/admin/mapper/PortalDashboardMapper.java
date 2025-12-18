package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.vo.ContentBriefVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门户首页看板统计 Mapper，负责聚合内容点击与简要信息。
 */
@Mapper
public interface PortalDashboardMapper {

    Long sumNewsViewCount();

    Long sumNoticeViewCount();

    Long sumScenicViewCount();

    Long sumVenueViewCount();

    Long sumActivityViewCount();

    List<ContentBriefVO> listLatestNews(@Param("limit") int limit);

    List<ContentBriefVO> listLatestNotices(@Param("limit") int limit);
}
