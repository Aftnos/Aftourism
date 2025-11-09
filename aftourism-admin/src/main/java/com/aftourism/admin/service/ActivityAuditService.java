package com.aftourism.admin.service;

import com.aftourism.admin.dto.ActivityAuditRequest;
import com.aftourism.admin.pojo.Activity;

import java.util.List;

/**
 * 活动审核服务接口。
 */
public interface ActivityAuditService {

    /**
     * 待审核活动列表。
     */
    List<Activity> listPendingActivities();

    /**
     * 审核活动。
     */
    void audit(ActivityAuditRequest request);
}
