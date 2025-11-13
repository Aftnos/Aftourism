package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.UserFavoritePageQuery;
import aftnos.aftourismserver.portal.service.UserFavoriteService;
import aftnos.aftourismserver.portal.vo.UserFavoriteVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户收藏接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/fav")
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;

    /**
     * 添加收藏
     */
    @PostMapping("/{type}/{id}")
    public Result<Long> addFavorite(@PathVariable String type,
                                    @PathVariable("id") Long targetId) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        log.info("【门户-收藏】收到添加收藏请求，用户ID={}，类型={}，目标ID={}", userId, type, targetId);
        Long favoriteId = userFavoriteService.addFavorite(userId, type, targetId);
        return Result.success(favoriteId);
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{type}/{id}")
    public Result<Void> cancelFavorite(@PathVariable String type,
                                       @PathVariable("id") Long targetId) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        log.info("【门户-收藏】收到取消收藏请求，用户ID={}，类型={}，目标ID={}", userId, type, targetId);
        userFavoriteService.cancelFavorite(userId, type, targetId);
        return Result.success();
    }

    /**
     * 分页查询收藏列表
     */
    @GetMapping("/page")
    public Result<PageInfo<UserFavoriteVO>> pageFavorites(@Valid UserFavoritePageQuery query) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        log.info("【门户-收藏】收到分页查询请求，用户ID={}，类型过滤={}", userId, query.getType());
        PageInfo<UserFavoriteVO> pageInfo = userFavoriteService.pageFavorites(userId, query);
        return Result.success(pageInfo);
    }
}
