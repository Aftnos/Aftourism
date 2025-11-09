package com.aftourism.portal.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.portal.service.VenueService;
import com.aftourism.portal.vo.VenueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 场馆接口。
 */
@RestController
@RequestMapping("/portal/venue")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public ResponseResult<List<VenueVO>> listVenues() {
        return ResponseResult.ok(venueService.listVenues());
    }
}
