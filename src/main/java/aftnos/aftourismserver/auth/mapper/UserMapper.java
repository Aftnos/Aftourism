package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 插入前台用户数据。
     */
    int insert(User user);

    /**
     * 根据用户名查询未删除的用户。
     */
    User findByUsername(@Param("username") String username);
}
