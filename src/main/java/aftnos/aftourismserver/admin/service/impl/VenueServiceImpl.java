package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.VenueDTO;
import aftnos.aftourismserver.admin.dto.VenuePageQuery;
import aftnos.aftourismserver.admin.mapper.VenueMapper;
import aftnos.aftourismserver.admin.pojo.Venue;
import aftnos.aftourismserver.admin.service.VenueService;
import aftnos.aftourismserver.admin.vo.VenueVO;
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
 * 场馆后台业务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueMapper venueMapper;

    @Override
    public Long createVenue(VenueDTO venueDTO) {
        log.info("【新增场馆】开始处理，场馆名称={}", venueDTO.getName());
        Venue venue = new Venue();
        BeanUtils.copyProperties(venueDTO, venue);
        LocalDateTime now = LocalDateTime.now();
        venue.setIsDeleted(0);
        venue.setCreateTime(now);
        venue.setUpdateTime(now);
        int rows = venueMapper.insert(venue);
        log.info("【新增场馆】写入完成，影响行数={}，生成ID={}", rows, venue.getId());
        return venue.getId();
    }

    @Override
    public void updateVenue(Long id, VenueDTO venueDTO) {
        log.info("【修改场馆】开始处理，场馆ID={}", id);
        Venue dbVenue = venueMapper.selectById(id);
        if (dbVenue == null) {
            log.warn("【修改场馆】场馆不存在或已删除，场馆ID={}", id);
            throw new BusinessException("场馆不存在或已被删除");
        }
        Venue updateEntity = new Venue();
        BeanUtils.copyProperties(venueDTO, updateEntity);
        updateEntity.setId(id);
        updateEntity.setUpdateTime(LocalDateTime.now());
        int rows = venueMapper.update(updateEntity);
        if (rows == 0) {
            log.warn("【修改场馆】更新失败，场馆ID={}", id);
            throw new BusinessException("场馆更新失败，请稍后重试");
        }
        log.info("【修改场馆】处理完成，影响行数={}，场馆ID={}", rows, id);
    }

    @Override
    public void deleteVenue(Long id) {
        log.info("【删除场馆】开始处理，场馆ID={}", id);
        Venue dbVenue = venueMapper.selectById(id);
        if (dbVenue == null) {
            log.warn("【删除场馆】场馆不存在或已删除，场馆ID={}", id);
            throw new BusinessException("场馆不存在或已被删除");
        }
        int rows = venueMapper.logicalDelete(id, LocalDateTime.now());
        if (rows == 0) {
            log.warn("【删除场馆】删除失败，场馆ID={}", id);
            throw new BusinessException("场馆删除失败，请稍后重试");
        }
        log.info("【删除场馆】处理完成，影响行数={}，场馆ID={}", rows, id);
    }

    @Override
    public PageInfo<VenueVO> pageVenue(VenuePageQuery query) {
        log.info("【分页查询场馆】开始处理，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        int pageNum = query.getCurrent() != null ? query.getCurrent() : 1;
        int pageSize = query.getSize() != null ? query.getSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<VenueVO> list = venueMapper.pageList(query);
        PageInfo<VenueVO> pageInfo = new PageInfo<>(list);
        log.info("【分页查询场馆】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public VenueVO getVenueDetail(Long id) {
        log.info("【查询场馆详情】开始处理，场馆ID={}", id);
        VenueVO detail = venueMapper.selectDetail(id);
        if (detail == null) {
            log.warn("【查询场馆详情】场馆不存在或已删除，场馆ID={}", id);
            throw new BusinessException("场馆不存在或已被删除");
        }
        return detail;
    }
}
