package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.mapper.ScenicSpotMapper;
import aftnos.aftourismserver.admin.mapper.VenueMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.pojo.ScenicSpot;
import aftnos.aftourismserver.admin.pojo.Venue;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.UserFavoritePageQuery;
import aftnos.aftourismserver.portal.enums.FavoriteTargetTypeEnum;
import aftnos.aftourismserver.portal.mapper.UserFavoriteMapper;
import aftnos.aftourismserver.portal.pojo.UserFavorite;
import aftnos.aftourismserver.portal.service.UserFavoriteService;
import aftnos.aftourismserver.portal.vo.UserFavoriteVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户收藏业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavoriteServiceImpl implements UserFavoriteService {

    private final UserFavoriteMapper userFavoriteMapper;
    private final ScenicSpotMapper scenicSpotMapper;
    private final VenueMapper venueMapper;
    private final ActivityMapper activityMapper;

    @Override
    public Long addFavorite(Long userId, String targetTypeCode, Long targetId) {
        FavoriteTargetTypeEnum targetType = FavoriteTargetTypeEnum.fromCode(targetTypeCode);
        if (targetType == null) {
            throw new BusinessException("收藏类型不受支持");
        }
        log.info("【门户-收藏】开始新增收藏，用户ID={}，类型={}，目标ID={}", userId, targetType.name(), targetId);

        // 校验目标是否存在，避免收藏无效数据
        validateTargetExists(targetType, targetId);

        UserFavorite existed = userFavoriteMapper.selectByUnique(userId, targetType.name(), targetId);
        LocalDateTime now = LocalDateTime.now();
        if (existed == null) {
            UserFavorite favorite = new UserFavorite();
            favorite.setUserId(userId);
            favorite.setTargetType(targetType.name());
            favorite.setTargetId(targetId);
            favorite.setIsDeleted(0);
            favorite.setCreateTime(now);
            favorite.setUpdateTime(now);
            userFavoriteMapper.insert(favorite);
            log.info("【门户-收藏】新增收藏成功，生成记录ID={}", favorite.getId());
            return favorite.getId();
        }

        if (existed.getIsDeleted() != null && existed.getIsDeleted() == 1) {
            userFavoriteMapper.restore(existed.getId(), now);
            log.info("【门户-收藏】恢复历史收藏记录成功，记录ID={}", existed.getId());
            return existed.getId();
        }

        log.info("【门户-收藏】已存在有效收藏，直接返回记录ID={}", existed.getId());
        return existed.getId();
    }

    @Override
    public void cancelFavorite(Long userId, String targetTypeCode, Long targetId) {
        FavoriteTargetTypeEnum targetType = FavoriteTargetTypeEnum.fromCode(targetTypeCode);
        if (targetType == null) {
            throw new BusinessException("收藏类型不受支持");
        }
        log.info("【门户-收藏】取消收藏，用户ID={}，类型={}，目标ID={}", userId, targetType.name(), targetId);
        UserFavorite existed = userFavoriteMapper.selectByUnique(userId, targetType.name(), targetId);
        if (existed == null || existed.getIsDeleted() != null && existed.getIsDeleted() == 1) {
            throw new BusinessException("当前目标未被收藏或已取消");
        }
        LocalDateTime now = LocalDateTime.now();
        int rows = userFavoriteMapper.logicalDelete(userId, targetType.name(), targetId, now);
        if (rows == 0) {
            throw new BusinessException("取消收藏失败，请稍后再试");
        }
        log.info("【门户-收藏】取消收藏成功，影响行数={}", rows);
    }

    @Override
    public PageInfo<UserFavoriteVO> pageFavorites(Long userId, UserFavoritePageQuery query) {
        String typeFilter = query.getType();
        if (typeFilter != null && FavoriteTargetTypeEnum.fromCode(typeFilter) == null) {
            throw new BusinessException("收藏类型不受支持");
        }
        log.info("【门户-收藏】分页查询收藏记录，用户ID={}，类型过滤={}，页码={}，每页={}",
                userId, typeFilter, query.getCurrent(), query.getSize());
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<UserFavorite> favorites = userFavoriteMapper.pageList(userId,
                typeFilter == null ? null : FavoriteTargetTypeEnum.fromCode(typeFilter).name());
        PageInfo<UserFavorite> pageInfo = new PageInfo<>(favorites);

        List<UserFavoriteVO> voList = assembleFavoriteVOs(favorites);
        PageInfo<UserFavoriteVO> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);
        return result;
    }

    /**
     * 根据收藏类型验证目标存在性
     */
    private void validateTargetExists(FavoriteTargetTypeEnum type, Long targetId) {
        switch (type) {
            case SCENIC -> {
                ScenicSpot scenicSpot = scenicSpotMapper.selectById(targetId);
                if (scenicSpot == null) {
                    throw new BusinessException("景区不存在或已下架");
                }
            }
            case VENUE -> {
                Venue venue = venueMapper.selectById(targetId);
                if (venue == null) {
                    throw new BusinessException("场馆不存在或已下架");
                }
            }
            case ACTIVITY -> {
                Activity activity = activityMapper.selectById(targetId);
                if (activity == null) {
                    throw new BusinessException("活动不存在或已下架");
                }
            }
            default -> throw new BusinessException("收藏类型不受支持");
        }
    }

    /**
     * 组装收藏返回对象，补充目标名称与封面
     */
    private List<UserFavoriteVO> assembleFavoriteVOs(List<UserFavorite> favorites) {
        if (favorites.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> scenicIds = new ArrayList<>();
        List<Long> venueIds = new ArrayList<>();
        List<Long> activityIds = new ArrayList<>();
        for (UserFavorite favorite : favorites) {
            FavoriteTargetTypeEnum typeEnum = FavoriteTargetTypeEnum.fromCode(favorite.getTargetType());
            if (typeEnum == null) {
                continue;
            }
            switch (typeEnum) {
                case SCENIC -> scenicIds.add(favorite.getTargetId());
                case VENUE -> venueIds.add(favorite.getTargetId());
                case ACTIVITY -> activityIds.add(favorite.getTargetId());
                default -> {
                }
            }
        }

        Map<Long, ScenicSpot> scenicMap = scenicIds.isEmpty()
                ? Collections.emptyMap()
                : scenicSpotMapper.selectByIds(scenicIds).stream()
                .collect(Collectors.toMap(ScenicSpot::getId, Function.identity(), (a, b) -> a));
        Map<Long, Venue> venueMap = venueIds.isEmpty()
                ? Collections.emptyMap()
                : venueMapper.selectByIds(venueIds).stream()
                .collect(Collectors.toMap(Venue::getId, Function.identity(), (a, b) -> a));
        Map<Long, Activity> activityMap = activityIds.isEmpty()
                ? Collections.emptyMap()
                : activityMapper.selectByIds(activityIds).stream()
                .collect(Collectors.toMap(Activity::getId, Function.identity(), (a, b) -> a));

        List<UserFavoriteVO> voList = new ArrayList<>(favorites.size());
        for (UserFavorite favorite : favorites) {
            UserFavoriteVO vo = new UserFavoriteVO();
            vo.setId(favorite.getId());
            vo.setTargetType(favorite.getTargetType());
            vo.setTargetId(favorite.getTargetId());
            vo.setCreateTime(favorite.getCreateTime());

            FavoriteTargetTypeEnum typeEnum = FavoriteTargetTypeEnum.fromCode(favorite.getTargetType());
            if (typeEnum != null) {
                switch (typeEnum) {
                    case SCENIC -> {
                        ScenicSpot scenicSpot = scenicMap.get(favorite.getTargetId());
                        vo.setTargetName(scenicSpot != null ? scenicSpot.getName() : "景区已下架");
                        vo.setTargetCover(scenicSpot != null ? scenicSpot.getImageUrl() : null);
                    }
                    case VENUE -> {
                        Venue venue = venueMap.get(favorite.getTargetId());
                        vo.setTargetName(venue != null ? venue.getName() : "场馆已下架");
                        vo.setTargetCover(venue != null ? venue.getImageUrl() : null);
                    }
                    case ACTIVITY -> {
                        Activity activity = activityMap.get(favorite.getTargetId());
                        vo.setTargetName(activity != null ? activity.getName() : "活动已下架");
                        vo.setTargetCover(activity != null ? activity.getCoverUrl() : null);
                    }
                    default -> {
                    }
                }
            }
            voList.add(vo);
        }
        return voList;
    }
}
