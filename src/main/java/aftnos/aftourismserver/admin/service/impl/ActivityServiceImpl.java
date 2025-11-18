package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ActivityAuditPageQuery;
import aftnos.aftourismserver.admin.enums.ActivityApplyStatusEnum;
import aftnos.aftourismserver.admin.enums.ActivityOnlineStatusEnum;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.service.ActivityService;
import aftnos.aftourismserver.admin.vo.ActivityAuditItemVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * 活动后台业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;

    @Override
    public void approve(Long id) {
        log.info("【后台-活动审核通过】开始处理，活动ID={}", id);
        Activity activity = requireActivity(id);
        if (ActivityApplyStatusEnum.APPROVED.getCode() == activity.getApplyStatus()) {
            log.warn("【后台-活动审核通过】活动已是通过状态，活动ID={}", id);
            throw new BusinessException("活动已审核通过，无需重复操作");
        }
        Activity update = new Activity();
        update.setId(id);
        update.setApplyStatus(ActivityApplyStatusEnum.APPROVED.getCode());
        // 设置为空字符串用于清理历史驳回原因，确保数据库中不再保留旧信息
        update.setRejectReason("");
        update.setUpdateTime(LocalDateTime.now());
        int rows = activityMapper.update(update);
        log.info("【后台-活动审核通过】处理完成，影响行数={}，活动ID={}", rows, id);
    }

    @Override
    public void reject(Long id, String reason) {
        log.info("【后台-活动审核驳回】开始处理，活动ID={}，原因={}", id, reason);
        Activity activity = requireActivity(id);
        if (ActivityApplyStatusEnum.REJECTED.getCode() == activity.getApplyStatus()) {
            log.warn("【后台-活动审核驳回】活动已处于驳回状态，活动ID={}", id);
            throw new BusinessException("活动已被驳回，无需重复操作");
        }
        Activity update = new Activity();
        update.setId(id);
        update.setApplyStatus(ActivityApplyStatusEnum.REJECTED.getCode());
        update.setRejectReason(reason);
        update.setOnlineStatus(ActivityOnlineStatusEnum.OFFLINE.getCode());
        update.setUpdateTime(LocalDateTime.now());
        int rows = activityMapper.update(update);
        log.info("【后台-活动审核驳回】处理完成，影响行数={}，活动ID={}", rows, id);
    }

    @Override
    public void online(Long id) {
        log.info("【后台-活动上线】开始处理，活动ID={}", id);
        Activity activity = requireActivity(id);
        if (ActivityApplyStatusEnum.APPROVED.getCode() != activity.getApplyStatus()) {
            log.warn("【后台-活动上线】活动未审核通过无法上线，活动ID={}", id);
            throw new BusinessException("仅审核通过的活动可以上线");
        }
        if (ActivityOnlineStatusEnum.ONLINE.getCode() == activity.getOnlineStatus()) {
            log.warn("【后台-活动上线】活动已上线，活动ID={}", id);
            throw new BusinessException("活动已上线，无需重复操作");
        }
        Activity update = new Activity();
        update.setId(id);
        update.setOnlineStatus(ActivityOnlineStatusEnum.ONLINE.getCode());
        update.setUpdateTime(LocalDateTime.now());
        int rows = activityMapper.update(update);
        log.info("【后台-活动上线】处理完成，影响行数={}，活动ID={}", rows, id);
    }

    @Override
    public void offline(Long id) {
        log.info("【后台-活动下线】开始处理，活动ID={}", id);
        Activity activity = requireActivity(id);
        if (ActivityOnlineStatusEnum.OFFLINE.getCode() == activity.getOnlineStatus()) {
            log.warn("【后台-活动下线】活动已下线，活动ID={}", id);
            throw new BusinessException("活动已处于下线状态");
        }
        Activity update = new Activity();
        update.setId(id);
        update.setOnlineStatus(ActivityOnlineStatusEnum.OFFLINE.getCode());
        update.setUpdateTime(LocalDateTime.now());
        int rows = activityMapper.update(update);
        log.info("【后台-活动下线】处理完成，影响行数={}，活动ID={}", rows, id);
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
        java.util.List<ActivityAuditItemVO> list = activityMapper.adminAuditPageList(query, status, organizer);
        PageInfo<ActivityAuditItemVO> pageInfo = new PageInfo<>(list);
        log.info("【后台-活动审核分页】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    /**
     * 校验活动是否存在
     */
    private Activity requireActivity(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null || activity.getIsDeleted() != null && activity.getIsDeleted() == 1) {
            log.warn("【活动校验】活动不存在或已删除，活动ID={}", id);
            throw new BusinessException("活动不存在或已被删除");
        }
        return activity;
    }
}
