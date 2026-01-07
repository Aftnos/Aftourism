package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.admin.vo.ExchangeArticleManageVO;
import aftnos.aftourismserver.portal.pojo.ExchangeArticle;
import aftnos.aftourismserver.portal.vo.ExchangeArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交流文章数据访问层
 */
@Mapper
public interface ExchangeArticleMapper {

    int insert(ExchangeArticle article);

    ExchangeArticle selectById(@Param("id") Long id);

    ExchangeArticleVO selectVOById(@Param("id") Long id);

    List<ExchangeArticleVO> pageList(@Param("status") Integer status,
                                     @Param("keyword") String keyword,
                                     @Param("userId") Long userId);

    List<ExchangeArticleManageVO> pageListForManage(@Param("status") Integer status,
                                                    @Param("keyword") String keyword);

    ExchangeArticleManageVO selectManageById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status,
                     @Param("auditRemark") String auditRemark,
                     @Param("updateTime") LocalDateTime updateTime);

    int increaseLikeCount(@Param("id") Long id,
                          @Param("delta") int delta,
                          @Param("updateTime") LocalDateTime updateTime);

    int increaseCommentCount(@Param("id") Long id,
                             @Param("delta") int delta,
                             @Param("updateTime") LocalDateTime updateTime);
}
