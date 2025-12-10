package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ScenicSpotDTO;
import aftnos.aftourismserver.admin.dto.ScenicSpotPageQuery;
import aftnos.aftourismserver.admin.mapper.ScenicSpotMapper;
import aftnos.aftourismserver.admin.pojo.ScenicSpot;
import aftnos.aftourismserver.admin.service.ScenicSpotService;
import aftnos.aftourismserver.admin.vo.ScenicSpotVO;
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
 * 景区后台业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScenicSpotServiceImpl implements ScenicSpotService {

    private final ScenicSpotMapper scenicSpotMapper;

    @Override
    public Long createScenicSpot(ScenicSpotDTO scenicSpotDTO) {
        log.info("【新增景区】开始处理，名称={}", scenicSpotDTO.getName());
        ScenicSpot scenicSpot = new ScenicSpot();
        BeanUtils.copyProperties(scenicSpotDTO, scenicSpot);
        LocalDateTime now = LocalDateTime.now();
        scenicSpot.setIsDeleted(0);
        scenicSpot.setCreateTime(now);
        scenicSpot.setUpdateTime(now);
        int rows = scenicSpotMapper.insert(scenicSpot);
        log.info("【新增景区】写入完成，影响行数={}，主键ID={}", rows, scenicSpot.getId());
        return scenicSpot.getId();
    }

    @Override
    public void updateScenicSpot(Long id, ScenicSpotDTO scenicSpotDTO) {
        log.info("【修改景区】开始处理，景区ID={}", id);
        ScenicSpot dbScenic = scenicSpotMapper.selectById(id);
        if (dbScenic == null) {
            log.warn("【修改景区】目标景区不存在或已删除，景区ID={}", id);
            throw new BusinessException("景区不存在或已被删除");
        }
        ScenicSpot updateEntity = new ScenicSpot();
        BeanUtils.copyProperties(scenicSpotDTO, updateEntity);
        updateEntity.setId(id);
        updateEntity.setUpdateTime(LocalDateTime.now());
        int rows = scenicSpotMapper.update(updateEntity);
        if (rows == 0) {
            log.warn("【修改景区】更新失败，景区ID={}", id);
            throw new BusinessException("景区更新失败，请稍后重试");
        }
        log.info("【修改景区】处理完成，影响行数={}，景区ID={}", rows, id);
    }

    @Override
    public void deleteScenicSpot(Long id) {
        log.info("【删除景区】开始处理，景区ID={}", id);
        ScenicSpot dbScenic = scenicSpotMapper.selectById(id);
        if (dbScenic == null) {
            log.warn("【删除景区】目标景区不存在或已删除，景区ID={}", id);
            throw new BusinessException("景区不存在或已被删除");
        }
        int rows = scenicSpotMapper.logicalDelete(id, LocalDateTime.now());
        if (rows == 0) {
            log.warn("【删除景区】删除失败，景区ID={}", id);
            throw new BusinessException("景区删除失败，请稍后重试");
        }
        log.info("【删除景区】处理完成，影响行数={}，景区ID={}", rows, id);
    }

    @Override
    public PageInfo<ScenicSpotVO> pageScenicSpot(ScenicSpotPageQuery query) {
        log.info("【分页查询景区】开始处理，页码={}，每页条数={}", query.getCurrent(), query.getSize());

        // 设置默认分页参数，防止空指针异常
        int pageNum = (query.getCurrent() != null) ? query.getCurrent() : 1;
        int pageSize = (query.getSize() != null) ? query.getSize() : 10;

        PageHelper.startPage(pageNum, pageSize);
        List<ScenicSpotVO> list = scenicSpotMapper.pageList(query);
        PageInfo<ScenicSpotVO> pageInfo = new PageInfo<>(list);
        log.info("【分页查询景区】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public ScenicSpotVO getScenicSpotDetail(Long id) {
        log.info("【查询景区详情】开始处理，景区ID={}", id);
        ScenicSpotVO detail = scenicSpotMapper.selectDetail(id);
        if (detail == null) {
            log.warn("【查询景区详情】目标景区不存在或已删除，景区ID={}", id);
            throw new BusinessException("景区不存在或已被删除");
        }
        return detail;
    }
}
