package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ActivityAuditPageQuery;
import aftnos.aftourismserver.admin.enums.ActivityApplyStatusEnum;
import aftnos.aftourismserver.admin.enums.ActivityOnlineStatusEnum;
import aftnos.aftourismserver.admin.mapper.ActivityApplyMapper;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.pojo.ActivityApply;
import aftnos.aftourismserver.admin.service.ActivityService;
import aftnos.aftourismserver.admin.vo.ActivityAuditDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityAuditItemVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动后台业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityApplyMapper activityApplyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long id) {
        log.info("【后台-活动审核通过】开始处理，申报ID={}", id);
        ActivityApply apply = requireApply(id);
        if (ActivityApplyStatusEnum.APPROVED.getCode() == apply.getApplyStatus()) {
            throw new BusinessException("活动已审核通过，无需重复操作");
        }
        LocalDateTime now = LocalDateTime.now();
        Activity activityEntity = buildActivityFromApply(apply);
        activityEntity.setUpdateTime(now);
        Long activityId = apply.getActivityId();
        if (activityId == null) {
            activityEntity.setViewCount(0L);
            activityEntity.setFavoriteCount(0L);
            activityEntity.setIsDeleted(0);
            activityEntity.setCreateTime(now);
            activityMapper.insert(activityEntity);
            activityId = activityEntity.getId();
        } else {
            activityEntity.setId(activityId);
            activityMapper.update(activityEntity);
        }
        ActivityApply update = new ActivityApply();
        update.setId(id);
        update.setApplyStatus(ActivityApplyStatusEnum.APPROVED.getCode());
        update.setRejectReason(null);
        update.setActivityId(activityId);
        update.setUpdateTime(now);
        activityApplyMapper.update(update);
        log.info("【后台-活动审核通过】处理完成，申报ID={}，活动ID={}", id, activityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id, String reason) {
        log.info("【后台-活动审核驳回】开始处理，申报ID={}，原因={}", id, reason);
        ActivityApply apply = requireApply(id);
        if (ActivityApplyStatusEnum.REJECTED.getCode() == apply.getApplyStatus()) {
            throw new BusinessException("活动已被驳回，无需重复操作");
        }
        LocalDateTime now = LocalDateTime.now();
        ActivityApply update = new ActivityApply();
        update.setId(id);
        update.setApplyStatus(ActivityApplyStatusEnum.REJECTED.getCode());
        update.setRejectReason(reason);
        update.setUpdateTime(now);
        activityApplyMapper.update(update);
        if (apply.getActivityId() != null) {
            Activity offline = new Activity();
            offline.setId(apply.getActivityId());
            offline.setOnlineStatus(ActivityOnlineStatusEnum.OFFLINE.getCode());
            offline.setUpdateTime(now);
            activityMapper.update(offline);
        }
        log.info("【后台-活动审核驳回】处理完成，申报ID={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void online(Long id) {
        log.info("【后台-活动上线】开始处理，申报ID={}", id);
        ActivityApply apply = requireApply(id);
        if (ActivityApplyStatusEnum.APPROVED.getCode() != apply.getApplyStatus()) {
            throw new BusinessException("仅审核通过的活动可以上线");
        }
        Activity activity = ensurePublishedActivity(apply);
        if (ActivityOnlineStatusEnum.ONLINE.getCode() == activity.getOnlineStatus()) {
            throw new BusinessException("活动已上线，无需重复操作");
        }
        Activity update = new Activity();
        update.setId(activity.getId());
        update.setOnlineStatus(ActivityOnlineStatusEnum.ONLINE.getCode());
        update.setUpdateTime(LocalDateTime.now());
        activityMapper.update(update);
        log.info("【后台-活动上线】处理完成，申报ID={}，活动ID={}", id, activity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offline(Long id) {
        log.info("【后台-活动下线】开始处理，申报ID={}", id);
        ActivityApply apply = requireApply(id);
        Activity activity = ensurePublishedActivity(apply);
        if (ActivityOnlineStatusEnum.OFFLINE.getCode() == activity.getOnlineStatus()) {
            throw new BusinessException("活动已处于下线状态");
        }
        Activity update = new Activity();
        update.setId(activity.getId());
        update.setOnlineStatus(ActivityOnlineStatusEnum.OFFLINE.getCode());
        update.setUpdateTime(LocalDateTime.now());
        activityMapper.update(update);
        log.info("【后台-活动下线】处理完成，申报ID={}，活动ID={}", id, activity.getId());
    }

    @Override
    public PageInfo<ActivityAuditItemVO> pageAudit(ActivityAuditPageQuery query) {
        log.info("【后台-活动审核分页】开始处理，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        Integer status = query.getApplyStatus();
        if (status != null && status != ActivityApplyStatusEnum.PENDING.getCode()
                && status != ActivityApplyStatusEnum.APPROVED.getCode()
                && status != ActivityApplyStatusEnum.REJECTED.getCode()) {
            throw new BusinessException("审核状态不合法");
        }
        Integer onlineStatus = query.getOnlineStatus();
        if (onlineStatus != null && onlineStatus != ActivityOnlineStatusEnum.OFFLINE.getCode()
                && onlineStatus != ActivityOnlineStatusEnum.ONLINE.getCode()) {
            throw new BusinessException("上线状态不合法");
        }
        String organizer = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AdminPrincipal admin) {
            boolean superAdmin = admin.isSuperAdmin();
            if (!superAdmin && (status == null || status == ActivityApplyStatusEnum.PENDING.getCode())) {
                organizer = admin.getRealName();
            }
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ActivityAuditItemVO> list = activityApplyMapper.adminAuditPageList(query, status, organizer);
        PageInfo<ActivityAuditItemVO> pageInfo = new PageInfo<>(list);
        log.info("【后台-活动审核分页】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public ActivityAuditDetailVO detail(Long id) {
        ActivityAuditDetailVO detail = activityApplyMapper.selectDetailById(id);
        if (detail == null) {
            throw new BusinessException("活动不存在或已被删除");
        }
        if (detail.getOnlineStatus() == null) {
            detail.setOnlineStatus(ActivityOnlineStatusEnum.OFFLINE.getCode());
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRemark(Long id, String remark) {
        ActivityApply apply = requireApply(id);
        ActivityApply update = new ActivityApply();
        update.setId(apply.getId());
        update.setAuditRemark(remark);
        update.setUpdateTime(LocalDateTime.now());
        activityApplyMapper.update(update);
        log.info("【后台-活动审核备注】更新完成，申报ID={}", id);
    }

    private ActivityApply requireApply(Long id) {
        ActivityApply apply = activityApplyMapper.selectById(id);
        if (apply == null || (apply.getIsDeleted() != null && apply.getIsDeleted() == 1)) {
            log.warn("【活动申报校验】申报记录不存在或已删除，申报ID={}", id);
            throw new BusinessException("活动不存在或已被删除");
        }
        return apply;
    }

    private Activity buildActivityFromApply(ActivityApply apply) {
        Activity activity = new Activity();
        activity.setName(apply.getName());
        activity.setCoverUrl(apply.getCoverUrl());
        activity.setStartTime(apply.getStartTime());
        activity.setEndTime(apply.getEndTime());
        activity.setCategory(apply.getCategory());
        activity.setVenueId(apply.getVenueId());
        activity.setOrganizer(apply.getOrganizer());
        activity.setContactPhone(apply.getContactPhone());
        activity.setIntro(apply.getIntro());
        activity.setAddressCache(apply.getAddressCache());
        activity.setOnlineStatus(ActivityOnlineStatusEnum.ONLINE.getCode());
        return activity;
    }

    private Activity ensurePublishedActivity(ActivityApply apply) {
        Long activityId = apply.getActivityId();
        if (activityId == null) {
            throw new BusinessException("活动尚未发布，无法进行此操作");
        }
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || (activity.getIsDeleted() != null && activity.getIsDeleted() == 1)) {
            throw new BusinessException("特色活动不存在或已删除");
        }
        return activity;
    }
}
