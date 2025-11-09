package com.aftourism.portal.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.portal.service.ScenicService;
import com.aftourism.portal.vo.ScenicSpotVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 景区接口。
 */
@RestController
@RequestMapping("/portal/scenic")
@RequiredArgsConstructor
public class ScenicController {

    private final ScenicService scenicService;

    @GetMapping
    public ResponseResult<List<ScenicSpotVO>> listScenicSpots() {
        return ResponseResult.ok(scenicService.listScenicSpots());
    }
}
