package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.NoticePageQuery;
import aftnos.aftourismserver.admin.pojo.Notice;
import aftnos.aftourismserver.admin.vo.NoticeVO;
import aftnos.aftourismserver.portal.dto.NoticePortalPageQuery;
import aftnos.aftourismserver.portal.vo.NoticeDetailVO;
import aftnos.aftourismserver.portal.vo.NoticeSummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告数据访问层
 */
@Mapper
public interface NoticeMapper {

    int insert(Notice notice);

    int update(Notice notice);

    Notice selectById(@Param("id") Long id);

    List<NoticeVO> pageList(NoticePageQuery query);

    int logicalDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    List<NoticeSummaryVO> portalPageList(NoticePortalPageQuery query);

    NoticeDetailVO portalDetail(@Param("id") Long id);

    int incrementViewCount(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);
}
