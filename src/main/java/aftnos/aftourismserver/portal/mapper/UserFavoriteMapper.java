package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.portal.pojo.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户收藏数据访问层
 */
@Mapper
public interface UserFavoriteMapper {

    /** 新增收藏 */
    int insert(UserFavorite favorite);

    /**
     * 根据唯一键查询收藏记录
     */
    UserFavorite selectByUnique(@Param("userId") Long userId,
                                 @Param("targetType") String targetType,
                                 @Param("targetId") Long targetId);

    /**
     * 恢复逻辑删除的收藏记录
     */
    int restore(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 逻辑删除收藏记录
     */
    int logicalDelete(@Param("userId") Long userId,
                      @Param("targetType") String targetType,
                      @Param("targetId") Long targetId,
                      @Param("updateTime") LocalDateTime updateTime);

    /**
     * 分页查询收藏记录
     */
    List<UserFavorite> pageList(@Param("userId") Long userId,
                                 @Param("targetType") String targetType);
}
