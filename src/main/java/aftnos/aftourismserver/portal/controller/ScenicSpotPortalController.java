package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.dto.ScenicSpotPortalPageQuery;
import aftnos.aftourismserver.portal.service.ScenicSpotPortalService;
import aftnos.aftourismserver.portal.vo.ScenicSpotDetailVO;
import aftnos.aftourismserver.portal.vo.ScenicSpotSummaryVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户景区接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/scenic")
public class ScenicSpotPortalController {

    private final ScenicSpotPortalService scenicSpotPortalService;

    /**
     * 门户分页查询景区
     */
    @GetMapping("/page")
    public Result<PageInfo<ScenicSpotSummaryVO>> page(@Valid ScenicSpotPortalPageQuery query) {
        log.info("【门户-分页查询景区】收到请求，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageInfo<ScenicSpotSummaryVO> pageInfo = scenicSpotPortalService.pageScenicSpots(query);
        return Result.success(pageInfo);
    }

    /**
     * 门户获取景区详情
     */
    @GetMapping("/{id}")
    public Result<ScenicSpotDetailVO> detail(@PathVariable Long id) {
        log.info("【门户-景区详情】收到请求，景区ID={}", id);
        ScenicSpotDetailVO detail = scenicSpotPortalService.getDetail(id);
        return Result.success(detail);
    }
}
