package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import aftnos.aftourismserver.auth.pojo.UserRegisterStats;

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

    /**
     * 更新门户用户的个人资料，包含昵称、性别、联系方式等基础字段。
     */
    int updateProfile(@Param("id") Long id,
                      @Param("nickname") String nickname,
                      @Param("gender") String gender,
                      @Param("phone") String phone,
                      @Param("email") String email,
                      @Param("avatar") String avatar,
                      @Param("remark") String remark);

    /**
     * 统计门户用户总数
     */
    long countAll();

    /**
     * 统计指定时间范围内的新增用户数量
     */
    long countByCreateTimeRange(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    /**
     * 查询最新注册的门户用户列表
     */
    List<User> listLatest(@Param("limit") int limit);

    /**
     * 按天统计新增用户数量
     */
    List<UserRegisterStats> countDailyRegister(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}
