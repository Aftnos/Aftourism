package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 认证授权模块的用户 Mapper 接口，负责用户基础信息的持久化操作。
 */
@Mapper
public interface UserMapper {

    int insert(User user);

    User findByUsername(@Param("username") String username);

    User findById(@Param("id") Long id);

    /**
     * 批量查询用户信息
     */
    List<User> findByIds(@Param("ids") List<Long> ids);

    /**
     * 条件分页查询门户用户。
     */
    List<User> search(@Param("username") String username,
                      @Param("nickname") String nickname,
                      @Param("status") Integer status);

    /**
     * 系统管理用户列表查询，增加手机号与邮箱过滤。
     */
    List<User> searchForManage(@Param("username") String username,
                               @Param("nickname") String nickname,
                               @Param("gender") String gender,
                               @Param("phone") String phone,
                               @Param("email") String email,
                               @Param("status") Integer status);

    /**
     * 更新门户用户角色。
     */
    int updateRole(@Param("id") Long id, @Param("roleCode") String roleCode);

    /**
     * 更新门户用户状态。
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
