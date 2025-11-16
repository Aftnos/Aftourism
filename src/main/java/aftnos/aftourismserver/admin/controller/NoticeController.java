package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.NoticeDTO;
import aftnos.aftourismserver.admin.dto.NoticePageQuery;
import aftnos.aftourismserver.admin.service.NoticeService;
import aftnos.aftourismserver.admin.vo.NoticeVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 通知公告后台管理接口
 * 新增@PreAuthorize注解用来管理角色权限
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 新增通知公告
     */
    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NOTICE_CREATE)")
    public Result<Long> create(@Valid @RequestBody NoticeDTO noticeDTO) {
        log.info("【新增通知】收到请求，标题={}", noticeDTO.getTitle());
        Long noticeId = noticeService.createNotice(noticeDTO);
        return Result.success(noticeId);
    }

    /**
     * 修改通知公告
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NOTICE_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody NoticeDTO noticeDTO) {
        log.info("【修改通知】收到请求，通知ID={}", id);
        noticeService.updateNotice(id, noticeDTO);
        return Result.success();
    }

    /**
     * 逻辑删除通知公告
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NOTICE_DELETE)")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【删除通知】收到请求，通知ID={}", id);
        noticeService.deleteNotice(id);
        return Result.success();
    }

    /**
     * 分页查询通知公告
     * 示例：GET /admin/notice/page?pageNum=1&pageSize=10
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NOTICE_READ)")
    public Result<PageInfo<NoticeVO>> page(@Valid NoticePageQuery query) {
        log.info("【分页查询通知】收到请求，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageInfo<NoticeVO> pageInfo = noticeService.pageNotices(query);
        return Result.success(pageInfo);
    }
}
