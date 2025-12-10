package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.dto.NoticePortalPageQuery;
import aftnos.aftourismserver.portal.service.NoticePortalService;
import aftnos.aftourismserver.portal.vo.NoticeDetailVO;
import aftnos.aftourismserver.portal.vo.NoticeSummaryVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户通知公告接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/notice")
public class NoticePortalController {

    private final NoticePortalService noticePortalService;

    /**
     * 门户分页查询通知公告
     */
    @GetMapping("/page")
    public Result<PageInfo<NoticeSummaryVO>> page(@Valid NoticePortalPageQuery query) {
        log.info("【门户-分页查询通知】收到请求，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        PageInfo<NoticeSummaryVO> pageInfo = noticePortalService.pageNotices(query);
        return Result.success(pageInfo);
    }

    /**
     * 查看通知公告详情
     */
    @GetMapping("/{id}")
    public Result<NoticeDetailVO> detail(@PathVariable Long id) {
        log.info("【门户-通知详情】收到请求，通知ID={}", id);
        NoticeDetailVO detail = noticePortalService.getNoticeDetail(id);
        return Result.success(detail);
    }
}
