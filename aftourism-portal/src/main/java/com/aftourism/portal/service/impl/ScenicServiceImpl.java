package com.aftourism.portal.service.impl;

import com.aftourism.common.util.BeanCopyUtils;
import com.aftourism.portal.mapper.ScenicSpotMapper;
import com.aftourism.portal.pojo.ScenicSpot;
import com.aftourism.portal.service.ScenicService;
import com.aftourism.portal.vo.ScenicSpotVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 景区服务实现。
 */
@Service
@RequiredArgsConstructor
public class ScenicServiceImpl implements ScenicService {

    private final ScenicSpotMapper scenicSpotMapper;

    @Override
    public List<ScenicSpotVO> listScenicSpots() {
        List<ScenicSpot> spots = scenicSpotMapper.selectAll();
        return BeanCopyUtils.copyList(spots, ScenicSpotVO::new);
    }
}
