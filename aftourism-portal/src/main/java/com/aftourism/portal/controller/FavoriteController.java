package com.aftourism.portal.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.portal.dto.FavoriteRequest;
import com.aftourism.portal.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏接口。
 */
@RestController
@RequestMapping("/portal/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /** 添加收藏 */
    @PostMapping("/add")
    public ResponseResult<Void> addFavorite(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody FavoriteRequest request) {
        favoriteService.addFavorite(userId, request);
        return ResponseResult.ok();
    }

    /** 取消收藏 */
    @PostMapping("/cancel")
    public ResponseResult<Void> cancelFavorite(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody FavoriteRequest request) {
        favoriteService.cancelFavorite(userId, request);
        return ResponseResult.ok();
    }
}
