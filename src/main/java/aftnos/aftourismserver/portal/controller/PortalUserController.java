package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.service.PortalUserProfileService;
import aftnos.aftourismserver.portal.vo.PortalUserProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户用户主页接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/user")
public class PortalUserController {

    private final PortalUserProfileService portalUserProfileService;

    /**
     * 查询用户主页信息
     */
    @GetMapping("/{id}/profile")
    public Result<PortalUserProfileVO> profile(@PathVariable("id") Long id) {
        log.info("【门户-用户主页】查询用户主页，用户ID={}", id);
        PortalUserProfileVO vo = portalUserProfileService.getProfile(id);
        return Result.success(vo);
    }
}
