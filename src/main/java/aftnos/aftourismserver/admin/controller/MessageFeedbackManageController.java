package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.MessageFeedbackManageDTO;
import aftnos.aftourismserver.admin.dto.MessageFeedbackManagePageQuery;
import aftnos.aftourismserver.admin.service.MessageFeedbackManageService;
import aftnos.aftourismserver.admin.vo.MessageFeedbackManageVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 留言反馈管理接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/feedback/manage")
public class MessageFeedbackManageController {

    private final MessageFeedbackManageService messageFeedbackManageService;

    /**
     * 分页查询留言反馈
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).FEEDBACK_MANAGE)")
    public Result<PageInfo<MessageFeedbackManageVO>> page(@Valid MessageFeedbackManagePageQuery query) {
        PageInfo<MessageFeedbackManageVO> pageInfo = messageFeedbackManageService.page(query);
        return Result.success(pageInfo);
    }

    /**
     * 查询留言反馈详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).FEEDBACK_MANAGE)")
    public Result<MessageFeedbackManageVO> detail(@PathVariable("id") Long id) {
        MessageFeedbackManageVO vo = messageFeedbackManageService.detail(id);
        return Result.success(vo);
    }

    /**
     * 更新留言反馈
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).FEEDBACK_MANAGE)")
    public Result<Void> update(@PathVariable("id") Long id,
                               @Valid @RequestBody MessageFeedbackManageDTO dto) {
        messageFeedbackManageService.update(id, dto);
        return Result.success();
    }

    /**
     * 删除留言反馈
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).FEEDBACK_MANAGE)")
    public Result<Void> delete(@PathVariable("id") Long id) {
        messageFeedbackManageService.delete(id);
        return Result.success();
    }
}
