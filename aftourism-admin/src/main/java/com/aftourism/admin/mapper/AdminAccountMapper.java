package com.aftourism.admin.mapper;

import com.aftourism.admin.pojo.AdminAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员数据访问层。
 */
@Mapper
public interface AdminAccountMapper {

    /**
     * 根据账号查询管理员。
     */
    AdminAccount findByUsername(@Param("username") String username);
}
