package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员表操作 Mapper。
 */
@Mapper
public interface AdminMapper {

    /**
     * 根据登录账号查询管理员。
     *
     * @param username 登录账号
     * @return 管理员实体
     */
    Admin findByUsername(@Param("username") String username);

    /**
     * 根据主键查询管理员。
     *
     * @param id 主键ID
     * @return 管理员实体
     */
    Admin findById(@Param("id") Long id);
}
