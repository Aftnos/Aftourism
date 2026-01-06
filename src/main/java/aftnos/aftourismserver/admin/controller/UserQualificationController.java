package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.QualificationAuditRequest;
import aftnos.aftourismserver.admin.service.UserQualificationManageService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/admin/qualification")
public class UserQualificationController {

    private final UserQualificationManageService qualificationManageService;

    /**
     * 通过资质申请。
     */
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable("id") Long id,
                                @Valid @RequestBody QualificationAuditRequest request) {
        log.info("【管理端-资质审核】通过申请，applyId={}", id);
        qualificationManageService.approve(id, request.getAuditRemark());
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
