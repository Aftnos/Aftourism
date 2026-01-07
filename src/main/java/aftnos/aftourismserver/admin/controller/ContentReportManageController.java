package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ContentReportManageDTO;
import aftnos.aftourismserver.admin.dto.ContentReportManagePageQuery;
import aftnos.aftourismserver.admin.service.ContentReportManageService;
import aftnos.aftourismserver.admin.vo.ContentReportManageVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 举报管理接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/report/manage")
public class ContentReportManageController {

    private final ContentReportManageService contentReportManageService;

    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).CONTENT_REPORT_MANAGE)")
    public Result<PageInfo<ContentReportManageVO>> page(@Valid ContentReportManagePageQuery query) {
        PageInfo<ContentReportManageVO> pageInfo = contentReportManageService.page(query);
        return Result.success(pageInfo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).CONTENT_REPORT_MANAGE)")
    public Result<Void> update(@PathVariable("id") Long id,
                               @Valid @RequestBody ContentReportManageDTO dto) {
        contentReportManageService.update(id, dto);
        return Result.success();
    }
}
