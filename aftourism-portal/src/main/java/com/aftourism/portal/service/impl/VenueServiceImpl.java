package com.aftourism.portal.service.impl;

import com.aftourism.common.util.BeanCopyUtils;
import com.aftourism.portal.mapper.VenueMapper;
import com.aftourism.portal.pojo.Venue;
import com.aftourism.portal.service.VenueService;
import com.aftourism.portal.vo.VenueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场馆服务实现。
 */
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueMapper venueMapper;

    @Override
    public List<VenueVO> listVenues() {
        List<Venue> venues = venueMapper.selectAll();
        return BeanCopyUtils.copyList(venues, VenueVO::new);
    }
}
