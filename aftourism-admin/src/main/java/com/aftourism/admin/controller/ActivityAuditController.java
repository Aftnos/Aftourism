package com.aftourism.admin.controller;

import com.aftourism.admin.dto.ActivityAuditRequest;
import com.aftourism.admin.pojo.Activity;
import com.aftourism.admin.service.ActivityAuditService;
import com.aftourism.common.pojo.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动审核接口。
 */
@RestController
@RequestMapping("/admin/activity")
@RequiredArgsConstructor
public class ActivityAuditController {

    private final ActivityAuditService activityAuditService;

    /** 待审核活动列表 */
    @GetMapping("/pending")
    public ResponseResult<List<Activity>> listPendingActivities() {
        return ResponseResult.ok(activityAuditService.listPendingActivities());
    }

    /** 审核活动 */
    @PostMapping("/audit")
    public ResponseResult<Void> audit(@Valid @RequestBody ActivityAuditRequest request) {
        activityAuditService.audit(request);
        return ResponseResult.ok();
    }
}
