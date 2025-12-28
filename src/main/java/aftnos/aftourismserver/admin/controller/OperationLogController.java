package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.OperationLogPageQuery;
import aftnos.aftourismserver.admin.service.OperationLogService;
import aftnos.aftourismserver.admin.vo.OperationLogVO;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.vo.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志查询接口。
 */
@RestController
@RequestMapping("/admin/system/backend")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 分页查询操作日志。
     */
    @GetMapping("/operation-logs")
    public Result<PageResponse<OperationLogVO>> page(@Valid OperationLogPageQuery query) {
        return Result.success(operationLogService.page(query));
    }
}
