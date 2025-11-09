package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 Mapper。
 */
@Mapper
public interface UserMapper {

    void insert(User user);

    User findByUsername(@Param("username") String username);

    User findById(@Param("id") Long id);
}
