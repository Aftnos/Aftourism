package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.PortalNotificationPageQuery;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import aftnos.aftourismserver.portal.vo.PortalNotificationVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户通知接口
 */
@Slf4j
@RestController
@RequestMapping("/portal/notification")
@RequiredArgsConstructor
public class PortalNotificationController {

    private final PortalNotificationService portalNotificationService;

    @GetMapping("/page")
    public Result<PageInfo<PortalNotificationVO>> page(@Valid PortalNotificationPageQuery query) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        PageInfo<PortalNotificationVO> pageInfo = portalNotificationService.pageNotifications(userId, query);
        return Result.success(pageInfo);
    }

    @PutMapping("/{id}/read")
    public Result<Void> read(@PathVariable("id") Long id) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        portalNotificationService.markRead(userId, id);
        return Result.success();
    }

    @PutMapping("/read-all")
    public Result<Void> readAll() {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        portalNotificationService.markAllRead(userId);
        return Result.success();
    }
}
