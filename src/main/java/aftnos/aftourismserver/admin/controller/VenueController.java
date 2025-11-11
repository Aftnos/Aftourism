package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.VenueDTO;
import aftnos.aftourismserver.admin.dto.VenuePageQuery;
import aftnos.aftourismserver.admin.service.VenueService;
import aftnos.aftourismserver.admin.vo.VenueVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 场馆后台接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/venue")
public class VenueController {

    private final VenueService venueService;

    /**
     * 新增场馆
     */
    @PostMapping
    public Result<Long> create(@Valid @RequestBody VenueDTO venueDTO) {
        log.info("【后台-新增场馆】收到请求，名称={}", venueDTO.getName());
        Long id = venueService.createVenue(venueDTO);
        return Result.success(id);
    }

    /**
     * 修改场馆
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody VenueDTO venueDTO) {
        log.info("【后台-修改场馆】收到请求，场馆ID={}", id);
        venueService.updateVenue(id, venueDTO);
        return Result.success();
    }

    /**
     * 删除场馆
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【后台-删除场馆】收到请求，场馆ID={}", id);
        venueService.deleteVenue(id);
        return Result.success();
    }

    /**
     * 分页查询场馆
     */
    @GetMapping("/page")
    public Result<PageInfo<VenueVO>> page(@Valid VenuePageQuery query) {
        log.info("【后台-分页查询场馆】收到请求，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageInfo<VenueVO> pageInfo = venueService.pageVenue(query);
        return Result.success(pageInfo);
    }

    /**
     * 查询场馆详情
     */
    @GetMapping("/{id}")
    public Result<VenueVO> detail(@PathVariable Long id) {
        log.info("【后台-场馆详情】收到请求，场馆ID={}", id);
        VenueVO detail = venueService.getVenueDetail(id);
        return Result.success(detail);
    }
}
