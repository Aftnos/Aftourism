package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏 Mapper。
 */
@Mapper
public interface UserFavoriteMapper {

    UserFavorite findOne(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    void insert(UserFavorite favorite);

    void delete(@Param("id") Long id);
}
