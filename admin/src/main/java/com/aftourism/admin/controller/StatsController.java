package com.aftourism.admin.controller;

import com.aftourism.admin.service.StatsService;
import com.aftourism.common.response.ApiResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes performance indicators to the admin UI.
 */
@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ApiResponse<Map<String, Object>> stats() {
        return ApiResponse.success(statsService.currentStats());
    }
}
