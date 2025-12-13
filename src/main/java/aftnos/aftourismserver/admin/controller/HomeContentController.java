package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.HomeContentSaveDTO;
import aftnos.aftourismserver.admin.service.HomeContentService;
import aftnos.aftourismserver.admin.vo.HomeContentAdminVO;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.AdminPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台首页内容管理接口。
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/home")
public class HomeContentController {

    private final HomeContentService homeContentService;

    /**
     * 查询首页轮播及文旅简介配置。
     */
    @GetMapping("/content")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).HOME_CONTENT_READ)")
    public Result<HomeContentAdminVO> queryContent() {
        log.info("【后台-首页配置】查询当前展示内容");
        HomeContentAdminVO vo = homeContentService.queryContent();
        return Result.success(vo);
    }

    /**
     * 保存首页配置。
     */
    @PutMapping("/content")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).HOME_CONTENT_UPDATE)")
    public Result<Void> saveContent(@Valid @RequestBody HomeContentSaveDTO dto) {
        log.info("【后台-首页配置】收到保存请求，标题={}，轮播数量={}", dto.getTitle(), dto.getBanners() == null ? 0 : dto.getBanners().size());
        homeContentService.saveContent(dto);
        return Result.success();
    }
}
