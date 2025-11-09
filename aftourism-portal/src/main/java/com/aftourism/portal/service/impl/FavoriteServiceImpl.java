package com.aftourism.portal.service.impl;

import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import com.aftourism.portal.dto.FavoriteRequest;
import com.aftourism.portal.mapper.UserFavoriteMapper;
import com.aftourism.portal.pojo.UserFavorite;
import com.aftourism.portal.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 收藏服务实现。
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final UserFavoriteMapper userFavoriteMapper;

    @Override
    public void addFavorite(Long userId, FavoriteRequest request) {
        UserFavorite exists = userFavoriteMapper.findOne(userId, request.getTargetType(), request.getTargetId());
        if (exists != null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR.getCode(), "已收藏该资源");
        }
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setTargetType(request.getTargetType());
        favorite.setTargetId(request.getTargetId());
        userFavoriteMapper.insert(favorite);
    }

    @Override
    public void cancelFavorite(Long userId, FavoriteRequest request) {
        UserFavorite exists = userFavoriteMapper.findOne(userId, request.getTargetType(), request.getTargetId());
        if (exists == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "收藏记录不存在");
        }
        userFavoriteMapper.delete(exists.getId());
    }
}
