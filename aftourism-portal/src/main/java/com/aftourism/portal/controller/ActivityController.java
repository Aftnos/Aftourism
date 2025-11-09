package com.aftourism.portal.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.portal.dto.ActivityApplyRequest;
import com.aftourism.portal.dto.CommentRequest;
import com.aftourism.portal.service.ActivityPortalService;
import com.aftourism.portal.vo.ActivityDetailVO;
import com.aftourism.portal.vo.ActivityVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动接口。
 */
@RestController
@RequestMapping("/portal/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityPortalService activityPortalService;

    /** 活动列表 */
    @GetMapping
    public ResponseResult<List<ActivityVO>> listActivities() {
        return ResponseResult.ok(activityPortalService.listActivities());
    }

    /** 活动详情 */
    @GetMapping("/{id}")
    public ResponseResult<ActivityDetailVO> detail(@PathVariable Long id) {
        return ResponseResult.ok(activityPortalService.getDetail(id));
    }

    /** 活动申报 */
    @PostMapping
    public ResponseResult<Long> apply(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody ActivityApplyRequest request) {
        return ResponseResult.ok(activityPortalService.apply(request, userId));
    }

    /** 活动留言 */
    @PostMapping("/comment")
    public ResponseResult<Long> comment(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody CommentRequest request) {
        return ResponseResult.ok(activityPortalService.comment(request, userId));
    }
}
