package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.NewsPageQuery;
import aftnos.aftourismserver.admin.pojo.News;
import aftnos.aftourismserver.admin.vo.NewsVO;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.portal.dto.NewsPortalPageQuery;
import aftnos.aftourismserver.portal.vo.NewsPortalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NewsMapper {

    int insert(News news);

    int update(News news);

    News selectById(@Param("id") Long id);

    List<NewsVO> pageList(NewsPageQuery query);

    int logicalDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    List<NewsPortalVO> portalPageList(NewsPortalPageQuery query);

    NewsPortalVO portalDetail(@Param("id") Long id);

    int incrementViewCount(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
