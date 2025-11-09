package com.aftourism.admin.service.impl;

import com.aftourism.admin.dto.ActivityAuditRequest;
import com.aftourism.admin.mapper.ActivityMapper;
import com.aftourism.admin.pojo.Activity;
import com.aftourism.admin.service.ActivityAuditService;
import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动审核服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityAuditServiceImpl implements ActivityAuditService {

    private final ActivityMapper activityMapper;

    @Override
    public List<Activity> listPendingActivities() {
        return activityMapper.selectPendingList();
    }

    @Override
    public void audit(ActivityAuditRequest request) {
        Activity activity = new Activity();
        activity.setId(request.getActivityId());
        activity.setApplyStatus(request.getApplyStatus());
        activity.setRejectReason(request.getRejectReason());
        int status = request.getApplyStatus();
        if (status < 0 || status > 2) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "审核状态不合法");
        }
        activityMapper.updateStatus(activity);
        log.info("活动:{} 审核完成，状态:{}", request.getActivityId(), request.getApplyStatus());
    }
}
