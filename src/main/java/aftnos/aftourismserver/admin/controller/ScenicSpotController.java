package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ScenicSpotDTO;
import aftnos.aftourismserver.admin.dto.ScenicSpotPageQuery;
import aftnos.aftourismserver.admin.service.ScenicSpotService;
import aftnos.aftourismserver.admin.vo.ScenicSpotVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 景区后台管理接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor//自动給final添加构造器
@RequestMapping("/admin/scenic")
public class ScenicSpotController {

    private final ScenicSpotService scenicSpotService;

    /**
     * 新增景区
     */
    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).SCENIC_CREATE)")
    public Result<Long> create(@Valid @RequestBody ScenicSpotDTO scenicSpotDTO) {
        log.info("【新增景区】收到请求，名称={}", scenicSpotDTO.getName());
        Long id = scenicSpotService.createScenicSpot(scenicSpotDTO);
        return Result.success(id);
    }

    /**
     * 修改景区
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).SCENIC_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ScenicSpotDTO scenicSpotDTO) {
        log.info("【修改景区】收到请求，景区ID={}", id);
        scenicSpotService.updateScenicSpot(id, scenicSpotDTO);
        return Result.success();
    }

    /**
     * 逻辑删除景区
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).SCENIC_DELETE)")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【删除景区】收到请求，景区ID={}", id);
        scenicSpotService.deleteScenicSpot(id);
        return Result.success();
    }

    /**
     * 分页查询景区
     * 示例：GET /admin/scenic/page?pageNum=1&pageSize=10
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).SCENIC_READ)")
    public Result<PageInfo<ScenicSpotVO>> page(@Valid ScenicSpotPageQuery query) {
        log.info("【分页查询景区】收到请求，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageInfo<ScenicSpotVO> pageInfo = scenicSpotService.pageScenicSpot(query);
        return Result.success(pageInfo);
    }

    /**
     * 查询景区详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).SCENIC_READ)")
    public Result<ScenicSpotVO> detail(@PathVariable Long id) {
        log.info("【查询景区详情】收到请求，景区ID={}", id);
        ScenicSpotVO detail = scenicSpotService.getScenicSpotDetail(id);
        return Result.success(detail);
    }
}