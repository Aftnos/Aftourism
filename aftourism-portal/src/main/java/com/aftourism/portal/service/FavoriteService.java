package com.aftourism.portal.service;

import com.aftourism.portal.dto.FavoriteRequest;

/**
 * 收藏服务。
 */
public interface FavoriteService {

    void addFavorite(Long userId, FavoriteRequest request);

    void cancelFavorite(Long userId, FavoriteRequest request);
}
