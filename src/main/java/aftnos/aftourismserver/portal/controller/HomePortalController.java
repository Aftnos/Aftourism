package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.service.HomePortalService;
import aftnos.aftourismserver.portal.vo.HomePortalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户首页展示接口。
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/home")
public class HomePortalController {

    private final HomePortalService homePortalService;

    /**
     * 首页聚合数据接口：轮播图 + 文旅简介。
     */
    @GetMapping("/content")
    public Result<HomePortalVO> loadHomeContent() {
        log.info("【门户-首页】收到查询请求，返回轮播与简介");
        HomePortalVO vo = homePortalService.loadHomeContent();
        return Result.success(vo);
    }
}
