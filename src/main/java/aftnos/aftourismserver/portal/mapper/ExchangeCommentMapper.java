package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.portal.pojo.ExchangeComment;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交流评论数据访问层
 */
@Mapper
public interface ExchangeCommentMapper {

    int insert(ExchangeComment comment);

    ExchangeComment selectById(@Param("id") Long id);

    ExchangeCommentVO selectVOById(@Param("id") Long id);

    List<ExchangeCommentVO> pageList(@Param("articleId") Long articleId,
                                     @Param("parentId") Long parentId);

    List<ExchangeCommentVO> listByParentIds(@Param("parentIds") List<Long> parentIds);

    int markDeleted(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int updateContent(@Param("id") Long id,
                      @Param("content") String content,
                      @Param("updateTime") LocalDateTime updateTime);

    int increaseLikeCount(@Param("id") Long id,
                          @Param("delta") int delta,
                          @Param("updateTime") LocalDateTime updateTime);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
