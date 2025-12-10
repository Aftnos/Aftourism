package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.RecycleQueryDTO;
import aftnos.aftourismserver.admin.enums.RecycleType;
import aftnos.aftourismserver.admin.service.RecycleBinService;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/recycle")
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 分页查询回收站数据
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).RECYCLE_READ)")
    public Result<PageInfo<RecycleItemVO>> page(@Valid RecycleQueryDTO dto) {
        RecycleType type = dto.getType();
        Object typeLog = type == null ? "ALL" : type;
        log.info("【回收站-分页查询】收到请求，类型={}，页码={}，每页条数={}", typeLog, dto.getCurrent(), dto.getSize());
        PageInfo<RecycleItemVO> pageInfo = recycleBinService.pageDeletedItems(dto);
        return Result.success(pageInfo);
    }

    /**
     * 恢复指定记录
     */
    @PutMapping("/{type}/{id}/restore")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).RECYCLE_RESTORE)")
    public Result<Void> restore(@PathVariable RecycleType type, @PathVariable Long id) {
        log.info("【回收站-恢复】收到请求，类型={}，ID={}", type, id);
        recycleBinService.restoreItem(type, id);
        return Result.success();
    }

    /**
     * 彻底删除指定记录
     */
    @DeleteMapping("/{type}/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).RECYCLE_DELETE)")
    public Result<Void> forceDelete(@PathVariable RecycleType type, @PathVariable Long id) {
        log.info("【回收站-彻底删除】收到请求，类型={}，ID={}", type, id);
        recycleBinService.forceDeleteItem(type, id);
        return Result.success();
    }
}
