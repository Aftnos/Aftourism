package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    int insert(User user);

    User findByUsername(@Param("username") String username);
}
