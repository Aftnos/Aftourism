package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.NoticeDTO;
import aftnos.aftourismserver.admin.dto.NoticePageQuery;
import aftnos.aftourismserver.admin.enums.NoticeStatusEnum;
import aftnos.aftourismserver.admin.mapper.NoticeMapper;
import aftnos.aftourismserver.admin.pojo.Notice;
import aftnos.aftourismserver.admin.service.NoticeService;
import aftnos.aftourismserver.admin.vo.NoticeVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public Long createNotice(NoticeDTO noticeDTO) {
        log.info("【新增通知】开始处理，标题={}", noticeDTO.getTitle());
        validateStatus(noticeDTO.getStatus());
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDTO, notice);
        notice.setViewCount(noticeDTO.getViewCount() != null ? noticeDTO.getViewCount() : 0L);
        notice.setIsDeleted(0);
        notice.setCreateTime(now);
        notice.setUpdateTime(now);
        int rows = noticeMapper.insert(notice);
        log.info("【新增通知】写入完成，影响行数={}，通知ID={}", rows, notice.getId());
        return notice.getId();
    }

    @Override
    public void updateNotice(Long id, NoticeDTO noticeDTO) {
        log.info("【修改通知】开始处理，通知ID={}", id);
        Notice dbNotice = noticeMapper.selectById(id);
        if (dbNotice == null) {
            log.warn("【修改通知】目标不存在或已删除，通知ID={}", id);
            throw new BusinessException("通知不存在或已被删除");
        }
        validateStatus(noticeDTO.getStatus());
        Notice updateNotice = new Notice();
        BeanUtils.copyProperties(noticeDTO, updateNotice);
        updateNotice.setId(id);
        updateNotice.setUpdateTime(LocalDateTime.now());
        int rows = noticeMapper.update(updateNotice);
        if (rows == 0) {
            log.warn("【修改通知】更新失败，通知ID={}", id);
            throw new BusinessException("通知更新失败，请稍后重试");
        }
        log.info("【修改通知】处理完成，影响行数={}，通知ID={}", rows, id);
    }

    @Override
    public void deleteNotice(Long id) {
        log.info("【删除通知】开始处理，通知ID={}", id);
        Notice dbNotice = noticeMapper.selectById(id);
        if (dbNotice == null) {
            log.warn("【删除通知】目标不存在或已删除，通知ID={}", id);
            throw new BusinessException("通知不存在或已被删除");
        }
        int rows = noticeMapper.logicalDelete(id, LocalDateTime.now());
        if (rows == 0) {
            log.warn("【删除通知】删除失败，通知ID={}", id);
            throw new BusinessException("通知删除失败，请稍后重试");
        }
        log.info("【删除通知】处理完成，影响行数={}，通知ID={}", rows, id);
    }

    @Override
    public PageInfo<NoticeVO> pageNotices(NoticePageQuery query) {
        log.info("【分页查询通知】开始处理，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<NoticeVO> list = noticeMapper.pageList(query);
        list.forEach(item -> item.setStatusText(NoticeStatusEnum.getTextByCode(item.getStatus())));
        PageInfo<NoticeVO> pageInfo = new PageInfo<>(list);
        log.info("【分页查询通知】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    /**
     * 校验通知状态是否合法
     */
    private void validateStatus(Integer status) {
        if (!NoticeStatusEnum.isValid(status)) {
            log.warn("【通知状态校验】状态值不合法: {}", status);
            throw new BusinessException("通知状态不合法");
        }
    }
}
