package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ActivityManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityManagePageQuery;
import aftnos.aftourismserver.admin.enums.ActivityOnlineStatusEnum;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.service.ActivityManageService;
import aftnos.aftourismserver.admin.vo.ActivityManageDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityManageVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动管理业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityManageServiceImpl implements ActivityManageService {

    private final ActivityMapper activityMapper;

    @Override
    public PageInfo<ActivityManageVO> page(ActivityManagePageQuery query) {
        int pageNum = query.getPageNum() == null ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null ? 10 : query.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityManageVO> list = activityMapper.adminManagePageList(query);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ActivityManageDTO dto) {
        Activity entity = buildActivity(dto);
        LocalDateTime now = LocalDateTime.now();
        entity.setId(null);
        entity.setIsDeleted(0);
        entity.setViewCount(0L);
        entity.setFavoriteCount(0L);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        if (entity.getOnlineStatus() == null) {
            entity.setOnlineStatus(ActivityOnlineStatusEnum.OFFLINE.getCode());
        }
        activityMapper.insert(entity);
        log.info("【后台-活动管理】新增活动成功，活动ID={}，名称={}", entity.getId(), entity.getName());
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ActivityManageDTO dto) {
        Activity existed = requireActivity(id);
        Activity update = buildActivity(dto);
        update.setId(existed.getId());
        update.setUpdateTime(LocalDateTime.now());
        activityMapper.update(update);
        log.info("【后台-活动管理】编辑活动成功，活动ID={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireActivity(id);
        Activity update = new Activity();
        update.setId(id);
        update.setIsDeleted(1);
        update.setUpdateTime(LocalDateTime.now());
        activityMapper.update(update);
        log.info("【后台-活动管理】已删除活动，活动ID={}", id);
    }

    @Override
    public ActivityManageDetailVO detail(Long id) {
        ActivityManageDetailVO detail = activityMapper.selectManageDetail(id);
        if (detail == null) {
            throw new BusinessException("活动不存在或已被删除");
        }
        return detail;
    }

    private Activity buildActivity(ActivityManageDTO dto) {
        Activity activity = new Activity();
        activity.setName(dto.getName());
        activity.setCoverUrl(dto.getCoverUrl());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setCategory(dto.getCategory());
        activity.setVenueId(dto.getVenueId());
        activity.setOrganizer(dto.getOrganizer());
        activity.setContactPhone(dto.getContactPhone());
        activity.setIntro(dto.getIntro());
        activity.setAddressCache(dto.getAddressCache());
        activity.setOnlineStatus(dto.getOnlineStatus());
        if (dto.getOnlineStatus() != null) {
            if (dto.getOnlineStatus() != ActivityOnlineStatusEnum.OFFLINE.getCode()
                    && dto.getOnlineStatus() != ActivityOnlineStatusEnum.ONLINE.getCode()) {
                throw new BusinessException("上线状态不合法");
            }
        }
        return activity;
    }

    private Activity requireActivity(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null || (activity.getIsDeleted() != null && activity.getIsDeleted() == 1)) {
            throw new BusinessException("活动不存在或已被删除");
        }
        return activity;
    }
}
