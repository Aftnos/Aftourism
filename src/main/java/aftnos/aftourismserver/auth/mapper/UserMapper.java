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
}
