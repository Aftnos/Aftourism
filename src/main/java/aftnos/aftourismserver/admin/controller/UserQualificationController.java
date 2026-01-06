package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.QualificationAuditRequest;
import aftnos.aftourismserver.admin.dto.QualificationPageQuery;
import aftnos.aftourismserver.admin.service.UserQualificationManageService;
import aftnos.aftourismserver.admin.vo.QualificationApplyManageVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端资质审核接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/qualification")
public class UserQualificationController {

    private final UserQualificationManageService qualificationManageService;

    /**
     * 资质申请分页查询。
     */
    @GetMapping("/page")
    public Result<PageInfo<QualificationApplyManageVO>> page(@Valid QualificationPageQuery query) {
        PageInfo<QualificationApplyManageVO> pageInfo = qualificationManageService.page(query);
        return Result.success(pageInfo, "请求成功");
    }

    /**
     * 资质申请详情。
     */
    @GetMapping("/{id}")
    public Result<QualificationApplyManageVO> detail(@PathVariable("id") Long id) {
        QualificationApplyManageVO detail = qualificationManageService.detail(id);
        return Result.success(detail, "请求成功");
    }

    /**
     * 通过资质申请。
     */
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable("id") Long id,
                                @RequestBody(required = false) QualificationAuditRequest request) {
        log.info("【管理端-资质审核】通过申请，applyId={}", id);
        qualificationManageService.approve(id, request != null ? request.getAuditRemark() : null);
        return Result.success(null, "审核已通过");
    }

    /**
     * 驳回资质申请。
     */
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable("id") Long id,
                               @Valid @RequestBody QualificationAuditRequest request) {
        log.info("【管理端-资质审核】驳回申请，applyId={}", id);
        qualificationManageService.reject(id, request.getAuditRemark());
        return Result.success(null, "审核已驳回");
    }
}
