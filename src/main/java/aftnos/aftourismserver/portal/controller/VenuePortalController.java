package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.dto.VenuePortalPageQuery;
import aftnos.aftourismserver.portal.service.VenuePortalService;
import aftnos.aftourismserver.portal.vo.VenueDetailVO;
import aftnos.aftourismserver.portal.vo.VenueSummaryVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户场馆接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/venue")
public class VenuePortalController {

    private final VenuePortalService venuePortalService;

    /**
     * 门户分页查询场馆
     */
    @GetMapping("/page")
    public Result<PageInfo<VenueSummaryVO>> page(@Valid VenuePortalPageQuery query) {
        log.info("【门户-分页查询场馆】收到请求，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        PageInfo<VenueSummaryVO> pageInfo = venuePortalService.pageVenues(query);
        return Result.success(pageInfo);
    }

    /**
     * 门户查看场馆详情
     */
    @GetMapping("/{id}")
    public Result<VenueDetailVO> detail(@PathVariable Long id) {
        log.info("【门户-场馆详情】收到请求，场馆ID={}", id);
        VenueDetailVO detail = venuePortalService.getDetail(id);
        return Result.success(detail);
    }
}
