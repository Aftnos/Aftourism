package com.aftourism.user.mapper;

import com.aftourism.user.entity.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper for user operations.
 */
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(username, password, role, is_deleted, create_time, update_time) "
            + "VALUES(#{username}, #{password}, #{role}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT id, username, password, role, is_deleted, create_time, update_time "
            + "FROM user WHERE username=#{username} AND is_deleted=0")
    Optional<User> findByUsername(@Param("username") String username);
}
