package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.UserFavoritePageQuery;
import aftnos.aftourismserver.portal.vo.UserFavoriteVO;
import com.github.pagehelper.PageInfo;

/**
 * 用户收藏业务接口
 */
public interface UserFavoriteService {

    /**
     * 添加收藏
     *
     * @param userId     当前登录用户ID
     * @param targetType 收藏目标类型
     * @param targetId   收藏目标ID
     * @return 收藏记录ID
     */
    Long addFavorite(Long userId, String targetType, Long targetId);

    /**
     * 取消收藏
     *
     * @param userId     当前登录用户ID
     * @param targetType 收藏目标类型
     * @param targetId   收藏目标ID
     */
    void cancelFavorite(Long userId, String targetType, Long targetId);

    /**
     * 分页查询收藏列表
     *
     * @param userId 当前登录用户ID
     * @param query  分页条件
     * @return 分页结果
     */
    PageInfo<UserFavoriteVO> pageFavorites(Long userId, UserFavoritePageQuery query);
}
