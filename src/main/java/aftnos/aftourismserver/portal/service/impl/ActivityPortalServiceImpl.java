package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.enums.ActivityApplyStatusEnum;
import aftnos.aftourismserver.admin.enums.ActivityOnlineStatusEnum;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.mapper.VenueMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.pojo.Venue;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ActivityApplyDTO;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.service.ActivityPortalService;
import aftnos.aftourismserver.portal.vo.ActivitySummaryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户活动业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityPortalServiceImpl implements ActivityPortalService {

    private final ActivityMapper activityMapper;
    private final VenueMapper venueMapper;

    @Override
    public Long apply(ActivityApplyDTO dto, Long userId) {
        log.info("【门户-活动申报】开始处理，活动名称={}，用户ID={}", dto.getName(), userId);
        if (dto.getStartTime() != null && dto.getEndTime() != null
                && !dto.getEndTime().isAfter(dto.getStartTime())) {
            log.warn("【门户-活动申报】时间区间非法，start={}，end={}", dto.getStartTime(), dto.getEndTime());
            throw new BusinessException("活动结束时间必须晚于开始时间");
        }
        Venue venue = venueMapper.selectById(dto.getVenueId());
        if (venue == null) {
            log.warn("【门户-活动申报】场馆不存在，venueId={}", dto.getVenueId());
            throw new BusinessException("场馆不存在或已被删除");
        }
        int overlapCount = activityMapper.countOverlapActivities(dto.getVenueId(), dto.getStartTime(), dto.getEndTime());
        if (overlapCount > 0) {
            log.warn("【门户-活动申报】存在时间冲突，venueId={}，start={}，end={}", dto.getVenueId(), dto.getStartTime(), dto.getEndTime());
            throw new BusinessException("当前时间段该场馆已排期其它活动，请调整时间");
        }
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setVenueId(dto.getVenueId());
        activity.setApplyUserId(userId);
        activity.setApplyStatus(ActivityApplyStatusEnum.PENDING.getCode());
        activity.setOnlineStatus(ActivityOnlineStatusEnum.ONLINE.getCode());
        activity.setAddressCache(venue.getAddress());
        activity.setViewCount(0L);
        activity.setFavoriteCount(0L);
        activity.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        int rows = activityMapper.insert(activity);
        log.info("【门户-活动申报】写入完成，影响行数={}，生成ID={}", rows, activity.getId());
        return activity.getId();
    }

    @Override
    public PageInfo<ActivitySummaryVO> pageActivities(ActivityPortalPageQuery query) {
        log.info("【门户-分页查询活动】开始处理，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySummaryVO> list = activityMapper.portalPageList(query,
                ActivityApplyStatusEnum.APPROVED.getCode(), ActivityOnlineStatusEnum.ONLINE.getCode());
        PageInfo<ActivitySummaryVO> pageInfo = new PageInfo<>(list);
        log.info("【门户-分页查询活动】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }
}
