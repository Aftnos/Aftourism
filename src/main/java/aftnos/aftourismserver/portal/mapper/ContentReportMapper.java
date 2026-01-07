package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.admin.vo.ContentReportManageVO;
import aftnos.aftourismserver.portal.pojo.ContentReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 举报记录数据访问层
 */
@Mapper
public interface ContentReportMapper {

    int insert(ContentReport report);

    ContentReport selectById(@Param("id") Long id);

    List<ContentReportManageVO> pageListForManage(@Param("status") Integer status,
                                                  @Param("targetType") String targetType,
                                                  @Param("keyword") String keyword);

    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status,
                     @Param("violationFlag") Integer violationFlag,
                     @Param("resultRemark") String resultRemark,
                     @Param("updateTime") LocalDateTime updateTime);
}
