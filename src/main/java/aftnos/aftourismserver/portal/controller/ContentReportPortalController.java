package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.ContentReportCreateDTO;
import aftnos.aftourismserver.portal.service.ContentReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户举报接口
 */
@Slf4j
@RestController
@RequestMapping("/portal/report")
@RequiredArgsConstructor
public class ContentReportPortalController {

    private final ContentReportService contentReportService;

    @PostMapping
    public Result<Long> create(@Valid @RequestBody ContentReportCreateDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        Long id = contentReportService.createReport(userId, dto);
        return Result.success(id);
    }
}
