package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.VenuePageQuery;
import aftnos.aftourismserver.admin.pojo.Venue;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.admin.vo.VenueVO;
import aftnos.aftourismserver.portal.dto.VenuePortalPageQuery;
import aftnos.aftourismserver.portal.vo.VenueDetailVO;
import aftnos.aftourismserver.portal.vo.VenueSummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 场馆相关数据库操作
 */
@Mapper
public interface VenueMapper {

    /** 新增场馆 */
    int insert(Venue venue);

    /** 根据主键更新场馆信息 */
    int update(Venue venue);

    /** 根据ID查询场馆 */
    Venue selectById(@Param("id") Long id);

    /** 分页查询场馆列表 */
    List<VenueVO> pageList(VenuePageQuery query);

    /** 逻辑删除场馆 */
    int logicalDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /** 查询场馆详情 */
    VenueVO selectDetail(@Param("id") Long id);

    /** 门户分页查询 */
    List<VenueSummaryVO> portalPageList(VenuePortalPageQuery query);

    /** 门户详情查询 */
    VenueDetailVO portalDetail(@Param("id") Long id);

    /**
     * 增加场馆浏览量，统计门户内容点击量
     */
    int incrementViewCount(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据ID集合批量查询场馆
     */
    List<Venue> selectByIds(@Param("ids") List<Long> ids);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
