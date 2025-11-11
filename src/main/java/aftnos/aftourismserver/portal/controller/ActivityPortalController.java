package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.interceptor.JwtAuthenticationInterceptor;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.dto.ActivityApplyDTO;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.service.ActivityPortalService;
import aftnos.aftourismserver.portal.vo.ActivitySummaryVO;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户活动接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/activity")
public class ActivityPortalController {

    private final ActivityPortalService activityPortalService;

    /**
     * 活动申报
     */
    @PostMapping("/apply")
    public Result<Long> apply(@Valid @RequestBody ActivityApplyDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(JwtAuthenticationInterceptor.ATTR_USER_ID);
        if (userId == null) {
            throw new UnauthorizedException("用户未登录或登录状态已失效");
        }
        log.info("【门户-活动申报】收到请求，用户ID={}，活动名称={}", userId, dto.getName());
        Long id = activityPortalService.apply(dto, userId);
        return Result.success(id);
    }

    /**
     * 门户分页查询活动
     */
    @GetMapping("/page")
    public Result<PageInfo<ActivitySummaryVO>> page(@Valid ActivityPortalPageQuery query) {
        log.info("【门户-分页查询活动】收到请求，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageInfo<ActivitySummaryVO> pageInfo = activityPortalService.pageActivities(query);
        return Result.success(pageInfo);
    }
}
