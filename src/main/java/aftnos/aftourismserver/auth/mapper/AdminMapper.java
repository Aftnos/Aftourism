package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 分页条件查询管理员列表。
     *
     * @param username 用户名模糊匹配
     * @param realName 真实姓名模糊匹配
     * @param status   状态筛选
     * @return 管理员集合
     */
    List<Admin> search(@Param("username") String username,
                       @Param("realName") String realName,
                       @Param("status") Integer status);

    /**
     * 新增管理员。
     */
    int insert(Admin admin);

    /**
     * 更新管理员基础信息。
     */
    int update(Admin admin);

    /**
     * 更新管理员状态。
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据主键逻辑删除管理员。
     */
    int softDelete(@Param("id") Long id);

    /**
     * 查询管理员表中出现过的全部角色编码（逗号分隔）。
     */
    List<String> findAllRoleCodes();
}
