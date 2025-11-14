package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 更新门户用户角色。
     */
    int updateRole(@Param("id") Long id, @Param("roleCode") String roleCode);

    /**
     * 更新门户用户状态。
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
