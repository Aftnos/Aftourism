package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.enums.ActivityApplyStatusEnum;
import aftnos.aftourismserver.admin.enums.ActivityOnlineStatusEnum;
import aftnos.aftourismserver.admin.mapper.ActivityApplyMapper;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.mapper.VenueMapper;
import aftnos.aftourismserver.admin.pojo.ActivityApply;
import aftnos.aftourismserver.admin.pojo.Venue;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.cache.PortalCacheable;
import aftnos.aftourismserver.portal.dto.ActivityApplyDTO;
import aftnos.aftourismserver.portal.dto.ActivityApplyPageQuery;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.service.ActivityPortalService;
import aftnos.aftourismserver.portal.vo.ActivityApplyRecordVO;
import aftnos.aftourismserver.portal.vo.ActivityDetailVO;
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
    private final ActivityApplyMapper activityApplyMapper;
    private final VenueMapper venueMapper;
    private final UserMapper userMapper;

    @Override
    public Long apply(ActivityApplyDTO dto, Long userId) {
        log.info("【门户-活动申报】开始处理，活动名称={}，用户ID={}", dto.getName(), userId);
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("用户不存在或已失效");
        }
        if (user.getIsAdvanced() == null || user.getIsAdvanced() != 1) {
            throw new BusinessException("当前账号尚未通过资质认证，无法申报活动");
        }
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
        int overlapCount = activityApplyMapper.countOverlapActivities(dto.getVenueId(), dto.getStartTime(), dto.getEndTime());
        if (overlapCount > 0) {
            log.warn("【门户-活动申报】存在时间冲突，venueId={}，start={}，end={}", dto.getVenueId(), dto.getStartTime(), dto.getEndTime());
            throw new BusinessException("当前时间段该场馆已排期其它活动，请调整时间");
        }
        ActivityApply apply = new ActivityApply();
        BeanUtils.copyProperties(dto, apply);
        apply.setVenueId(dto.getVenueId());
        apply.setApplyUserId(userId);
        apply.setApplyStatus(ActivityApplyStatusEnum.PENDING.getCode());
        apply.setAddressCache(venue.getAddress());
        apply.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        apply.setCreateTime(now);
        apply.setUpdateTime(now);
        int rows = activityApplyMapper.insert(apply);
        log.info("【门户-活动申报】写入完成，影响行数={}，生成ID={}", rows, apply.getId());
        return apply.getId();
    }

    @Override
    public PageInfo<ActivityApplyRecordVO> pageApplyRecords(Long userId, ActivityApplyPageQuery query) {
        log.info("【门户-活动申报】查询申报记录，用户ID={}，页码={}，每页={}", userId, query.getCurrent(), query.getSize());
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ActivityApplyRecordVO> list = activityApplyMapper.pageByUser(userId, query.getApplyStatus(), query.getName());
        for (ActivityApplyRecordVO item : list) {
            item.setApplyStatusText(ActivityApplyStatusEnum.fromCode(item.getApplyStatus()).getText());
        }
        return new PageInfo<>(list);
    }

    @Override
    @PortalCacheable(cacheName = "portal:activity:page")
    public PageInfo<ActivitySummaryVO> pageActivities(ActivityPortalPageQuery query) {
        log.info("【门户-分页查询活动】开始处理，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        int pageNum = query.getCurrent() != null ? query.getCurrent() : 1;
        int pageSize = query.getSize() != null ? query.getSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySummaryVO> list = activityMapper.portalPageList(query, ActivityOnlineStatusEnum.ONLINE.getCode());
        PageInfo<ActivitySummaryVO> pageInfo = new PageInfo<>(list);
        log.info("【门户-分页查询活动】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    @PortalCacheable(cacheName = "portal:activity:detail", ttlSeconds = 90)
    public ActivityDetailVO getDetail(Long id) {
        log.info("【门户-活动详情】开始处理，活动ID={}", id);
        ActivityDetailVO detailVO = activityMapper.selectPortalDetail(id, ActivityOnlineStatusEnum.ONLINE.getCode());
        if (detailVO == null) {
            log.warn("【门户-活动详情】未找到活动或已下线，活动ID={}", id);
            throw new BusinessException("活动不存在或已下线");
        }
        // 中文注释：浏览量自增与展示解耦，不影响主流程
        activityMapper.incrementViewCount(id);
        return detailVO;
    }
}
