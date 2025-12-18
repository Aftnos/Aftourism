package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.ScenicSpotPageQuery;
import aftnos.aftourismserver.admin.pojo.ScenicSpot;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.admin.vo.ScenicSpotVO;
import aftnos.aftourismserver.portal.dto.ScenicSpotPortalPageQuery;
import aftnos.aftourismserver.portal.vo.ScenicSpotDetailVO;
import aftnos.aftourismserver.portal.vo.ScenicSpotSummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 景区数据访问层
 */
@Mapper
public interface ScenicSpotMapper {

    int insert(ScenicSpot scenicSpot);

    int update(ScenicSpot scenicSpot);

    ScenicSpot selectById(@Param("id") Long id);

    List<ScenicSpotVO> pageList(ScenicSpotPageQuery query);

    int logicalDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    ScenicSpotVO selectDetail(@Param("id") Long id);

    List<ScenicSpotSummaryVO> portalPageList(ScenicSpotPortalPageQuery query);

    ScenicSpotDetailVO portalDetail(@Param("id") Long id);

    /**
     * 增加景区的浏览量
     */
    int incrementViewCount(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据ID集合查询景区基础信息
     */
    List<ScenicSpot> selectByIds(@Param("ids") List<Long> ids);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
